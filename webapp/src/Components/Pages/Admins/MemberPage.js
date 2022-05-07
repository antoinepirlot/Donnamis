import {
  getAllItemsByMemberIdAndOfferStatus,
  getNumberOfItems,
  getNumberOfReceivedOrNotReceivedItems,
  getOneMember,
  setMemberAvailability,
  setRecipientUnavailable
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {Redirect} from "../../Router/Router";
import {isAdmin} from "../../../utils/session";

const memberPageHtml = `
  <div class="whiteCard">
    <div id="memberPageContent" ><!--class="d-flex bd-highlight mb-3 shadow-lg p-3 mb-5 bg-white rounded" -->
    </div>
    <div id="donatedItemsMemberPage">
    <h4>Objets offerts par ce membre</h4>
    <div id="donatedItemsMemberPageMessage"></div>
  </div>
  <div id="receivedItemsMemberPage">
    <h4>Objets reçus par ce membre</h4>
  <div id="receivedItemsMemberPageMessage"></div>
  </div>
  <div id="errorMessage"></div>
  
`;

let idMember;
let he = require('he');

const MemberPage = async () => {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }
  idMember = new URLSearchParams(window.location.search).get("id");
  const member = await getOneMember(idMember);
  const page = document.querySelector("#page");
  page.innerHTML = `<h1 class="display-3" id="login_title">Profile de ${he.decode(
      member.username)}</h1>`;
  page.innerHTML += memberPageHtml;
  await showMemberInformation(member);
  await showDonatedItems(member);
  await showReceivedItems(member);
}

async function showDonatedItems(member) {
  const donatedItems = await getAllItemsByMemberIdAndOfferStatus(member.id,
      "donated");
  console.log(donatedItems)
  if (!donatedItems) {
    const messageDiv = document.querySelector("#donatedItemsMemberPageMessage");
    showError("Ce membre n'a pas d'objet offerts", "info", messageDiv);
    return;
  }
  const donatedItemsDiv = document.querySelector("#donatedItemsMemberPage");
  donatedItemsDiv.innerHTML += getShowItemsHtml(donatedItems);
}

async function showReceivedItems(member) {
  const receivedItems = await getAllItemsByMemberIdAndOfferStatus(member.id,
      "given");
  if (!receivedItems) {
    const messageDiv = document.querySelector(
        "#receivedItemsMemberPageMessage");
    showError("Ce membre n'a pas d'objet offerts", "info", messageDiv);
    return;
  }
  const receivedItemsDiv = document.querySelector("#receivedItemsMemberPage");
  receivedItemsDiv.innerHTML += getShowItemsHtml(receivedItems);
}

async function showMemberInformation(member) {
  const content = document.querySelector("#memberPageContent");
  let contentHtml = `
    <h3>Infos de ${he.decode(member.username)}</h3>
    <p>Prénom : <span id="firstnameMemberPage" >${he.decode(member.lastName)}</span> </p>
    <p >Nom : <span id="lastnameMemberPage">${he.decode(member.lastName)}</span></p>
    <p >Statut : <span id="statusMemberPage">${getActualState(member)}</span> </p>
    <p>Administrateur : <span id="adminMemberPage">${member.isAdmin ? "Oui" : "Non"}</span></p>
    <h3>Adresse de ${he.decode(member.username)}</h3>
  `
  //  <div id="left" class="" xmlns="http://www.w3.org/1999/html">
  //    <h3>Infos de ${he.decode(member.username)}</h3>
  //    <label>Prénom: </label>
  //    <p class="">${he.decode(member.firstName)}</p>
  //    Nom : ${he.decode(member.lastName)}<br>
  //    Statut : ${getActualState(member)}<br>
  //    Administrateur : ${member.isAdmin ? "Oui" : "Non"}<br>
  //  </div>
  //  <div id="right" class="p-2 bd-highlight">
  //    <h3>Adresse de ${he.decode(member.username)}</h3>
  //    ${getAddressHtml(member.address)}<br>
  //  </div>
  //    Numéro de téléphone : ${member.phoneNumber ? member.phoneNumber : "Aucun"}<br>
  //    Nombre d'objets offerts : ${await getNumberOfItems(member.id, "donated")}<br>
  //    Nombre d'objets donnés : ${await getNumberOfItems(member.id, "given")}<br>
  //    Nombre d'objets intéréssé mais non reçu : ${await getNumberOfReceivedOrNotReceivedItems(
  //    member.id, false)}<br>
  //    Nombre d'objets reçus : ${await getNumberOfReceivedOrNotReceivedItems(
  //    member.id, true)}<br>
  //`;
  content.innerHTML += contentHtml;
  const pageErrorDiv = document.querySelector("#errorMessage");
  let button;

  //Create button if member confirmed or unavailable
  if (member.actualState === 'confirmed' || member.actualState
      === 'unavailable') {
    content.innerHTML += `<button id="markUnavailableButton"></button>`;
    button = document.querySelector("#markUnavailableButton");

    //Change the value of the button
    if (member.actualState === 'confirmed') {
      button.innerHTML = "Marquer Indisponible";
    } else if (member.actualState === 'unavailable') {
      button.innerHTML = "Marquer Disponible";
    }

    button.addEventListener("click", async function () {

      const memberUnavailable = {
        id: member.id,
        actualState: member.actualState,
        version: member.version
      };

      const recipient = {
        member: memberUnavailable
      };

      try {
        await setMemberAvailability(memberUnavailable, pageErrorDiv);
        if (memberUnavailable.actualState === "confirmed") {
          await setRecipientUnavailable(recipient);
        }
        await MemberPage();
      } catch (err) {
        console.error(err);
      }
    });
  }
}

function getActualState(member) {
  switch (member.actualState) {
    case "registered":
      return "Inscrit";
    case "confirmed":
      return "Confirmé";
    case "denied":
      return "Refusé";
    default:
      "Statut inconnu";
  }
}

function getAddressHtml(address) {
  let addressHtml = `
    Adresse : ${address.street} n°${address.buildingNumber}
  `;
  if (address.unitNumber) {
    addressHtml += `
      Boite ${address.unitNumber}
    `;
  }
  addressHtml += `
    ${address.postcode} ${address.commune}
    
  `;
  return he.decode(addressHtml);
}

export default MemberPage