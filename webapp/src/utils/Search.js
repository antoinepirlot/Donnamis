import {getMyItemsHtml, getShowItemsHtml} from "./HtmlCode";

const searchBarHtml = `
  <input class="form-control me-2 w-75 d-inline-block" id="searchInput" type="search" placeholder="Rechercher un objet" aria-label="Rechercher"> 
`;

/**
 * Create the items' search bar and return the result.
 * @param items the list of items
 * @param searchBarId the search bar id start with "#"
 * @param contentId the emplacement where items are shown start with "#"
 * @param pageName the page's name to show items
 */
function createItemsSearchBar(items, searchBarId, contentId, pageName) {
  const searchBar = document.querySelector(searchBarId);
  searchBar.innerHTML = searchBarHtml;
  const searchInput = document.querySelector("#searchInput");
  const tbody = document.querySelector(contentId);
  searchInput.addEventListener("keyup", () => {
    //Empty the table
    tbody.innerHTML = "";
    //'he' is a library to decode HTML element from a string
    const input = searchInput.value.toLowerCase().trim();
    const he = require("he");
    const result = items.filter(
        item => he.decode(item.title).toLowerCase().includes(input)
            || he.decode(item.member.firstName).toLowerCase().includes(input)
            || he.decode(item.member.lastName).toLowerCase().includes(input)
            || he.decode(item.itemType.itemType).toLowerCase().includes(input)
            || he.decode(item.offerStatus).toLowerCase().includes(input)
            || he.decode(item.itemDescription).toLowerCase().includes(input)
    );
    if (result.length < 1) {
      tbody.innerHTML = `<h1 class="display-6" id="SearchErrorMessage">Il n'y a aucun résultat pour cette recherche</h1>`;
    } else {
      if (pageName === "homePage") {
        tbody.innerHTML = getShowItemsHtml(result);
      } else if (pageName === "myItemsPage") {
        tbody.innerHTML = getMyItemsHtml(result);
      }
    }
  });
}

export {createItemsSearchBar};