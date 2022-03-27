import {getItems} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";

const tableHtml = `
  <div>
    <h1 class="display-3">All offered items</h1>
    <br>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Titre</th>
          <th scope="col">Description de l'objet</th>
          <th scope="col">Photo</th>
          <th scope="col">Statut de l'offre</th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody id="tbody_all_offered_items">
      </tbody>
    </table>
  </div>
`;

const AllOfferedItemsPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  showItems(await getItems())
};

function showItems(items) {
  const tbody = document.querySelector("#tbody_all_offered_items");
  items.forEach((item) => {
    tbody.innerHTML += `
      <tr>
        <td>${item.title}</td>
        <td>${item.itemDescription}</td>
        <td>${item.photo}</td>
        <td>${item.offerStatus}</td>
        <td><button type="submit" class="btn btn-primary" id="ItemDetails">Voir offre</button></td>
      </tr>    
    `;
    const OfferDetailsButton = document.querySelector("#ItemDetails");
    OfferDetailsButton.addEventListener("click", function () {
      Redirect(`/offer?id=${offer.id}`);
    })
  });
}

export default AllOfferedItemsPage;