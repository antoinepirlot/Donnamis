import {getMyItemsHtml, getShowItemsHtml} from "./HtmlCode";

function filterItemsByDate(itemsId, items) {
  let startDate;
  let endDate;
  if (itemsId === "#myItems") {
    startDate = document.querySelector("#formStartDateMyItemsPage").value;
    endDate = document.querySelector("#formEndDateMyItemsPage").value;
  } else {
    startDate = document.querySelector("#formStartDateHomePage").value;
    endDate = document.querySelector("#formEndDateHomePage").value;
  }
  if (startDate === "" || endDate === "") {
    return;
  }
  startDate = new Date(startDate);
  startDate.setSeconds(59);
  startDate.setMinutes(59);
  startDate.setHours(23);

  endDate = new Date(endDate);
  endDate.setSeconds(59);
  endDate.setMinutes(59);
  endDate.setHours(23);

  const itemsDiv = document.querySelector(itemsId);
  const filteredItems = items.filter(item =>
      new Date(item.lastOffer.date) >= startDate
      && new Date(item.lastOffer.date) <= endDate
  );
  if (filteredItems.length === 0) {
    itemsDiv.innerHTML = `
      <h1 class="display-3">Aucun objets trouvé</h1>
      <h5 class="text-secondary">Aucun objet offert entre le ${startDate.toLocaleDateString()} et le ${endDate.toLocaleDateString()}.</h5>
    `;
    return;
  }
  if (itemsId === "#myItems") {
    itemsDiv.innerHTML = getMyItemsHtml(filteredItems);
  } else {
    itemsDiv.innerHTML = getShowItemsHtml(filteredItems);
  }
}

export {filterItemsByDate}