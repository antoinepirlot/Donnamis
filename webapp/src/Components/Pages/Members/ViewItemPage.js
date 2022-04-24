import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  getItem,
  modifyTheItem,
  postInterest as postInterestBackEnd
} from "../../../utils/BackEndRequests";
import {closeModal, openModal} from "../../../utils/Modals";

const viewOfferHtml = `
<div id="offerCard" class="card mb-3">
  <div class="row no-gutters">
  <div class="col-md">
      <div class="card-body">
        <h2 id="title" class="card-title"></h2>
        <p id="offerer" class="text-muted"> </p>
        <h5 id="type" class="card-text"></h5>
        <h5 id="description" class="card-text"></h5>
        <h5 id="availabilities" class="card-text"></h5>
        <h5 id="pubDate" class="card-text"></h5>
        <button id="interestButton" class="btn btn-primary">
      </div>
    </div>
    <div class="col-md-4">
      <img src="" class="card-img" alt="JS">
    </div>
  </div>
  <div id="viewItemPageError"></div>
</div>
<!-- Modal Modify Offer -->
<div id="modifyItemModal" class="modal">
  <div class="modal-content">
    <span id="modifyItemCloseModal" class="close">&times;</span>
    <form id="modifyItemForm">
      <h5>Modifier votre objet</h5><br>
      <p>Nom de l'objet</p>
      <input id="titleForm" type="text">
      <p>Description de l'objet</p>
      <input id="itemDescriptionForm" type="text">
      <p>Photo</p>
      <input id="photoForm" type="file">
      <p>Type de l'objet</p>
      <input id="itemTypeFormList" list="itemsTypes"><br>
      <p></p>
      <input type="submit" value="Modifier">
    </form>
    <div id="modifyInterestMessage"></div>
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
let idItem;
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
  idItem = urlParams.get("id");

  const page = document.querySelector("#page");
  page.innerHTML = viewOfferHtml;
  errorMessageDiv = document.querySelector("#viewItemPageError")

  const postInterestButton = document.querySelector("#interestButton");
  //get item's information with the id in param

  const item = await getItemInfo();
  const modifyMember = getObject("memberDTO");

  if (item.member.id === modifyMember.id) {
    postInterestButton.innerText = "Modifier";
    //modify item
    postInterestButton.addEventListener("click", showModifyForm);
  } else {
    postInterestButton.innerText = "Je suis interessé(e) !";
    //post an interest
    postInterestButton.addEventListener("click", showInterestForm);
  }
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

async function getItemInfo() {
  try {
    const item = await getItem(idItem);
    lastOffer = item.offerList[0];
    let date = new Date(lastOffer.date);
    date = date.getDate() + "/" + (date.getMonth() + 1) + "/"
        + date.getFullYear();

    document.querySelector("#title").innerHTML = item.title
    document.querySelector(
        "#offerer").innerHTML = `Offre proposée par : ${item.member.firstName} ${item.member.lastName} `
    document.querySelector(
        "#type").innerHTML = `Type : ${item.itemType.itemType}`
    document.querySelector(
        "#description").innerHTML = `Description : ${item.itemDescription}`
    document.querySelector(
        "#availabilities").innerHTML = `Disponibilités : ${lastOffer.timeSlot}`
    document.querySelector(
        "#pubDate").innerHTML = `Date de publication : ${date}`
    return item;
  } catch (err) {
    console.error(err);
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
  const modifyForm = document.querySelector("#modifyItemForm");
  modifyForm.addEventListener("submit", modifyItem);
}

async function showInterestForm(e) {
  e.preventDefault();
  openModal("#interestModal", "#interestCloseModal");

  const callWantedCheckbox = document.querySelector("#callWanted");
  callWantedCheckbox.addEventListener("click", showPhoneNumberInput);

  const interestForm = document.querySelector("#interestForm");
  interestForm.addEventListener("submit", postInterest);
}

async function modifyItem(e) {
  e.preventDefault();

  const title = document.querySelector("#titleForm");
  const itemDescription = document.querySelector("#itemDescriptionForm");
  const photo = document.querySelector("#photoForm");
  const newItemType = document.querySelector("#itemTypeFormList");
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const idItem = urlParams.get("id");

  const itemType = {
    itemType: newItemType.value
  }

  const item = {
    id: idItem,
    itemDescription: itemDescription.value,
    itemType: itemType,
    photo: photo.value,
    title: title.value,
  }

  try {
    await modifyTheItem(item);
    const errorMessage = document.querySelector("#modifyInterestMessage");
    showError("Modification validé", "success", errorMessage);
    await ViewItemPage();
  } catch (error) {
    console.error(error);
  }
}

export default ViewItemPage;
