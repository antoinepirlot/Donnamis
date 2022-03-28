import {getItems, getOffers} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import {setLocalObject} from "../../utils/session";

const tableHtml = `
  <div>
    <h1 class="display-3">All items</h1>
    <div class="row" id="all_items">
    </div>
  </div>
`;

const AllOfferedItemsPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const item = await getItems();
  showItems(item)
};

function showItems(item) {
  let tbody = document.querySelector("#all_items");
  item.forEach((item) => {
    tbody.innerHTML += `
    <div class="col-sm-3" id="item-card" >
      <div class="card">
      <img class="card-img-top" alt="Card image cap">
        <div class="card-body">
          <h5 class="card-title">${item.title}</h5>
          <p class="card-text">${item.itemDescription}</p>
          <div id="seeItemButton">
            <a href="/item?id=${item.id}" type="button" class="btn btn-primary"> Voir les détails</a>
            <!--<input id="itemIdButton" type="hidden" value="">-->
          </div>
        </div>
      </div>
    </div>
    `;
  });
  //const itemButtons = document.querySelectorAll("#seeItemButton");
  //setSeeItemEvent(itemButtons);
}
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
