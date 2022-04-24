import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  getItem,
  modifyTheItem,
  postInterest as postInterestBackEnd
} from "../../../utils/BackEndRequests";
import {openModal} from "../../../utils/Modals";

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

        <form>
          <div id="call_wanted"></div>
           <input id="interestButton" type="submit" class="btn btn-primary">
        <form id="interestForm">
          <div id="interestsInputs" class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="callWanted">
            <label class="form-check-label" for="callWanted">J'accepte d'être appelé</label>
            <div id="phoneNumberInputDiv"></div>
          </div>
          <input id="interestButton" type="submit" class="btn btn-primary" value="Je suis interessé(e) !">
       </form>

       <div class="message" id="interestMessage"></div>
      </div>
    </div>
    <div class="col-md-4">
      <img src="" class="card-img" alt="JS">
    </div>
  </div>
</div>
<!-- The Modal -->
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
  </div>
  <div id="errorMessage"></div>
</div>
`;

let lastOffer;

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
  const page = document.querySelector("#page");
  const idItem = urlParams.get("id");
  page.innerHTML = viewOfferHtml;

  const callWanted = document.querySelector("#callWanted");
  callWanted.addEventListener("click", showPhoneNumberInput)

  const button = document.querySelector("#offerCard");
  const form = document.querySelector("#offerCard");
  //get offer's infos with the id in param

  const item = await getItemInfo(idItem);
  const modifyMember = getObject("memberDTO");
  const button2 = document.querySelector("#interestButton");

  if (item.member.id == modifyMember.id) {
    button2.value = "Modifier";
    //modify item
    form.addEventListener("submit", showModifyForm);
  } else {
    const call_wanted = document.querySelector("#call_wanted");
    call_wanted.innerHTML = ` 
        <input class=\"form-check-input\" type=\"checkbox\" id=\"callWanted\">
        <label class=\"form-check-label\" for=\"callWanted\">J'accepte d'être appelé</label>`;
    button2.value = "Je suis interessé(e) !";
    //post an interest
    form.addEventListener("submit", postInterest);
  }
}

function showPhoneNumberInput() {
  const phoneNumberInputDiv = document.querySelector("#phoneNumberInputDiv");
  let phoneNumberInputHtml;
  const memberDTO = getObject("memberDTO");

  if (memberDTO.phoneNumber) {
    phoneNumberInputHtml = `
      <input id="phoneNumberInput" type="tel" value="${memberDTO.phoneNumber}" pattern="(+?[0-9]{3})[0-9]{13}">
    `;
  } else {
    phoneNumberInputHtml = `
      <input id="phoneNumberInput" type="tel" placeholder="Téléphone" pattern="(+?[0-9]{3})[0-9]{13}">
    `;
  }
  if (!phoneNumberInputDiv.querySelector("#phoneNumberInput")) {
    phoneNumberInputDiv.innerHTML = phoneNumberInputHtml;
  } else {
    phoneNumberInputDiv.innerHTML = "";
  }
}

async function getItemInfo(idItem) {
  try {
    const item = await getItem(idItem);
    lastOffer = item.offerList[0];
    var date = new Date(lastOffer.date);
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
  const interestMessage = document.querySelector("#interestMessage");
  //const urlParams = new URLSearchParams(queryString);
  const callWanted = document.querySelector("#callWanted").checked;
  const member = {
    id: getPayload().id
  };
  if (callWanted) {
    member.phoneNumber = document.querySelector("#phoneNumberInput").value;
  }
  const interest = {
    callWanted: callWanted,
    offer: lastOffer,
    member: member
  };
  try {
    await postInterestBackEnd(interest, interestMessage);
    if (callWanted) {
      await checkToken();
    }
  } catch (err) {
    showError("Votre marque d'intérêt n'a pas pu être ajoutée", "danger",
        interestMessage);
    console.error(err);
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
    const errorMessage = document.querySelector("#errorMessage");
    showError("Modification validé", "success", errorMessage);
    await ViewItemPage();
  } catch (error) {
    console.error(error);
  }
}

export default ViewItemPage;
