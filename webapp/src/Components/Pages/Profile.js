import {getObject, getPayload,} from "../../utils/session";
import {Redirect} from "../Router/Router";

const viewProfileHtml = `
  <div class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2>Mon profile</h2>
    <div id="username"></div>
    <div id="name"></div>
    <div id="phone"></div>
    <div id="address"></div>
  </div>
`;

async function ProfilePage() {
  if (!await getPayload()) {
    Redirect("/");
    return;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML += viewProfileHtml;
  showProfile();
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

export default ProfilePage;