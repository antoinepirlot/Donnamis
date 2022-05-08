import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  getAllPublicItems,
  getItem,
  getNumberOfInterestedMembers,
  modifyTheItem,
  postInterest as postInterestBackEnd
} from "../../../utils/BackEndRequests";
import {closeModal, openModal} from "../../../utils/Modals";
import {displayImage, getShowItemsHtml} from "../../../utils/HtmlCode";
import {sendFile} from "../../../utils/File";

const viewOfferHtml = `
<div id="offerCard" class="card mb-3" xmlns="http://www.w3.org/1999/html">
  <div class="row no-gutters">
    <div class="col-md" >
      <div class="card-body">
        <h2 id="titleViewItemPage" class="card-title"></h2>
        <p id="memberViewItemPage" class="text-muted"> </p>
        <h5 id="itemTypeViewItemPage" class="card-text"></h5>
        <h5 id="descriptionViewItemPage" class="card-text"></h5>
        <h5 id="availabilitiesViewItemPage" class="card-text"></h5>
        <h5 id="numberOfInterestedMembersViewItemPage" class="card-text"></h5>
        <h5 id="pubDateViewItemPage" class="card-text"></h5>
        <h5 id="oldPubDateViewItemPage" class="card-text"></h5>
        <button id="interestButton" class="btn btn-primary">
      </div>
    </div>
    <div class="col-md-4" id="imageItem">
    </div>
  </div>
  <div id="viewItemPageError"></div>
</div>
<!-- Modal Modify Item is in createModifyItemModal function-->

<div id="suggestedItemsTitle">
  <p class="display-4"> Voici des objets de la même catégorie : </p> 
  <div id="suggestedItems">
    
  </div>
</div>


<!-- Modal Post Interest -->
<div id="interestModal" class="modal">
  <div class="modal-content">
    <span id="interestCloseModal" class="close">&times;</span>
    <form id="interestForm">
      <h5>Marquer votre intéret pour cette offre</h5><br>
      <p>Date de récupération</p>
      <input id="dateForm" type="date">
      <input class="form-check-input" type="checkbox" id="callWanted">
      <label class="form-check-label" for="callWanted">J'accepte d'être appelé</label>
      <div id="phoneNumberInputDiv"></div>
      <br>
      <input type="submit" class="btn btn-primary" value="Confirmer">
    </form>
    <div id="postInterestMessage"></div>
  </div>
</div>
`;

let lastOffer;
let item;
let errorMessageDiv;
let he = require('he');

/**
 * Render the OfferPage :
 */
async function ViewItemPage() {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  //get param from url
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const idItem = urlParams.get("id");
  const page = document.querySelector("#page");
  page.innerHTML = viewOfferHtml;
  errorMessageDiv = document.querySelector("#viewItemPageError");
  try {
    item = await getItem(idItem);
    page.innerHTML += createModifyItemModal();
    await showItemInfo();
    const modifyMember = getObject("memberDTO");
    const postInterestButton = document.querySelector("#interestButton");
    if (item.member.id === modifyMember.id) {
      postInterestButton.innerText = "Modifier";
      //modify item
      postInterestButton.addEventListener("click", showModifyForm);
    } else {
      postInterestButton.innerText = "Je suis interessé(e) !";
      //post an interest
      postInterestButton.addEventListener("click", showInterestForm);
    }
    const suggestedItems = document.querySelector("#suggestedItems");
    const items = await getAllPublicItems();
    suggestedItems.innerHTML = getShowItemsHtml(
        items.filter(
            items => items.itemType.itemType == item.itemType.itemType
                && items.id !== item.id));
  } catch (e) {
    console.error(e);
    showError("Une erreur est survenue.", "danger", errorMessageDiv);
  }
}

function createModifyItemModal() {
  let itemDescription = item.itemDescription ? item.itemDescription
      : "";
  let timeSlot = item.offerList[0].timeSlot ? item.offerList[0].timeSlot : "";
  return `
    <!-- Modal Modify Item -->
    <div id="modifyItemModal" class="modal">
      <div class="modal-content">
        <span id="modifyItemCloseModal" class="close">&times;</span>
        <form id="modifyItemForm">
          <h5>Modifier votre objet<span id="asterisk">*</span>:</h5><br>
          <p>Description de l'objet<span id="asterisk">*</span>:</p>
          <textarea id="itemDescriptionForm" cols="30" rows="3">${itemDescription}</textarea>
          <p>Photo</p>
          <input id="photoForm" name="changePhoto" type="file"><br>
          <br>
          <p>Disponibilités horaire<span id="asterisk">*</span>:</p>
          <textarea id="timeSlotModifyForm" cols="30" rows="3">${timeSlot}</textarea>
          <br>
          <input type="submit" value="Modifier">
        </form>
        <div id="modifyItemMessage"></div>
      </div>
    </div>
  `;
}

