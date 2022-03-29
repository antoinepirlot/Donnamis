import {
  getPayload,
  setLocalObject,
  setSessionObject
} from "../../utils/session";
import {login as loginBackEndRequest} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import Navbar from "../Navbar/Navbar";
import {showError} from "../../utils/ShowError";

const loginFormHtml = `
<h1 class="display-3" id="login_title">Se connecter</h1>
<div class="form">
  <form  id="loginForm">
    <div class="mb-3">
      <label class="form-label">Pseudo</label>
      <input type="text" class="form-control" id="usernameInput">
    </div>
    <div class="mb-3">
      <label class="form-label">Mot de passe</label>
      <input type="password" class="form-control" id="passwordInput">
    </div>
    <div class="mb-3 form-check">
      <input type="checkbox" class="form-check-input" id="rememberMeInput">
      <label class="form-check-label" >Se souvenir de moi</label>
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
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
  if (getPayload()) {
    Redirect("/");
    return;
  }
  const page = document.querySelector("#page");
  page.innerHTML = loginFormHtml;
  page.addEventListener("submit", login);

}

async function login(e) {
  e.preventDefault();
  const username = document.querySelector("#usernameInput").value;
  const password = document.querySelector("#passwordInput").value;
  const rememberMe = document.querySelector("#rememberMeInput").checked;
  const loginMessage = document.querySelector("#loginMessage");
  if (username === "" ||
      password === "") {
    showError("Tous les champs doivent être complet", "danger", loginMessage)
    return;
  }
  try {
    const content = await loginBackEndRequest(username, password);
    if (content == null) {
      return
    }
    if (rememberMe) {
      setLocalObject("token", content.token);
      setLocalObject("memberDTO", content.memberDTO);
    } else {
      setSessionObject("token", content.token);
      setSessionObject("memberDTO", content.memberDTO);
    }
    Redirect("/");
    Navbar();
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

export default LoginPage;
