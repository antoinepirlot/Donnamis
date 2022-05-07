import {
  register as registerBackEndRequest
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";

const registerFormHtml = `
<h1 class="display-3" id="login_title">S'inscrire</h1>
<div class="form">
  
  <form  id="registerForm" class="d-flex bd-highlight mb-3 shadow-lg p-3 mb-5 bg-white rounded">
  
    <div id="left" class="mr-auto p-2 bd-highlight">
      <h3>Mes infos</h3>
      <div class="mb-3">
        <label class="form-label">Prénom<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="firstNameInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Nom<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="lastNameInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Pseudo<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="usernameInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Mot de passe<span id="asterisk">*</span></label>
        <input type="password" class="form-control" id="passwordInput">
      </div>
    </div>
    
    <div id="right" class="p-2 bd-highlight">
      <h3>Adresse</h3>
      
      <div class="mb-3">
        <label class="form-label">Rue<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="streetInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Numero<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="buildingNumberInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Boîte</label>
        <input type="text" class="form-control" id="unitNumberInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Commune<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="communeInput">
      </div>
      
      <div class="mb-3">
        <label class="form-label">Code postal<span id="asterisk">*</span></label>
        <input type="text" class="form-control" id="postcodeInput">
      </div>
      <button type="submit" class="btn btn-primary">S'inscrire</button>
      <br>
      <span id="asterisk">* Champs obligatoires</span>
    </div>
  </form>
  <div class="message" id="registerMessage"></div>
</div>
<br>
`;

/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router
 * to "redirect" to a new page
 */
function RegisterPage() {
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
