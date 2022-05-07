import {checkToken, getObject, getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {
  modifyMember as modifyMemberBackEnd
} from "../../../utils/BackEndRequests";
import {getProfileFormHtml} from "../../../utils/HtmlCode";

const viewProfileHtml = `
  <div class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2 class="display-4">Modifier mon profile</h2>
    <form id="modifyForm">
    </form> 
  </div>
  <div id="errorMessage"></div>
`;

async function ProfilePage() {
  if (!getPayload()) {
    Redirect("/");
    return;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";
  pageDiv.innerHTML += viewProfileHtml;
  const modifyForm = document.querySelector("#modifyForm");
  modifyForm.innerHTML = getProfileFormHtml();
  modifyForm.addEventListener("submit", await modifyProfile);
}

async function modifyProfile(e) {
  e.preventDefault();
  const member = getObject("memberDTO");
  const address = member.address;
  let errorMessage = document.querySelector("#errorMessage");
  const password = document.querySelector("#passwordForm").value;
  const passwordConfirmation = document.querySelector(
      "#passwordConfirmationForm").value;
  if (password !== passwordConfirmation) {
    const message = "Les mots de passes ne sont pas identiques. Les modification n'ont pas été acceptées";
    showError(message, "danger", errorMessage);
  }

  member.id = getPayload().id;
  member.lastName = document.querySelector("#nameForm").value;
  member.firstName = document.querySelector("#firstnameForm").value;
  member.username = document.querySelector("#usernameForm").value;
  member.phoneNumber = document.querySelector("#phoneForm").value;
  member.password = password;

  address.street = document.querySelector("#streetFormProfilePage").value;
  address.buildingNumber = document.querySelector("#buildingNumberFormProfilePage").value;
  address.unitNumber = document.querySelector("#unitNumberFormProfilePage").value;
  address.postcode = document.querySelector("#postCodeFormProfilePage").value;
  address.commune = document.querySelector("#communeFormProfilePage").value;

  try {
    await modifyMemberBackEnd(member);
    await checkToken();
    await ProfilePage();
    errorMessage = document.querySelector("#errorMessage");
    showError("Modification validée", "success", errorMessage);
  } catch (error) {
    showError("Un problème est survenue", "danger", errorMessage);
    console.error(error);
  }
}

export default ProfilePage;