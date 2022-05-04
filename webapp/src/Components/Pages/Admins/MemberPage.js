import {
  getAllItemsByMemberIdAndOfferStatus,
  getNumberOfItems,
  getNumberOfReceivedOrNotReceivedItems,
  getOneMember,
  setMemberAvailability
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {Redirect} from "../../Router/Router";
import {isAdmin} from "../../../utils/session";

const memberPageHtml = `
  <div id="memberPageContent" class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2 id="profilUsernameMemberPage" class="display-3"></h2>
  </div>
  <div id="errorMessage"></div>
  <div id="donatedItemsMemberPage">
    <h4>Objets offerts par ce membre</h4>
    <div id="donatedItemsMemberPageMessage"></div>
  </div>
  <div id="receivedItemsMemberPage">
    <h4>Objets reçus par ce membre</h4>
    <div id="receivedItemsMemberPageMessage"></div>
  </div>
`;

let idMember;
let he = require('he');

const MemberPage = async () => {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }

  const page = document.querySelector("#page");
  page.innerHTML = memberPageHtml;
  idMember = new URLSearchParams(window.location.search).get("id");
  const member = await getOneMember(idMember);
  const profilUsernameDiv = document.querySelector(
      "#profilUsernameMemberPage");
  const username = he.decode(member.username);
  profilUsernameDiv.innerText = `Profile de: ${he.decode(username)}`;
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
    <p>
      Prénom : ${he.decode(member.firstName)}<br>
      Nom : ${he.decode(member.lastName)}<br>
      ${getAddressHtml(member.address)}<br>
      Statut : ${getActualState(member)}<br>
      Administrateur : ${member.isAdmin ? "Oui" : "Non"}<br>
      Numéro de téléphone : ${member.phoneNumber ? member.phoneNumber : "Aucun"}<br>
      Nombre d'objets offerts : ${await getNumberOfItems(member.id, "donated")}<br>
      Nombre d'objets donnés : ${await getNumberOfItems(member.id, "given")}<br>
      Nombre d'objets intéréssé mais non reçu : ${await getNumberOfReceivedOrNotReceivedItems(
      member.id, false)}<br>
      Nombre d'objets reçus : ${await getNumberOfReceivedOrNotReceivedItems(
      member.id, true)}<br>
  `;
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

      try {
        await setMemberAvailability(memberUnavailable, pageErrorDiv);
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