import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  getItem, me,
  postInterest as postInterestBackEnd
} from "../../../utils/BackEndRequests";

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
  //get offer's infos with the id in param
  await getItemInfo(idItem);
  //post an interest
  button.addEventListener("submit", postInterest);
}

function showPhoneNumberInput() {
  const phoneNumberInputDiv = document.querySelector("#phoneNumberInputDiv");
  let phoneNumberInputHtml;
  const memberDTO = getObject("memberDTO");
  console.log(memberDTO)
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
  } catch (err) {
    console.error(err);
  }
}

async function postInterest(e) {
  console.log("postInterest");
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

export default ViewItemPage;
