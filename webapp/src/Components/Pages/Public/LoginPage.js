import {setLocalObject, setSessionObject} from "../../../utils/session";
import {
  getRefusal,
  login as loginBackEndRequest
} from "../../../utils/BackEndRequests";
import {Redirect} from "../../Router/Router";
import Navbar from "../../Navbar/Navbar";
import {showError} from "../../../utils/ShowError";

const loginFormHtml = `
<h1 class="display-3" id="login_title">Se connecter</h1>
<div class="form">
  <form  id="loginForm" class="shadow-lg p-3 mb-5 bg-white rounded">
    <div class="mb-3">
      <label class="form-label">Pseudo</label>
      <input type="text" class="form-control" id="usernameInput">
    </div>
    <div class="mb-3">
      <label class="form-label">Mot de passe</label>
      <input type="password" class="form-control" id="passwordInput">
      <input type="checkbox" id="checkPassword"> Voir mot de passe
    </div>
    <div class="mb-3 form-check">
      <input type="checkbox" class="form-check-input" id="rememberMeInput">
      <label class="form-check-label" >Se souvenir de moi</label>
    </div>
    <button type="submit" class="btn btn-primary">Se connecter</button>
    <div class="message" id="loginMessage"></div>
  </form>
</div>
`;

/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router
 * to "redirect" to a new page
 */
function LoginPage() {
  const page = document.querySelector("#page");
  page.innerHTML = loginFormHtml;
  page.addEventListener("submit", login);
  const checkPassword = document.getElementById("checkPassword")
  checkPassword.addEventListener("click", seePassword);
}

async function login(e) {
  e.preventDefault();
  const loginMessage = document.querySelector("#loginMessage");
  showError("Connexion en cours...", "info", loginMessage);
  const username = document.querySelector("#usernameInput").value;
  const password = document.querySelector("#passwordInput").value;
  const rememberMe = document.querySelector("#rememberMeInput").checked;
  if (username === "" ||
      password === "") {
    showError("Tous les champs doivent être complet", "danger", loginMessage)
    return;
  }
  try {
    const content = await loginBackEndRequest(username, password);
    if (!content) {

      const refusal = await getRefusal(username);
      if (refusal) {
        const refusalMessage = refusal.text;
        showError(refusalMessage, "danger", loginMessage);
      } else {
        showError("Aucun utilisateur pour ce username et ce mot de passe",
            "danger", loginMessage);
      }
      return;
    } else {
      if (rememberMe) {
        setLocalObject("token", content.token);
        setLocalObject("memberDTO", content.memberDTO);
      } else {
        setSessionObject("token", content.token);
        setSessionObject("memberDTO", content.memberDTO);
      }
    }
    Redirect("/");
    await Navbar();
  } catch (error) {
    console.error("LoginPage::error: ", error);
    showError("Une erreur est survenue. Vérifiez votre connexion internet.",
        "danger", loginMessage);
  }
}

// to see the input password
function seePassword() {
  let x = document.getElementById("passwordInput");
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}

export default LoginPage;
