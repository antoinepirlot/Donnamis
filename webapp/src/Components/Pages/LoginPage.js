import HomePage from "./HomePage";
import { Redirect } from "../Router/Router";

const loginFormHtml = `
  <div>
    <form id="loginForm">
      Pseudo: <input id="usernameInput" type="text" placeholder="Ecris ton pseudo ici"><br>
      <br>
      Mot de passe: <input id="passwordInput" type="password" placeholder="Mot de passe"><br>
      <br>
      <input id="rememberMeInput" type="checkbox"> Se souvenir de moi<br>
      <br>
      <input type="submit">
    </form>
    <div id="loginFormNotification"></div>
  </div>
`;


/**
 * Render the LoginPage :
 * Just an example to demonstrate how to use the router to "redirect" to a new page
 */
function LoginPage() {
  const page = document.querySelector("#page");
  page.innerHTML = loginFormHtml;
  page.addEventListener("submit", login);

}

async function login(e) {
  e.preventDefault();
  const username = document.querySelector("#usernameInput").value;
  const password = document.querySelector("#passwordInput").value;
  const rememberMe = document.querySelector("#rememberMeInput").checked;

  try {
    const request = {
      method: "POST",
      headers: {
        "Content-Type":
            "application/json"
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    };
    const response = await fetch("api/members/login", request);
    if(!response.ok)
      throw new Error("Problème lors du fetch");
    const notification = document.querySelector("#loginFormNotification");
    notification.innerHTML = response.body.username;
  } catch (err) {
    console.error(err);
  }
}

export default LoginPage;
