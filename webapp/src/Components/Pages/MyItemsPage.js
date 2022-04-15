import {
  cancelOffer as cancelOfferBackEnd,
  getMyItems,
  offerAgain as offerAgainBackEnd
} from "../../utils/BackEndRequests";
import {getPayload} from "../../utils/session";
import {showError} from "../../utils/ShowError";
import {openModal} from "../../utils/Modals";
import {Redirect} from "../Router/Router";

const myItemsPageHtml = `
  <div>
    <h1 class="display-3">Mes offres</h1>
    <br>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Titre</th>
          <th scope="col">Description de l'objet</th>
          <th scope="col">Photo</th>
          <th scope="col">Statut de l'offre</th>
          <th scope="col"></th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody id="tbody_my_items">
      </tbody>
    </table>
  </div>
  <div id="errorMessageMyItemsPage"></div>
  
  <!-- The Modal -->
<div id="myItemsPageModal" class="modal">
  <div class="modal-content">
    <span id="myItemsPageModalCloseButton" class="close">&times;</span>
    <form id="offerAgainForm">
      <h5>Pour offrir un objet à nouveau, il faut que vous entrez une nouvelle plage horaire</h5><br>
      <br>
      Disponibilités horaire<span id="asterisk">*</span>:<br>
      <textarea id="timeSlotFormOfferAgain" cols="30" rows="3"></textarea><br>
      <br>
      <input type="submit" value="Envoyer">
    </form>
  </div>
</div>
`;

let offerAgainItemId;

const MyItemsPage = async () => {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = myItemsPageHtml;
  const items = await getMyItems();
  if (items.length === 0) {
    const message = "Vous n'avez aucune offre.";
    const errorMessageMyItemsPage = document.querySelector(
        "#errorMessageMyItemsPage");
    showError(message, "info", errorMessageMyItemsPage);
  } else {
    showItems(items);
  }
};

function showItems(items) {
  const tbody = document.querySelector("#tbody_my_items");
  tbody.innerHTML = "";
  items.forEach((item) => {
    tbody.innerHTML += `
      <tr>
        <td>${item.title}</td>
        <td>${item.itemDescription}</td>
        <td>${item.photo}</td>
        <td>${item.offerStatus}</td>
        <td><a id="itemDetails" href="/item?id=${item.id}" type="button" class="btn btn-primary">Voir offre</a></td>
        <td><button id="offerAgainButton" class="btn btn-primary" value="${item.id}">Offrir à nouveau</button></td>
        <td><button id="itemCancelled" class="btn btn-danger" value="${item.id}">Annuler l'offre</button></td>
      </tr>
    `;
  });
  const offerAgainButtons = document.querySelectorAll("#offerAgainButton");
  offerAgainButtons.forEach(async (offerAgainButton) => {
    offerAgainButton.addEventListener("click", async () => {
      offerAgainItemId = offerAgainButton.value;
      openModal("#myItemsPageModal", "#myItemsPageModalCloseButton");
      const offerAgainForm = document.querySelector("#offerAgainForm");
      offerAgainForm.addEventListener("submit", await offerAgain);
    });
  });

  const cancelButtons = document.querySelectorAll("#itemCancelled");
  cancelButtons.forEach(cancelButton => {
    cancelButton.addEventListener("click", async () => {
      await cancelOfferBackEnd(cancelButton.value);
      await MyItemsPage()
    });
  });
}

async function offerAgain(e) {
  e.preventDefault();
  const timeSlot = document.querySelector("#timeSlotFormOfferAgain").value;
  const offer = {
    idItem: offerAgainItemId,
    timeSlot: timeSlot
  }
  try {
    await offerAgainBackEnd(offer);
    await MyItemsPage();
  } catch (e) {
    console.error(e);
  }
}

export default MyItemsPage;