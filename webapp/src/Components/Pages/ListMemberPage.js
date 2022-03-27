/**
 * Render the ListMemberPage
 */
import {
  confirmAdmin,
  confirmMember,
  denyMember,
  getDeniedMembers,
  getRegisteredMembers
} from "../../utils/BackEndRequests";

const tableHtmlConfirmedMembers = `
  <div>
    <h1 class="display-6">Membres en attente d'acceptation</h1>
    <br>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Nom</th>
          <th scope="col">Prénom</th>
          <th scope="col">Administrateur</th>
          <th scope="col">Confirmation</th>
          <th scope="col">Refus</th>
        </tr>
      </thead>
      <tbody id="tbody_registered_members">
      </tbody>
    </table>
  </div>
  <br>
  <br>
`;

const tableHtmlDeniedMembers = `
  <div>
    <h1 class="display-6">Membres refusés</h1>
    <br>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Nom</th>
          <th scope="col">Prénom</th>
          <th scope="col">Administrateur</th>
          <th scope="col">Confirmation</th>
        </tr>
      </thead>
      <tbody id="tbody_denied_members">
      </tbody>
    </table>
  </div>
`;

const ListMemberPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtmlConfirmedMembers;
  pageDiv.innerHTML += tableHtmlDeniedMembers;
  viewRegisteredMembers(await getRegisteredMembers());
  viewDeniedMembers(await getDeniedMembers());
};

async function viewRegisteredMembers(members) {
  const tbody = document.querySelector("#tbody_registered_members");

  //For Each Member
  members.forEach((member) => {
    tbody.innerHTML += `
      <tr">
        <td>${member.firstName}</td>
        <td>${member.lastName}</td>
        <td><input class="form-check-input" type="checkbox" value="" id="RegisteredIsAdmin"></td>
        <td><button type="submit" class="btn btn-primary" id="RegisteredConfirmButton">Confirmer</button></td>
        <td><button type="submit" class="btn btn-danger" id="RegisteredRefuseButton">Refuser</button></td>
      </tr>    
    `;

    // Is Admin Button
    const isAdminButton = document.querySelector("#RegisteredIsAdmin");

    // Confirm Button
    const confirmButton = document.querySelector("#RegisteredConfirmButton");
    confirmButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      if (isAdminButton.checked) {
        await confirmAdmin(member.id);
      } else {
        await confirmMember(member.id);
      }
      location.reload();
    });

    //Deny Button
    const denyButton = document.querySelector("#RegisteredRefuseButton")
    denyButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      await denyMember(member.id);
      location.reload();
    });

    //line.dataset.memberId = member.id;
  });
}

async function viewDeniedMembers(members) {
  const tbody = document.querySelector("#tbody_denied_members");

  //For Each Member
  members.forEach((member) => {
    tbody.innerHTML += `
      <tr>
        <td>${member.firstName}</td>
        <td>${member.lastName}</td>
        <td><input class="form-check-input" type="checkbox" value="" id="DeniedIsAdmin"></td>
        <td><button type="submit" class="btn btn-primary" id="DeniedConfirmButton">Confirmer</button></td>
      </tr>    
    `;

    // Is Admin Button
    const isAdminButton = document.querySelector("#DeniedIsAdmin");

    // Confirm Button
    const confirmButton = document.querySelector("#DeniedConfirmButton");
    confirmButton.addEventListener("click", async function () {

      //Confirm the registration (Click on the button)
      if (isAdminButton.checked) {
        await confirmAdmin(member.id);
      } else {
        await confirmMember(member.id);
      }
      location.reload();
    });

    //line.dataset.memberId = member.id;
  });
}

export default ListMemberPage;