import {cancelOffer, getItems, getMyOffers} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";
import {getPayload} from "../../utils/session";

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
`;

const MyOffersPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const payload = await getPayload();
  showItems(await getMyOffers(payload.id))
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
    OfferCancelledButton.addEventListener("click", cancelOffer);
  });
}

async function cancelOffer(e) {
  e.preventDefault();
  try {
    await cancelOffer(item.id);
    showItems(await getItems());
  } catch (error) {
    console.error("MyOffersPage::error::deny registration:", error);
  }
}

export default MyOffersPage;