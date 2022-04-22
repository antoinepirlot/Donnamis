/**
 * Insert html code into the webpage.
 * @param items the list of item to show
 */
function getShowItemsHtml(items) {
  let html = "";
  items.forEach((item) => {
    html += `
      <div class="col-sm-3" id="item-card" >
        <div class="card">
        <img class="card-img-top" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">${item.title}</h5>
            <p class="card-text">${item.itemDescription}</p>
            <div id="itemButtons">
              <a href="/item?id=${item.id}" type="button" class="btn btn-primary"> Voir les détails</a>
            </div>
          </div>
        </div>
      </div>
    `;
  });
  return html;
}

export {
  getShowItemsHtml,
}