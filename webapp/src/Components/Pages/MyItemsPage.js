import {
  cancelOffer as cancelOfferBackEnd,
  getItems,
  getMyItems
} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import {getPayload} from "../../utils/session";
import {showError} from "../../utils/ShowError";

const tableHtml = `
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
`;

const MyItemsPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const payload = await getPayload();
  const items = await getMyItems(payload.id);
  if (items.length === 0) {
    const message = "Vous n'avez aucune offre.";
    const errorMessageMyItemsPage = document.querySelector("#errorMessageMyItemsPage");
    showError(message, "info", errorMessageMyItemsPage);
  } else {
    showItems(items)
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
        <td><button type="submit" class="btn btn-primary" id="ItemDetails">Voir offre</button></td>
        <td><button type="submit" class="btn btn-danger" id="ItemCancelled">Annuler l'offre</button></td>
      </tr>    
    `;
    const OfferDetailsButton = document.querySelector("#ItemDetails");
    OfferDetailsButton.addEventListener("click", function () {
      Redirect(`/offer?id=${item.id}`);
    });

    const OfferCancelledButton = document.querySelector("#ItemCancelled");
    OfferCancelledButton.addEventListener("click", async (e) => {
      e.preventDefault();
      try {
        await cancelOfferBackEnd(item.id);
        Redirect("/my_items");
      } catch (error) {
        console.error("MyItemsPage::error::deny registration:", error);
      }
    });
  });
}

export default MyItemsPage;