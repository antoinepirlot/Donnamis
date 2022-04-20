/**
 * Insert html code into the webpage.
 * @param items the list of item to show
 * @param tbody the html part to show items
 */
function showItems(items, tbody) {
  console.log("hello util");
  tbody.innerHTML = "";
  items.forEach((item) => {
    tbody.innerHTML += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
        <img class="card-img-top" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">${item.title}</h5>
            <p class="card-text">${item.itemDescription}</p>
            <div id="seeItemButton">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary"> Voir les détails</a>
            </div>
          </div>
        </div>
      </div>
    `;
  });
}

export {
  showItems,
}