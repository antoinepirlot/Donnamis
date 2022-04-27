import {getAllItemsByOfferStatus} from "../../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";

const tableHtml = `
  <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Bienvenue sur Donnamis</h1>
      <h5 class="text-secondary">Voici les derniers objets mis en ligne</h5>
    </div>
     <div id="searchBarHomePage">
     </div> 
    <div class="row" id="all_offered_items">
    </div>
  </div>
`;

const searchBarHtml = `
  <input class="form-control me-2 w-75 d-inline-block" id="searchInput" type="search" placeholder="Rechercher un objet" aria-label="Rechercher"> 
`;

const HomePage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus("donated");
  let tbody = document.querySelector("#all_offered_items");
  tbody.innerHTML = getShowItemsHtml(items);

  //Searching an item
  //Listener pour chaque frappe au clavier
  if (getPayload()) {
    const searchBar = document.querySelector("#searchBarHomePage");
    searchBar.innerHTML = searchBarHtml;
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('keyup', function () {
      //'he' is a library to decode HTML element from a string
      let he = require('he');
      //Empty the table
      tbody.innerHTML = "";

      const input = searchInput.value.toLowerCase().trim();
      const result = items.filter(
          item => he.decode(item.member.firstName).toLowerCase().includes(input)
              || he.decode(item.member.lastName).toLowerCase().includes(input)
              || he.decode(item.itemType.itemType).toLowerCase().includes(input)
              || he.decode(item.offerStatus).toLowerCase().includes(input)
              || he.decode(item.itemDescription).toLowerCase().includes(input)
      );

      if (result.length < 1) {
        tbody.innerHTML = `<h1 class="display-6" id="SearchErrorMessage">Il n'y a aucun résultat pour cette recherche</h1>`;
      } else {
        tbody.innerHTML = getShowItemsHtml(result);
      }
    })
  }
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
export default HomePage;
