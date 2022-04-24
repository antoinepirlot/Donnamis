import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  getItem,
  getItemsTypes,
  modifyTheItem,
  postInterest as postInterestBackEnd
} from "../../../utils/BackEndRequests";
import {closeModal, openModal} from "../../../utils/Modals";
import {showItemsTypes} from "../../../utils/HtmlCode";

const viewOfferHtml = `
<div id="offerCard" class="card mb-3">
  <div class="row no-gutters">
  <div class="col-md">
      <div class="card-body">
        <h2 id="titleViewItemPage" class="card-title"></h2>
        <p id="memberViewItemPage" class="text-muted"> </p>
        <h5 id="itemTypeViewItemPage" class="card-text"></h5>
        <h5 id="descriptionViewItemPage" class="card-text"></h5>
        <h5 id="availabilitiesViewItemPage" class="card-text"></h5>
        <h5 id="pubDateViewItemPage" class="card-text"></h5>
        <button id="interestButton" class="btn btn-primary">
      </div>
    </div>
    <div class="col-md-4">
      <img src="" class="card-img" alt="JS">
    </div>
  </div>
  <div id="viewItemPageError"></div>
</div>
<!-- Modal Modify Item is in createModifyItemModal function-->

<!-- Modal Post Interest -->
<div id="interestModal" class="modal">
  <div class="modal-content">
    <span id="interestCloseModal" class="close">&times;</span>
    <form id="interestForm">
      <h5>Marquer votre intéret pour cette offre</h5><br>
      <p>Date de récupération</p>
      <input id="dateForm" type="date">
      <p></p>
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
  errorMessageDiv = document.querySelector("#viewItemPageError");
  page.innerHTML = viewOfferHtml;
  try {
    item = await getItem(idItem);
    page.innerHTML += createModifyItemModal();
    showItemInfo();
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
  } catch (e) {
    console.error(e);
  }
}

function createModifyItemModal() {
  let title = item.title ? item.title : "";
  let itemDescription = item.itemDescription ? item.itemDescription : "";
  let itemType = item.itemType.itemType ? item.itemType.itemType : "";
  return `
    <!-- Modal Modify Item -->
    <div id="modifyItemModal" class="modal">
      <div class="modal-content">
        <span id="modifyItemCloseModal" class="close">&times;</span>
        <form id="modifyItemForm">
          <h5>Modifier votre objet</h5><br>
          <p>Nom de l'objet</p>
          <input id="titleForm" type="text" value="${title}">
          <p>Description de l'objet</p>
          <input id="itemDescriptionForm" type="text" value="${itemDescription}">
          <p>Photo</p>
          <input id="photoForm" type="file">
          <p>Type de l'objet</p>
          <input id="itemTypeFormList" list="itemsTypesViewItemPage" value="${itemType}"><br>
          <datalist id="itemsTypesViewItemPage"></datalist>
          <br>
          <input type="submit" value="Modifier">
        </form>
        <div id="modifyItemMessage"></div>
      </div>
    </div>
  `;
}

function showItemInfo() {
  lastOffer = item.offerList[0];
  let date = new Date(lastOffer.date);
  date = date.getDate() + "/" + (date.getMonth() + 1) + "/"
      + date.getFullYear();

  const titleDiv = document.querySelector("#titleViewItemPage");
  titleDiv.innerHTML = item.title;
  const memberDiv = document.querySelector("#memberViewItemPage");
  memberDiv.innerHTML = `Offre proposée par : ${item.member.firstName} ${item.member.lastName} `;
  const itemType = document.querySelector("#itemTypeViewItemPage");
  itemType.innerHTML = `Type : ${item.itemType.itemType}`;
  const descriptionDiv = document.querySelector("#descriptionViewItemPage");
  descriptionDiv.innerHTML = `Description : ${item.itemDescription}`;
  const availabilitiesDiv = document.querySelector(
      "#availabilitiesViewItemPage");
  availabilitiesDiv.innerHTML = `Disponibilités : ${lastOffer.timeSlot}`;
  const pubDateDiv = document.querySelector("#pubDateViewItemPage");
  pubDateDiv.innerHTML = `Date de publication : ${date}`;
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
  console.log(interestMessage);

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
    memberInterested.phoneNumber = document.querySelector("#phoneNumberInputDiv").value;
  }
  const interest = {
    callWanted: callWanted,
    offer: lastOffer,
    member: memberInterested,
    date: date
  };
  try {
    await postInterestBackEnd(interest, errorMessageDiv);
    if (callWanted) {
      await checkToken();
    }
  } catch (err) {
    console.error(err);
  } finally {
    closeModal("#interestModal");
  }
}

async function showModifyForm(e) {
  e.preventDefault();
  openModal("#modifyItemModal", "#modifyItemCloseModal");
  showItemsTypes("#itemsTypesViewItemPage", await getItemsTypes());
  const modifyForm = document.querySelector("#modifyItemForm");
  modifyForm.addEventListener("submit", modifyItem);
}

async function modifyItem(e) {
  e.preventDefault();
  const title = document.querySelector("#titleForm").value;
  const itemDescription = document.querySelector("#itemDescriptionForm").value;
  const photo = document.querySelector("#photoForm").value;
  const itemTypeValue = document.querySelector("#itemTypeFormList").value;

  const itemType = {
    itemType: itemTypeValue
  }
  const newItem = {
    id: item.id,
    itemDescription: itemDescription,
    itemType: itemType,
    photo: photo,
    title: title,
  }
  try {
    await modifyTheItem(newItem);
    const errorMessage = document.querySelector("#modifyItemMessage");
    showError("Modification validé", "success", errorMessage);
    await ViewItemPage();
  } catch (error) {
    console.error(error);
    closeModal("#modifyItemModal");
  }
}

export default ViewItemPage;
