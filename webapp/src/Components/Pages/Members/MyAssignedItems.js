import {getAssignedItems} from "../../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

const MyAssignedItems = async () => {
  if (!getPayload()) {
    Redirect("/login");
    return;
  }
  const pageDiv = document.querySelector("#page");
  const items = await getAssignedItems();
  pageDiv.innerHTML = getShowItemsHtml(items);
}

export default MyAssignedItems;