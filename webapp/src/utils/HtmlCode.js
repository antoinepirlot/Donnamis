/**
 * Insert html code into the webpage.
 * @param items the list of item to show
 */
import {getObject, getPayload} from "./session";
import {Redirect} from "../Components/Router/Router";
import {openModal} from "./Modals";

let he = require('he');

function getShowItemsHtml(items) {
  let html = `<div id="all-item-cards">`
  for (const item of items) {
    html += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
          ${displayImage(item, false)}
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

function getProfileFormHtml() {
  const member = getObject("memberDTO");
  const address = member.address;

  return `
      <div id="left" class="mr-auto p-2 bd-highlight" xmlns="http://www.w3.org/1999/html">
        <h3>Mes infos</h3>
        <div class="mb-3">
          <label class="form-label">Nom<span id="asterisk">*</span></label>
          <input id="nameForm" class="form-control" type="text" value="${member.lastName}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Prénom<span id="asterisk">*</span></label>
          <input id="firstnameForm" class="form-control" type="text" value="${member.firstName}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Pseudo<span id="asterisk">*</span></label>
          <input id="usernameForm" class="form-control" type="text" value="${member.username}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Mot de passe<span id="asterisk">*</span></label>
          <input id="passwordForm" class="form-control" type="password" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Confirmez le mot de passe<span id="asterisk">*</span></label>
          <input id="passwordConfirmationForm" class="form-control" type = "password" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Téléphone<span id="asterisk">*</span></label>
          <input id="phoneForm" class="form-control" type="tel" value="${member.phoneNumber
      ? member.phoneNumber : ""}">
        </div>
      </div>
      <div id="right" class="p-2 bd-highlight">
        <h3>Adresse</h3>
        <div class="mb-3">
          <label class="form-label">Rue<span id="asterisk">*</span></label>
          <input id="streetFormProfilePage" class="form-control" type="text" value="${address.street}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Numéro<span id="asterisk">*</span></label>
          <input id="buildingNumberFormProfilePage" class="form-control" type="text" value="${address.buildingNumber}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Numéro de boîte</label>
          <input id="unitNumberFormProfilePage" class="form-control" type="text" value="${address.unitNumber
      ? address.unitNumber : ""}">
        </div>
        <div class="mb-3">
          <label class="form-label">Code postal<span id="asterisk">*</span></label>
          <input id="postCodeFormProfilePage" class="form-control" type="text" value="${address.postcode}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Commune<span id="asterisk">*</span></label>
          <input id="communeFormProfilePage" class="form-control" type="text" value="${address.commune}" required>
        </div>
        <input class="btn btn-primary align-bottomg" type="submit" value="Modifier">
        <p id="asterisk">* Champs obligatoires</p>
      </div>
  `;
}

/*
* Displays the image of the item in param.
*
* @param item of the image
* @param zoomed true if we want to see the image details
*/
function displayImage(item, zoomed) {
  if (item.photo) {
    return `<img src="/api/images/${item.photo}" id=${zoomed ? "bigItemImage"
        : "smallItemImage"}
                class="card-img-top" alt="Card image cap">`;
  }
  return `<img src="/api/images/no-images.png" id=${zoomed ? "bigItemImage"
      : "smallItemImage"}
            class="card-img-top" alt="Card image cap">`;
}

function getAssignedItemHtml(item) {
  return `
    <div class="col-sm-3" id="item-card" >
      <div class="card">
        ${displayImage(item, false)}
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
        ${displayImage(item, false)}
        <div class="card-body">
          <h5 class="card-title">${he.decode(item.title)}</h5>
          <p class="card-text">${he.decode(item.itemDescription)}</p>
          <div id="itemButtons">
            <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
          </div>
          <br>
  `;
  const loggedMemberId = getPayload().id;
  if (loggedMemberId !== item.member.id
      && (!ratings
          || !ratings.find((rating) => rating.item.id === item.id))
  ) {
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
          ${displayImage(item, false)}
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
  getMyItemsHtml,
  getProfileFormHtml,
  displayImage
}