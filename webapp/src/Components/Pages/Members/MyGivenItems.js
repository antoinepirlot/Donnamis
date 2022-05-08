import {getGivenItems} from "../../../utils/BackEndRequests";
import {getGivenItemHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

let html = `
  <div>
    <div>
      <h1 class="display-3">Mes objets donnés</h1>
      <h5 class="text-secondary">Voici vos objets donnés. Merci pour votre don !</h5>
    </div>
    <div id="myGivenItems"></div>
  </div>
`;

const MyGivenItems = async () => {
  if (!getPayload()) {
    Redirect("/login");
    return;
  }

  const pageDiv = document.querySelector("#page");
  const items = await getGivenItems();
  if (!items) {
    pageDiv.innerHTML = `
      <h1 class="display-3">Aucun objets donnés</h1>
      <h5 class="text-secondary">Vous n'avez donné aucun objets pour le moment</h5>
    `;
    return;
  }
  pageDiv.innerHTML = html;

  //Show items
  const myGivenItems = document.querySelector("#myGivenItems");
  myGivenItems.innerHTML = "";
  items.forEach((item) => { //TODO
    myGivenItems.innerHTML += getGivenItemHtml(item);
  });
}

export default MyGivenItems;