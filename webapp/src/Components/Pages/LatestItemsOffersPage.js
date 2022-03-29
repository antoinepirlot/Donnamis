import {getLatestItems} from "../../utils/BackEndRequests";
import {Redirect} from "../Router/Router";

const tableHtml = `
  <div>
    <h1 class="display-3">Bienvenue sur Donnamis</h1>
    <h5 class="text-secondary">Voici les derniers objets mis en ligne</h5>
    <br />
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Titre</th>
          <th scope="col">Description de l'objet</th>
          <th scope="col">Photo</th>
          <th scope="col">Statut de l'offre</th>
          <th scope="col">Disponibilités</th>
          <th scope="col"></th>
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
  await showItems(await getLatestItems())
};

async function showItems(items) {
  const tbody = document.querySelector("#tbody_all_items");
  tbody.innerHTML = "";
  items.forEach((item) => {
    //const offer = item.offerList[0];
    tbody.innerHTML += `
      <tr>
        <td>${item.title}</td>
        <td>${item.itemDescription}</td>
        <td>${item.photo}</td>
        <td>${item.offerStatus}</td>
        
        <td><button type="submit" class="btn btn-primary" id="ItemDetails">Voir offre</button></td>
      </tr>    
    `;
    // <td>${offer.timeSlot}</td>
    const OfferDetailsButton = document.querySelector("#ItemDetails");
    OfferDetailsButton.addEventListener("click", function () {
      Redirect(`/offer?id=${offer.id}`);
    })
  });
}

export default LatestItemsOffersPage;