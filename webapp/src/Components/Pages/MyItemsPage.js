import {
  cancelOffer as cancelOfferBackEnd,
  getMyItems
} from "../../utils/BackEndRequests";
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
  const items = await getMyItems(getPayload().id);
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
        <td><button id="itemCancelled" class="btn btn-danger" value="${item.id}">Annuler l'offre</button></td>
      </tr>
    `;
  });
  const cancelButtons = document.querySelectorAll("#itemCancelled");
  cancelButtons.forEach(cancelButton => {
    cancelButton.addEventListener("click", async () => {
      await cancelOfferBackEnd(cancelButton.value);
      await MyItemsPage()
    });
  });
}

export default MyItemsPage;