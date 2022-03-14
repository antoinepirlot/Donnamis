import {
  getPayload,
} from "../../utils/session";
import {Redirect} from "../Router/Router";

const registerFormHtml = `
  <div>
    <form id="RegisterForm">
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
      <input type="submit">
    </form>
    <div id="registerFormNotification"></div>
  </div>
  <div id="registerError"></div>
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
  const username = document.querySelector("#usernameInput").value;
  const password = document.querySelector("#passwordInput").value;
  const firstName = document.querySelector("#firstNameInput").value;
  const lastName = document.querySelector("#lastNameInput").value;
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
        lastName: lastName
      })
    };
    const response = await fetch("api/members/register", request);
    if (!response.ok) {
      throw new Error("Problème lors du fetch");
    }
    await response.json();
    showError("Votre inscription à bien été prise en compte. Veuillez patienter la validation de votre compte.", "success")
  } catch (err) {
    showError("Echec de l'inscription", "danger");
    console.error(err);
  }
}

function showError(message, alertType){
  const regsiterError =  document.querySelector("#registerError");
  regsiterError.innerHTML += `
    <p class="alert-${alertType}">${message}</p>
  `;
}

export default RegisterPage;
