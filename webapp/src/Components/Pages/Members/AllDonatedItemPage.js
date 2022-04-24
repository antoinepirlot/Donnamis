import {getAllItemsByOfferStatus} from "../../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

const tableHtml = `
  <div>
    <h1 class="display-3" id="all_offered_items_title">Tous les objets offerts</h1>
    <div class="row" id="all_offered_items">
    </div>
  </div>
`;

const AllOfferedItemsPage = async () => {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus("donated");
  let tbody = document.querySelector("#all_offered_items");
  tbody.innerHTML = getShowItemsHtml(items);
};

//function setSeeItemEvent(itemButtons) {
//  itemButtons.forEach(itemButton => {
//    itemButton.addEventListener("click", (e) => {
//      e.preventDefault();
//      const id = itemButton.querySelector("#itemIdButton").value;
//      //setLocalObject("idItem", id);
//      Redirect(`/item?id=${id}`);
//    });
//  })
//}
export default AllOfferedItemsPage;
