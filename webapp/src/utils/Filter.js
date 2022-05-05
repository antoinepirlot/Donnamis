import {getMyItemsHtml, getShowItemsHtml} from "./HtmlCode";
import {showError} from "./ShowError";

function filterItemsByDate(itemsId, errorId, items) {
  let startDate = document.querySelector("#formStartDateMyItemsPage").value;
  let endDate = document.querySelector("#formEndDateMyItemsPage").value;
  if (startDate === "" || endDate === "") {
    return;
  }
  startDate = new Date(startDate);
  endDate = new Date(endDate);
  const itemsDiv = document.querySelector(itemsId);
  const filteredItems = items.filter(item =>
      new Date(item.lastOffer.date).getDate() >= startDate.getDate()
      && new Date(item.lastOffer.date).getDate() <= endDate.getDate()
      && new Date(item.lastOffer.date).getMonth() >= startDate.getMonth()
      && new Date(item.lastOffer.date).getMonth() <= endDate.getMonth()
      && new Date(item.lastOffer.date).getFullYear() >= startDate.getFullYear()
      && new Date(item.lastOffer.date).getFullYear() <= endDate.getFullYear()
  );
  if (!filteredItems) {
    const errorDiv = document.querySelector(errorId);
    showError("Aucun objet pour ces dates.", "info", errorDiv);
  }
  if (itemsId === "#myItems") {
    itemsDiv.innerHTML = getMyItemsHtml(filteredItems);
  } else {
    itemsDiv.innerHTML = getShowItemsHtml(filteredItems);
  }
}

export {filterItemsByDate}