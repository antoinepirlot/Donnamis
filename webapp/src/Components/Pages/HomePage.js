import {getAllItemsByOfferStatus} from "../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../utils/HtmlCode";

const tableHtml = `
  <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Bienvenue sur Donnamis</h1>
      <h5 class="text-secondary">Voici les derniers objets mis en ligne</h5>
    </div>
    <div class="row" id="all_latest_items">
    </div>
  </div>
`;

const LatestItemsOffersPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus("donated");
  let tbody = document.querySelector("#all_latest_items");
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
export default LatestItemsOffersPage;
