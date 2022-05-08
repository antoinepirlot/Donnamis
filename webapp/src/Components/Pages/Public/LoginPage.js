import {setLocalObject, setSessionObject} from "../../../utils/session";
import {
  getRefusal,
  login as loginBackEndRequest,
  setMemberAvailability
} from "../../../utils/BackEndRequests";
import {Redirect} from "../../Router/Router";
import Navbar from "../../Navbar/Navbar";
import {showError} from "../../../utils/ShowError";
import {openModal} from "../../../utils/Modals";

const loginFormHtml = `
<h1 class="display-3" id="login_title">Se connecter</h1>
<div class="form">
  <form  id="loginForm" class="shadow-lg p-3 mb-5 bg-white rounded">
    <div class="mb-3">
      <label class="form-label">Pseudo<span id="asterisk">*</span></label>
      <input type="text" class="form-control" id="usernameInput">
    </div>
    <div class="mb-3">
      <label class="form-label">Mot de passe<span id="asterisk">*</span></label>
      <input type="password" class="form-control" id="passwordInput">
      <input type="checkbox" id="checkPassword"> Voir mot de passe
    </div>
    <div class="mb-3 form-check">
      <input type="checkbox" class="form-check-input" id="rememberMeInput">
      <label class="form-check-label" >Se souvenir de moi</label>
    </div>
    <button type="submit" class="btn btn-primary">Se connecter</button>
    <br>
    <span id="asterisk">* Champs obligatoires</span>
    <div class="message" id="loginMessage"></div>
  </form>
</div>
<!-- Modal Unavailable -->
<div id="unavailableModal" class="modal">
  <div class="modal-content">
    <span id="unavailableCloseModal" class="close">&times;</span>
    <form id="unavailableForm">
      <h5>Etes vous de nouveau disponible ?</h5><br>
      <input type="submit" class="btn btn-primary" value="Oui">
    </form>
  </div>
  <div id="unavailableError"></div>
</div>
`;

let content;
let rememberMe;

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
  rememberMe = document.querySelector("#rememberMeInput").checked;
  if (username === "" ||
      password === "") {
    showError("Tous les champs doivent être complet", "danger", loginMessage)
    return;
  }
  try {
    content = await loginBackEndRequest(username, password);
    if (!content) {
      const refusal = await getRefusal(username);
      if (refusal) {
        const refusalMessage = refusal.text;
        showError(refusalMessage, "danger", loginMessage);
      } else {
        showError("Aucun utilisateur pour ce username et ce mot de passe",
            "danger", loginMessage);
      }
    } else {
      if (content === 403) {
        showError(
            "Vous êtes inscrit mais devez patienter que un administrateur vous accepte.",
            "danger", loginMessage);
        return;
      }
      if (content.memberDTO.actualState === "unavailable") {
        await showUnavailableModal();
      } else if (rememberMe) {
        setLocalObject("token", content.token);
        setLocalObject("memberDTO", content.memberDTO);
        Redirect("/");
        await Navbar();
      } else {
        setSessionObject("token", content.token);
        setSessionObject("memberDTO", content.memberDTO);
        Redirect("/");
        await Navbar();
      }
    }
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

async function showUnavailableModal() {
  openModal("#unavailableModal", "#unavailableCloseModal");

  const unavailableForm = document.querySelector("#unavailableForm");
  unavailableForm.addEventListener("submit", loginUnavailable);
}

async function loginUnavailable(e) {
  e.preventDefault();

  const pageErrorDiv = document.querySelector("#unavailableError");
  const memberUnavailable = {
    id: content.memberDTO.id,
    actualState: "confirmed",
    version: content.memberDTO.version
  };

  try {
    await setMemberAvailability(memberUnavailable, pageErrorDiv);
  } catch (err) {
    console.error(err);
  }

  if (rememberMe) {
    setLocalObject("token", content.token);
    setLocalObject("memberDTO", content.memberDTO);
  } else {
    setSessionObject("token", content.token);
    setSessionObject("memberDTO", content.memberDTO);
  }
  Redirect("/");
  await Navbar();
}

export default LoginPage;
