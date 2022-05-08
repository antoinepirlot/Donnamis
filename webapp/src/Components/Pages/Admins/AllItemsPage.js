import {getAllItems} from "../../../utils/BackEndRequests";
import {Redirect} from "../../Router/Router";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {isAdmin} from "../../../utils/session";

const tableHtml = `
  <div>
    <h1 class="display-3" id="all_items_title">Tous les objets</h1>
    <div class="">
      <input class="form-control me-2 w-75 d-inline-block" id="searchInput" type="search" placeholder="Rechercher un objet" aria-label="Rechercher"> 
    </div>
    <div class="row" id="all_items">
    </div>
  </div>
`;

const AllItemsPage = async () => {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItems();
  let tbody = document.querySelector("#all_items");
  tbody.innerHTML = getShowItemsHtml(items);

  //Searching an item
  //Listener pour chaque frappe au clavier
  const searchInput = document.getElementById('searchInput');
  searchInput.addEventListener('keyup', function () {
    //'he' is a library to decode HTML element from a string
    let he = require('he');

    //Empty the table
    tbody.innerHTML = "";

    const input = searchInput.value.toLowerCase().trim();

    const result = items.filter(
        item => he.decode(item.title).toLowerCase().includes(input)
            || he.decode(item.itemDescription).toLowerCase().includes(input)
    )

    if (result.length < 1) {
      tbody.innerHTML = `<h1 class="display-6" id="SearchErrorMessage">Il n'y a aucun résultat pour cette recherche</h1>`;
    } else {
      //showFilterMembers(result)
      tbody.innerHTML = getShowItemsHtml(result);
    }
  })
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