async function showItemInfo() {
  lastOffer = item.offerList[0];
  let date = new Date(lastOffer.date);
  date = date.getDate() + "/" + (date.getMonth() + 1) + "/"
      + date.getFullYear();
  if (item.offerList.length === 2) {
    const oldOffer = item.offerList[1];
    let oldPubDate = new Date(oldOffer.date);
    oldPubDate = oldPubDate.getDate() + "/" + (oldPubDate.getMonth() + 1) + "/"
        + oldPubDate.getFullYear();
    const oldPubDateDiv = document.querySelector("#oldPubDateViewItemPage");
    oldPubDateDiv.innerHTML = `Date de publication précédente : ${oldPubDate}`;
  }

  const titleDiv = document.querySelector("#titleViewItemPage");
  titleDiv.innerHTML = he.decode(item.title);

  const memberDiv = document.querySelector("#memberViewItemPage");
  memberDiv.innerHTML = `Offre proposée par : ${he.decode(
      item.member.firstName)} ${he.decode(item.member.lastName)} `;

  const itemType = document.querySelector("#itemTypeViewItemPage");
  itemType.innerHTML = `Type : ${he.decode(item.itemType.itemType)}`;

  const descriptionDiv = document.querySelector("#descriptionViewItemPage");
  descriptionDiv.innerHTML = `Description : ${he.decode(item.itemDescription)}`;

  const availabilitiesDiv = document.querySelector(
      "#availabilitiesViewItemPage");
  availabilitiesDiv.innerHTML = `Disponibilités : ${lastOffer.timeSlot}`;

  const pubDateDiv = document.querySelector("#pubDateViewItemPage");
  pubDateDiv.innerHTML = `Date de publication : ${date}`;

  const image = document.querySelector("#imageItem");
  image.innerHTML = displayImage(item, true);

  const numberOfMemberInterested = document.querySelector(
      "#numberOfInterestedMembersViewItemPage");
  const count = await getNumberOfInterestedMembers(item.id);
  numberOfMemberInterested.innerHTML = `Nombre de personnes intéressée(s) : ${count}`;
}

async function showInterestForm(e) {
  e.preventDefault();
  openModal("#interestModal", "#interestCloseModal");

  const callWantedCheckbox = document.querySelector("#callWanted");
  callWantedCheckbox.addEventListener("click", showPhoneNumberInput);

  const interestForm = document.querySelector("#interestForm");
  interestForm.addEventListener("submit", postInterest);
}

function showPhoneNumberInput() {
  const phoneNumberInputDiv = document.querySelector("#phoneNumberInputDiv");
  let phoneNumberInputHtml;
  const memberDTO = getObject("memberDTO");

  if (memberDTO.phoneNumber) {
    phoneNumberInputHtml = `
      <input id="phoneNumberInput" type="tel" value="${memberDTO.phoneNumber}">
    `;
  } else {
    phoneNumberInputHtml = `
      <input id="phoneNumberInput" type="tel" placeholder="Téléphone">
    `;
  }
  if (!phoneNumberInputDiv.querySelector("#phoneNumberInput")) {
    phoneNumberInputDiv.innerHTML = phoneNumberInputHtml;
  } else {
    phoneNumberInputDiv.innerHTML = "";
  }
}

async function postInterest(e) {
  e.preventDefault();
  const interestMessage = document.querySelector("#postInterestMessage");
  const callWanted = document.querySelector("#callWanted").checked;
  const date = document.querySelector("#dateForm").value;
  if (!date) {
    showError("La date n'a pas été choisie.", "danger", interestMessage);
    return;
  }
  const memberInterested = {
    id: getPayload().id
  };
  if (callWanted) {
    memberInterested.phoneNumber = document.querySelector(
        "#phoneNumberInput").value;
  }
  const interest = {
    callWanted: callWanted,
    offer: lastOffer,
    member: memberInterested,
    date: date
  };
  let pageErrorDiv = document.querySelector("#viewItemPageError");
  try {
    const status = await postInterestBackEnd(interest);
    if (status === 409) {
      showError("Vous avez déjà marqué un intéret pour cette offre.", "danger",
          pageErrorDiv);
      return;
    }
    if (callWanted) {
      await checkToken();
    }
    await ViewItemPage();
    pageErrorDiv = document.querySelector("#viewItemPageError");
    showError("L'intérêt a bien été pris en compte.", "success", pageErrorDiv);
  } catch (err) {
    console.error(err);
  } finally {
    closeModal("#interestModal");
  }
}

async function showModifyForm(e) {
  e.preventDefault();
  openModal("#modifyItemModal", "#modifyItemCloseModal");
  const modifyForm = document.querySelector("#modifyItemForm");
  modifyForm.addEventListener("submit", modifyItem);
}

async function modifyItem(e) {
  e.preventDefault();
  const itemDescription = document.querySelector("#itemDescriptionForm").value;
  const photo = document.querySelector("#photoForm").value;
  const timeSlot = document.querySelector("#timeSlotModifyForm").value;
  const newItem = {
    id: item.id,
    itemDescription: itemDescription,
    photo: photo,
    lastOffer: {
      timeSlot: timeSlot
    },
    version: item.version
  }
  try {
    await modifyTheItem(newItem);
    await sendFile(newItem.id, "changePhoto");
    await ViewItemPage();
    const errorMessage = document.querySelector("#modifyItemMessage");
    showError("Modification validées", "success", errorMessage);
  } catch (error) {
    console.error(error);
    closeModal("#modifyItemModal");
  }
}

export default ViewItemPage;
