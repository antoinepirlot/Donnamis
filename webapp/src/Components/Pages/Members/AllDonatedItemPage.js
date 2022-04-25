import {getAllItemsByOfferStatus} from "../../../utils/BackEndRequests";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

const tableHtml = `
  <div>
    <h1 class="display-3" id="all_offered_items_title">Tous les objets offerts</h1>
    <div class="">
      <input class="form-control me-2" id="searchInput" type="search" placeholder="Rechercher un objet" aria-label="Rechercher"> 
    </div> 
     
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
      //tbody.innerHTML = searchItem(items);

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
          tbody.innerHTML = getShowItemsHtml(result);
        }
      })

    }
;

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
