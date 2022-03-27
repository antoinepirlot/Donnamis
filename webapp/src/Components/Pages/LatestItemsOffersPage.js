import {getLatestOffers} from "../../utils/BackEndRequests";

const tableHtml = `
  <div>
    <h1 class="display-3">Latest Items Offer</h1>
    <br />
    <table>
      <thead>
        <tr>
          <th>Titre</th>
          <th>Description de l'objet</th>
          <th>Photo</th>
          <th>Statut de l'offre</th>
          <th>Disponibilités</th>
        </tr>
      </thead>
      <tbody id="tbody_all_items">
      </tbody>
    </table>
  </div>
`;

const LatestItemsOffersPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  showItems(await getLatestOffers())
};

function showItems(offers) {
  const tbody = document.querySelector("#tbody_all_items");
  offers.forEach((offer) => {
    tbody.innerHTML += `
      <tr>
        <td>${offer.item.title}</td>
        <td>${offer.item.itemDescription}</td>
        <td>${offer.item.photo}</td>
        <td>${offer.item.offerStatus}</td>
        <td>${offer.timeSlot}</td>
      </tr>    
    `;
  });
}

export default LatestItemsOffersPage;