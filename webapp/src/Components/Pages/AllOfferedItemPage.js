import {getOfferedItems} from "../../utils/BackEndRequests";

const tableHtml = `
  <div>
    <h1 class="display-3" id="all_offered_items_title">Tous les objets offerts</h1>
    <div class="row" id="all_offered_items">
    </div>
  </div>
`;

const AllOfferedItemsPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const item = await getOfferedItems();
  showItems(item)
};

function showItems(item) {
  let tbody = document.querySelector("#all_offered_items");
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
export default AllOfferedItemsPage;
