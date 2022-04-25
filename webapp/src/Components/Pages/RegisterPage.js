import {getPayload,} from "../../utils/session";
import {register as registerBackEndRequest} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import {showError} from "../../utils/ShowError";

const registerFormHtml = `
<h1 class="display-3" id="login_title">S'inscrire</h1>
<div class="form">
  <h3>Mes infos</h3>
  <form  id="registerForm">
    <div class="mb-3">
      <label class="form-label">Pseudo</label>
      <input type="text" class="form-control" id="usernameInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Mot de passe</label>
      <input type="password" class="form-control" id="passwordInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Prénom</label>
      <input type="text" class="form-control" id="firstNameInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Nom</label>
      <input type="text" class="form-control" id="lastNameInput">
    </div>
    
    <h3>Adresse</h3>
    
    <div class="mb-3">
      <label class="form-label">Rue</label>
      <input type="text" class="form-control" id="streetInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Numero</label>
      <input type="text" class="form-control" id="buildingNumberInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Boîte (facultatif)</label>
      <input type="text" class="form-control" id="unitNumberInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Commune</label>
      <input type="text" class="form-control" id="communeInput">
    </div>
    
    <div class="mb-3">
      <label class="form-label">Code postal</label>
      <input type="text" class="form-control" id="postcodeInput">
    </div>
    
    <div class="message" id="registerMessage"></div>
    <button type="submit" class="btn btn-primary">S'inscrire</button>
  </form>
</div>
<br>
`;

/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router
 * to "redirect" to a new page
 */
function RegisterPage() {
  if (getPayload()) {
    Redirect("/");
    return;
  }
  const page = document.querySelector("#page");
  page.innerHTML = registerFormHtml;
  page.addEventListener("submit", register);

}

async function register(e) {
  e.preventDefault();
  //div
  const registerMessage = document.querySelector("#registerMessage");
  showError("Inscription en cours...", "info", registerMessage);
  //member
  const username = document.querySelector("#usernameInput").value;
  const password = document.querySelector("#passwordInput").value;
  const firstName = document.querySelector("#firstNameInput").value;
  const lastName = document.querySelector("#lastNameInput").value;
  //address
  const street = document.querySelector("#streetInput").value;
  const buildingNumber = document.querySelector("#buildingNumberInput").value;
  const unitNumber = document.querySelector("#unitNumberInput").value;
  const commune = document.querySelector("#communeInput").value;
  const postcode = document.querySelector("#postcodeInput").value;

  if (
      !username || !password || !firstName || !lastName || !street || !commune
      ||
      !buildingNumber || !postcode
  ) {
    showError("Tous les champs doivent être complet", "danger", registerMessage)
    return;
  }
  const address = {
    street: street,
    buildingNumber: buildingNumber,
    commune: commune,
    postcode: postcode
  };
  if (unitNumber) {
    // add the unitNumber to address if it exists
    address.unitNumber = unitNumber;
  }
  const member = {
    username: username,
    password: password,
    firstName: firstName,
    lastName: lastName,
    address: address
  }
  try {
    await registerBackEndRequest(member);
  } catch (err) {
    showError("Echec de l'inscription", "danger", registerMessage);
    console.error(err);
  }
}

export default RegisterPage;
