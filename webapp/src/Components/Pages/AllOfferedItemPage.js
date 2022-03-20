const tableHtml = `
  <div>
    <table>
      <thead>
        <tr>
          <th>Titre</th>
          <th>Description de l'objet</th>
          <th>photo</th>
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

async function getItems() {
  const request = {
    method: "GET"
  };
  const response = await fetch("/api/items/all_offered_items", request);
  if(!response.ok){
    throw new Error("Erreur lors du fetch");
  }
  return await response.json();
}

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