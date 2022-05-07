/**
 * Insert html code into the webpage.
 * @param items the list of item to show
 */
import {getPayload} from "./session";
import {Redirect} from "../Components/Router/Router";
import {openModal} from "./Modals";

let he = require('he');

function getShowItemsHtml(items) {
  let html = `<div id="all-item-cards">`
  for (const item of items) {
    html += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
          ${displayImage(item)}
          <div class="card-body">
            <h5 class="card-title">${he.decode(item.title)}</h5>
            <p class="card-text">${he.decode(item.itemDescription)}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
            </div>
          </div>
        </div>
      </div>
    `;
  }
  return html + "</div>";
}

function displayImage(item) {
  if (item.photo) {
    return `<img src="/api/images/${item.photo}" id="smallItemImage"
                class="card-img-top" alt="Card image cap">`;
  }
  return `<img src="/api/images/no-images.png" id="smallItemImage"
            class="card-img-top" alt="Card image cap">`;
}

function getAssignedItemHtml(item) {
  return `
    <div class="col-sm-3" id="item-card" >
      <div class="card">
        ${displayImage(item)}
        <div class="card-body">
          <h5 class="card-title">${he.decode(item.title)}</h5>
          <p class="card-text">${he.decode(item.itemDescription)}</p>
          <div id="itemButtons">
            <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
          </div>
        </div>
      </div>
    </div>
  `;
}

function getGivenItemHtml(item, ratings) {
  let html = `
    <div class="col-sm-3" id="item-card" >
      <div class="card">
        ${displayImage(item)}
        <div class="card-body">
          <h5 class="card-title">${he.decode(item.title)}</h5>
          <p class="card-text">${he.decode(item.itemDescription)}</p>
          <div id="itemButtons">
            <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
          </div>
          <br>
  `;
  if (!ratings || !ratings.find((rating) => rating.item.id === item.id)) {
    html += `<button id="ratingButton" type="submit" class="btn btn-primary" value="${item.id}">Evaluer</button>`;
  }
  html += `
        </div>
      </div>
    </div>
  `;
  return html;
}

function getMyItemsHtml(items) {
  let html = "";
  for (const item of items) {
    html += `
      <div class="col-sm-3 mb-3 d-flex align-items-stretch" id="item-card" >
        <div class="card">
          ${displayImage(item)}
          <div class="card-body">
            <h5 class="card-title">${he.decode(item.title)}</h5>
            <p class="card-text">${he.decode(item.itemDescription)}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>

    `;
    const cancelButtonHtml = `<td><button id="itemCancelled" class="btn btn-danger" value="${item.id}">Annuler l'offre</button></td>`;
    const offerAgainButtonHtml = `<td><button id="offerAgainButton" class="btn btn-primary" value="${item.id}">Offrir à nouveau</button></td>`;
    const markReceivedButtonHtml = `<td><button id="markReceivedButton" class="btn btn-primary" value="${item.id}">Objet donné</button></td>`;
    const chooseRecipientButtonHtml = `<td><button id="chooseRecipientButton" class="btn btn-primary" value="${item.id}">Choisir un receveur</button></td>`;
    const markNotGivenButtonHtml = `<td><button id="markNotGivenButton" class="btn btn-primary" value="${item.id}">Objet non récupéré</button></td>`;
    if (item.offerStatus === "donated") {
      // If no one is interested
      if (item.offerList[0].numberOfInterests === 0) {
        html += `
        ${offerAgainButtonHtml}
        ${cancelButtonHtml}
      `;
      } else {
        html += `
        ${offerAgainButtonHtml}
        ${chooseRecipientButtonHtml}
        ${cancelButtonHtml}
      `;
      }

    } else if (item.offerStatus === "cancelled") {
      html += `
        ${offerAgainButtonHtml}  
      `;
    } else if (item.offerStatus === "assigned") {
      html += `
        ${markReceivedButtonHtml}
        ${markNotGivenButtonHtml}
        ${cancelButtonHtml}
      `;
    }
    html += `
            </div>
          </div>
        </div>
      </div>
    `;
  }
  return html;
}

function checkIfMemberLoggedIn(modalId, closeModalId) {
  const itemButtons = document.querySelectorAll("#itemButtons");
  itemButtons.forEach(itemButton => {
    itemButton.addEventListener("click", (e) => {
      if (!getPayload()) {
        e.preventDefault();
        openModal(modalId, closeModalId);
        const iHaveAnAccountButton = document.querySelector(
            "#iHaveAnAccountButton");
        const iDontHaveAnAccountButton = document.querySelector(
            "#iDontHaveAnAccountButton");
        iHaveAnAccountButton.addEventListener("click", () => {
          Redirect("/login");
        });
        iDontHaveAnAccountButton.addEventListener("click", () => {
          Redirect("/register");
        })
      }
    });
  })
}

/*
function searchItem(items) {
  //Searching an item
  //Listener pour chaque frappe au clavier
  const searchInput = document.getElementById('searchInput');
  searchInput.addEventListener('keyup', function () {
    //Empty the table
    //tbody.innerHTML = "";

    const input = searchInput.value.toLowerCase();

    const result = items.filter(
        item => item.title.toLowerCase().includes(input)
            || item.itemDescription.toLowerCase().includes(input)
    )

    if (result.length < 1) {
      //tbody.innerHTML = `<h1 class="display-6" id="SearchErrorMessage">Il n'y a aucun résultat pour cette recherche</h1>`;
      const html = `<h1 class="display-6" id="SearchErrorMessage">Il n'y a aucun résultat pour cette recherche</h1>`;
      return html
    } else {
      //showFilterMembers(result)
      //tbody.innerHTML = getShowItemsHtml(result);
      const html = getShowItemsHtml(result);
      return html;
    }
  })
}*/

/**
 * Insert items types into the datalist.
 * @param datalistId the datalist to add items types
 * @param itemsTypes the items types to add into the datalist
 */
function showItemsTypes(datalistId, itemsTypes) {
  const itemsTypeList = document.querySelector(datalistId);
  itemsTypeList.innerHTML = "";
  itemsTypes.forEach(itemsType => {
    itemsTypeList.innerHTML += `
      <option value="${he.decode(itemsType.itemType)}">
    `;
  });
}

export {
  getShowItemsHtml,
  checkIfMemberLoggedIn,
  showItemsTypes,
  getAssignedItemHtml,
  getGivenItemHtml,
  getMyItemsHtml
}