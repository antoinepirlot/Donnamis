import {getOffers} from "../../utils/BackEndRequests";

const tableHtml = `
  <div>
    <h1 class="display-3">All items</h1>
    <div class="row" id="all_items">
    </div>
  </div>
`;

const AllOfferedItemsPage = async () => {
  const pageDiv = document.querySelector("#page");

  pageDiv.innerHTML = tableHtml;
  const offers = await getOffers();
  showItems(offers)
};

function showItems(offers) {
  let tbody = document.querySelector("#all_items");
  console.table(offers);
  offers.forEach((offer) => {

    tbody.innerHTML += `
    <div class="col-sm-3" id="item-card" >
      <div class="card">
      <img class="card-img-top" alt="Card image cap">
        <div class="card-body">
          <h5 class="card-title">${offer.item.title}</h5>
          <p class="card-text">${offer.item.itemDescription}</p>
          <a type="button" href="/offer?id=${offer.id}" class="btn btn-primary" > Voir les détails</a>
        </div>
      </div>
    </div>
    `;
  });

}


export default AllOfferedItemsPage;
