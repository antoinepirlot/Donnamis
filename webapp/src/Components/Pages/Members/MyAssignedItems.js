import {getAssignedItems} from "../../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

const html = `
  <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Mes objets attribués</h1>
      <h5 class="text-secondary">Voici vos objets attribués en attente de votre récupération</h5>
    </div>
    <div class="row" id="assigned_items">
    </div>
  </div>
`;

const MyAssignedItems = async () => {
  if (!getPayload()) {
    Redirect("/login");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = html;
  const items = await getAssignedItems();
  const tbody = document.querySelector("#assigned_items");
  tbody.innerHTML = getShowItemsHtml(items);
}

export {MyAssignedItems};