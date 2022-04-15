import {getAllItemsByOfferStatus} from "../../utils/BackEndRequests";

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
  showItems(items)
};

function showItems(item) {
  let tbody = document.querySelector("#all_latest_items");
  tbody.innerHTML = "";
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
export default LatestItemsOffersPage;
