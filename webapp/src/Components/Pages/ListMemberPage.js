/**
 * Render the ListMemberPage
 */
import {
  confirmAdmin, confirmMember, denyMember,
  getDeniedMembers,
  getRegisteredMembers
} from "../../utils/BackEndRequests";

const ListMemberPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";
  viewRegisteredMembers();
  viewDeniedMembers();
};

async function viewRegisteredMembers() {
  const pageDiv = document.querySelector("#page");

  //Membres en attente d'acceptation
  const members = await getRegisteredMembers();

  //Create Div and Table
  const tableWrapperRegistered = document.createElement("div");
  tableWrapperRegistered.className = "table-responsive pt-5";
  tableWrapperRegistered.innerHTML = "Membres en attente d'acceptation";
  const tableRegistered = document.createElement("table");
  tableRegistered.className = "table table";
  tableWrapperRegistered.appendChild(tableRegistered);

  //Create the header of the table
  const thead = document.createElement("thead");
  const header = document.createElement("tr");
  thead.appendChild(header);
  const header1 = document.createElement("th");
  header1.innerText = "Nom";
  const header2 = document.createElement("th");
  header2.innerText = "Prénom";
  const header3 = document.createElement("th");
  header3.innerText = "Administrateur";
  header.appendChild(header1);
  header.appendChild(header2);
  header.appendChild(header3);
  tableRegistered.appendChild(thead);

  // Create the body of the table
  const tbody = document.createElement("tbody");

  //For Each Member
  members.forEach((member) => {

    const line = document.createElement("tr");
    const NameCell = document.createElement("td");
    NameCell.innerText = member.lastName;
    line.appendChild(NameCell);
    const firstNameCell = document.createElement("td");
    firstNameCell.innerText = member.firstName;
    line.appendChild(firstNameCell);

    // Is Admin Button
    const isAdminButtonCell = document.createElement("td");
    const isAdminButton = document.createElement("input");
    isAdminButton.type = "checkbox";
    isAdminButtonCell.appendChild(isAdminButton);
    line.appendChild(isAdminButtonCell);

    // Confirm Button
    const confirmButtonCell = document.createElement("td");
    const confirmButton = document.createElement("input");
    confirmButton.type = "submit";
    confirmButton.value = "Confirmer";
    confirmButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      if (isAdminButton.checked) {
        await confirmAdmin();
      } else {
        await confirmMember();
      }
      location.reload();
    });
    confirmButtonCell.appendChild(confirmButton);
    line.appendChild(confirmButtonCell);

    //Deny Button
    const denyButtonCell = document.createElement("td");
    const denyButton = document.createElement("button");
    denyButton.innerHTML = "Refuser";
    denyButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      await denyMember();
      location.reload();
    });
    denyButtonCell.appendChild(denyButton);
    line.appendChild(denyButtonCell);

    line.dataset.memberId = member.id;

    tbody.appendChild(line);
  });
  tableRegistered.appendChild(tbody);
  // add the HTMLTableElement to the main, within the #page div

  pageDiv.appendChild(tableWrapperRegistered);
}

async function viewDeniedMembers() {
  const pageDiv = document.querySelector("#page");
  const members = await getDeniedMembers();

  //Create Div and Table
  const tableWrapper = document.createElement("div");
  tableWrapper.className = "table-responsive pt-5";
  tableWrapper.innerHTML = "Membres refusés";
  const table = document.createElement("table");
  table.className = "table table";
  tableWrapper.appendChild(table);

  //Create the header of the table
  const thead = document.createElement("thead");
  const header = document.createElement("tr");
  thead.appendChild(header);
  const header1 = document.createElement("th");
  header1.innerText = "Nom";
  const header2 = document.createElement("th");
  header2.innerText = "Prénom";
  const header3 = document.createElement("th");
  header3.innerText = "Administrateur";
  header.appendChild(header1);
  header.appendChild(header2);
  header.appendChild(header3);
  table.appendChild(thead);

  // Create the body of the table
  const tbody = document.createElement("tbody");
  members.forEach((member) => {
    const line = document.createElement("tr");
    const NameCell = document.createElement("td");
    NameCell.innerText = member.lastName;
    line.appendChild(NameCell);
    const firstNameCell = document.createElement("td");
    firstNameCell.innerText = member.firstName;
    line.appendChild(firstNameCell);

    // Is Admin Button
    const isAdminButtonCell = document.createElement("td");
    const isAdminButton = document.createElement("input");
    isAdminButton.type = "checkbox";
    isAdminButtonCell.appendChild(isAdminButton);
    line.appendChild(isAdminButtonCell);

    // Confirm Button
    const confirmButtonCell = document.createElement("td");
    const confirmButton = document.createElement("button");
    confirmButton.innerHTML = "Confirmer";
    confirmButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      if (isAdminButton.checked) {
        await confirmAdmin();
      } else {
        await confirmMember();
      }
      location.reload();
    });
    confirmButtonCell.appendChild(confirmButton);
    line.appendChild(confirmButtonCell);

    line.dataset.memberId = member.id;
    tbody.appendChild(line);
  });
  table.appendChild(tbody);
  // add the HTMLTableElement to the main, within the #page div
  pageDiv.appendChild(tableWrapper);

}

export default ListMemberPage;