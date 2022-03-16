import {
  getPayload,
  setLocalObject,
  setSessionObject
} from "../../utils/session";
import {Redirect} from "../Router/Router";
import Navbar from "../Navbar/Navbar";
import {showError} from "../../utils/ShowError";

const loginFormHtml = `
  <div>
    <form id="loginForm">
      Pseudo: <input id="usernameInput" type="text"
               placeholder="Ecris ton pseudo ici"><br>
      <br>
      Mot de passe: <input id="passwordInput" type="password"
                     placeholder="Mot de passe"><br>
      <br>
      <input id="rememberMeInput" type="checkbox"> Se souvenir de moi<br>
      <br>
      <input type="submit">
    </form>
    <div id="loginMessage"></div>
  </div>
`;

/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router
 * to "redirect" to a new page
 */
async function LoginPage() {
  if (await getPayload()) {
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
  if(username === "" ||
      password === ""){
    showError("Tous les champs doivent être complet", "danger", loginMessage)
    return;
  }
  try {
    const request = {
      method: "POST",
      headers: {
        "Content-Type":
            "application/json"
      },
      body: JSON.stringify( {
        username: username,
        password: password
      })
    };
    const response = await fetch("api/members/login", request);

    console.table(response)
    if (!response.ok) {
      showError("Aucun utilisateur pour ce username et ce mot de passe", "danger", loginMessage)
      return;
    }
    const content = await response.json();
    if (rememberMe) {
      setLocalObject("token", content.token);
      setLocalObject("memberDTO", content.memberDTO);
    } else {
      setSessionObject("token", content.token);
      setSessionObject("memberDTO", content.memberDTO);
    }
    Redirect("/");
    Navbar();
  } catch (err) {
    console.error(err);
  }
}

export default LoginPage;
