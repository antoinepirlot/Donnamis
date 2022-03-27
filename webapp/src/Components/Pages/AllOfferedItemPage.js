import {getItems} from "../../utils/BackEndRequests";

const tableHtml = `
  <div>
    <h1 class="display-3">All offered items</h1>
    <br>
    <table>
      <thead>
        <tr>
          <th>Titre</th>
          <th>Description de l'objet</th>
          <th>Photo</th>
          <th>Statut de l'offre</th>
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
      </tr>    
    `;
  });
}

export default AllOfferedItemsPage;