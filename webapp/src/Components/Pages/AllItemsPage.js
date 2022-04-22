import {getAllItemsByOfferStatus, isAdmin} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import {getShowItemsHtml} from "../../utils/HtmlCode";

const tableHtml = `
  <div>
    <h1 class="display-3" id="all_items_title">Tous les objets</h1>
    <div class="row" id="all_items">
    </div>
  </div>
`;

const AllItemsPage = async () => {
  if (!await isAdmin()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus();
  let tbody = document.querySelector("#all_items");
  tbody.innerHTML = getShowItemsHtml(items);
};

// function showItems(item) {
//
//
//   //const itemButtons = document.querySelectorAll("#seeItemButton");
//   //setSeeItemEvent(itemButtons);
// }

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
export default AllItemsPage;
