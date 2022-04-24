import {getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {modifyMember as modifyMemberBackEnd} from "../../../utils/BackEndRequests";

const viewProfileHtml = `
  <div class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2 class="display-3">Mon profile</h2>
    <div id="username"></div>
    <div id="name"></div>
    <div id="phone"></div>
    <div id="address"></div>
  </div>
  <div class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2 class="display-3">Modifier mon profile</h2>
    <form id="modifyForm">
      <p>Nom :</p>
      <input id="nameForm" type = "text">
      <p>Prénom :</p>
      <input id="firstnameForm" type = "text">
      <p>Pseudo :</p>
      <input id="usernameForm" type = "text">
      <p>Mot de passe :</p>
      <input id="passwordForm" type = "text">
      <p>Téléphone :</p>
      <input id="phoneForm" type = "text">
      <p></p>
      <input type= "submit" value = "Modifier">
    </form> 
  </div>
  <div id="errorMessage"></div>
`;

async function ProfilePage() {
  if (!await getPayload()) {
    Redirect("/");
    return;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";
  pageDiv.innerHTML += viewProfileHtml;
  showProfile();
  const modifyForm = document.querySelector("#modifyForm");
  modifyForm.addEventListener("submit", await modifyProfile);
}

function showProfile() {
  const member = getObject("memberDTO");
  const address = member.address;
  const phone = member.phoneNumber;

  const usernameDiv = document.querySelector("#username");
  const nameDiv = document.querySelector("#name");
  const phoneDiv = document.querySelector("#phone");
  const addressDiv = document.querySelector("#address");

  usernameDiv.innerHTML += member.username;
  nameDiv.innerHTML += member.lastName + " " + member.firstName;
  if (phone == null) {
    phoneDiv.innerHTML += "Aucun numéro de téléphone";
  } else {
    phoneDiv.innerHTML += phone;
  }
  addressDiv.innerHTML += address.street + " ";
  addressDiv.innerHTML += address.buildingNumber + " ";
  addressDiv.innerHTML += address.postcode + " ";
  addressDiv.innerHTML += address.commune + " ";
}

async function modifyProfile(e) {
  e.preventDefault();

  const lastName = document.querySelector("#nameForm").value;
  const firstName = document.querySelector("#firstnameForm").value;
  const username = document.querySelector("#usernameForm").value;
  const password = document.querySelector("#passwordForm").value;
  const phoneNumber = document.querySelector("#phoneForm").value;

  const member = {
    id: getPayload().id,
    username: username,
    password: password,
    lastName: lastName,
    firstName: firstName,
    phoneNumber: phoneNumber
  };
  try {
    await modifyMemberBackEnd(member);
    const errorMessage = document.querySelector("#errorMessage");
    showError("Modification validé", "success", errorMessage);
  } catch (error) {
    console.error(error);
  }
}

export default ProfilePage;