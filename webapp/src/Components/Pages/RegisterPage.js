import {
  getPayload,
} from "../../utils/session";
import {Redirect} from "../Router/Router";
import {showError} from "../../utils/ShowError";

const registerFormHtml = `
  <div>
    <form id="RegisterForm">
      <p>Mes infos</p>
      Pseudo: <input id="usernameInput" type="text"
               placeholder="Ecris ton pseudo ici"><br>
      <br>
      Mot de passe: <input id="passwordInput" type="password"
                     placeholder="Mot de passe"><br>
      <br>
      Prénom: <input id="firstNameInput" type="text"
                     placeholder="Prénom"><br>
      <br>
      Nom: <input id="lastNameInput" type="text"
                     placeholder="Nom"><br>
      <br>
      <p>Adresse</p>
      Rue: <input id="streetInput" type="text"
                  placeholder="Rue"><br>
      <br>
      Numero: <input id="buildingNumberInput" type="text"
                     placeholder="Numéro du batiment"><br>
      <br>
      Boîte (facultatif): <input id="unitNumberInput" type="text"
                                 placeholder="Boîte (peut être vide)"><br>
      <br>
      Commune: <input id="communeInput" type="text"
                      placeholder="Commune"><br>
      <br>
      Code postal: <input id="postcodeInput" type="text"
                          placeholder="Code postal"><br>
      <br>
      <input type="submit" value="S'inscrire">
    </form>
    <div id="registerFormNotification"></div>
  </div>
  <div id="registerMessage"></div>
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
