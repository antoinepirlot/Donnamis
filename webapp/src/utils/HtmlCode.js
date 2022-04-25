/**
 * Insert html code into the webpage.
 * @param items the list of item to show
 */
import {getPayload} from "./session";
import {Redirect} from "../Components/Router/Router";
import {openModal} from "./Modals";

function getShowItemsHtml(items) {
  let html = "";
  items.forEach((item) => {
    html += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
        <img class="card-img-top" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">${item.title}</h5>
            <p class="card-text">${item.itemDescription}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
            </div>
          </div>
        </div>
      </div>
    `;
  });
  return html;
}

function getShowItemsGivenAndAssignedHtml(items) {
  let divAssigned = "";
  let divGiven = "";
  items.forEach((item) => {

    if (item.offerStatus === "assigned") {
      divAssigned += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
        <img class="card-img-top" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">${item.title}</h5>
            <p class="card-text">${item.itemDescription}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
            </div>
          </div>
        </div>
      </div>
    `;
    } else if (item.offerStatus == "given") {
      divGiven += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
        <img class="card-img-top" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">${item.title}</h5>
            <p class="card-text">${item.itemDescription}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir les détails</a>
            </div>
            <br>
            <div id="ratingButtons">
              <input value="Evaluer" type="submit" class="btn btn-primary">
            </div>
          </div>
        </div>
      </div>
    `;
    }
  });
  let html = `
   <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Mes objets attribués</h1>
      <h5 class="text-secondary">Voici vos objets attribués en attente de votre récupération</h5>
    </div>
    ` + divAssigned + `
    <div id="all_latest_items_title">
      <h1 class="display-3">Mes objets reçus</h1>
      <h5 class="text-secondary">Voici vos objets reçus, vous pouvez les évaluers</h5>
    </div>
    ` + divGiven + `
  </div>
  `;
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
      <option value="${itemsType.itemType}">
    `;
  });
}

export {
  getShowItemsHtml,
  checkIfMemberLoggedIn,
  showItemsTypes,
  getShowItemsGivenAndAssignedHtml,
}