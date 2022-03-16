import {
  getPayload,
} from "../../utils/session";
import {Redirect} from "../Router/Router";
import {showError} from "../../utils/ShowError";

const registerFormHtml = `

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
    
    <div id="registerMessage"></div>
  </form>
</div>
`;

/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router
 * to "redirect" to a new page
 */
async function RegisterPage() {
  if (await getPayload()) {
    Redirect("/");
    return;
  }
  const page = document.querySelector("#page");
  page.innerHTML = registerFormHtml;
  page.addEventListener("submit", register);

}

async function register(e) {
  e.preventDefault();
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
  //div
  const registerMessage =  document.querySelector("#registerMessage");

  if(
    !username|| !password || !firstName || !lastName || !street || !commune ||
    !buildingNumber || !postcode
  ){
    showError("Tous les champs doivent être complet", "danger", registerMessage)
    return;
  }
  const address = {
    street: street,
    buildingNumber: buildingNumber,
    commune: commune,
    postcode: postcode
  };
  if(unitNumber) {
    // add the unitNumber to address if it exists
    address.unitNumber = unitNumber;
  }
  try {
    const request = {
      method: "POST",
      headers: {
        "Content-Type":
            "application/json"
      },
      body: JSON.stringify({
        username: username,
        password: password,
        firstName: firstName,
        lastName: lastName,
        address: address
      })
    };
    const response = await fetch("api/members/register", request);
    console.log(response)
    if (!response.ok) {
      throw new Error("Problème lors du fetch");
    }
    showError("Votre inscription à bien été prise en compte. Veuillez patienter la validation de votre compte.", "success", registerMessage)
  } catch (err) {
    showError("Echec de l'inscription", "danger", registerMessage);
    console.error(err);
  }
}

export default RegisterPage;
