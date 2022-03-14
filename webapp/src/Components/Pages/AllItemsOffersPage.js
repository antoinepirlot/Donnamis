/**
 * Render the AllItemsOffersPage
 */

const AllItemsOffersPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "All items offers";

  try {
    const response = await fetch("/api/items/all_items");
    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const items = await response.json();

    //Create Div and Table
    const tableWrapper = document.createElement("div");
    const table = document.createElement("table");
    tableWrapper.appendChild(table);

    //Create the header of the table
    const thead = document.createElement("thead");
    const header = document.createElement("tr");
    thead.appendChild(header);
    const header1 = document.createElement("th");
    header1.innerText = "Id_item";
    const header2 = document.createElement("th");
    header2.innerText = "Description";
    header.appendChild(header1);
    header.appendChild(header2);
    table.appendChild(thead);

    // Create the body of the table
    const tbody = document.createElement("tbody");
    items.forEach((item) => {
      const line = document.createElement("tr");
      const Id_ItemCell = document.createElement("td");
      Id_ItemCell.innerText = item.id;
      line.appendChild(Id_ItemCell);
      const Item_DescriptionCell = document.createElement("td");
      Item_DescriptionCell.innerText = item.item_description;
      line.appendChild(Item_DescriptionCell);
      line.dataset.itemId = item.id_item;
      tbody.appendChild(line);
    });
    table.appendChild(tbody);
    // add the HTMLTableElement to the main, within the #page div
    pageDiv.appendChild(tableWrapper);
  } catch (error) {
    console.error("LatestItemsOffersPage::error: ", error);
  }
};

export default AllItemsOffersPage;