/**
 * Render the ListMemberPage
 */
import {
  confirmInscription,
  denyMember,
  getAllMembers,
  getOneMember
} from "../../../utils/BackEndRequests";
import {Redirect} from "../../Router/Router";
import {isAdmin} from "../../../utils/session";

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
          <th scope="col"> Message du refus</th>
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

let tbodyRegisteredMembers;
let tbodyDeniedMembers;
let he = require('he');
const ListMemberPage = async () => {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtmlConfirmedMembers;
  pageDiv.innerHTML += tableHtmlDeniedMembers;
  const members = await getAllMembers();
  tbodyRegisteredMembers = document.querySelector("#tbody_registered_members");
  tbodyRegisteredMembers.innerHTML = "";

  tbodyDeniedMembers = document.querySelector("#tbody_denied_members");
  tbodyDeniedMembers.innerHTML = "";

  for (const member of members) {
    if (member.actualState === "registered") {
      await showRegisteredMember(member);
    } else if (member.actualState === "denied") {
      await showDeniedMember(member);
    }
  }
};

async function showRegisteredMember(member) {
  //For Each Member
  tbodyRegisteredMembers.innerHTML += `
      <tr id="RegisteredLine">
        <td>${he.decode(member.firstName)}</td>
        <td>${he.decode(member.lastName)}</td>
        <td><input class="form-check-input" type="checkbox" id="RegisteredIsAdmin" value="${member.id}"></td>
        <td><button type="submit" class="btn btn-primary" id="RegisteredConfirmButton" value=${member.id}>Confirmer</button></td>
        <td><button type="submit" class="btn btn-danger" id="RegisteredRefuseButton" value=${member.id}>Refuser</button></td>
        <td><div class="form-floating mb-3"><input type="text" class="form-control" id="refusalText"><label for="refusalText">Message</label></div></td>
        <td>
          <div id="itemButtons">
            <a href="/member?id=${member.id}" type="button" class="btn btn-primary">Voir les détails</a>
          </div>
        </td>
      </tr>    
    `;

  // Is Admin Button
  const isAdminButtons = document.querySelectorAll("#RegisteredIsAdmin");

  // Confirm Button
  const confirmButtons = document.querySelectorAll(
      "#RegisteredConfirmButton");
  confirmButtons.forEach(confirmButton => {
    confirmButton.addEventListener("click", async function () {
      let isAdminButtonChecked;
      isAdminButtons.forEach(button => {
        if (button.value === confirmButton.value) {
          isAdminButtonChecked = button.checked;
        }
      });
      const member = await getOneMember(confirmButton.value);
      const confirmMember = {
        id: member.id,
        isAdmin: isAdminButtonChecked,
        version: member.version
      };
      await confirmInscription(confirmMember);
      Redirect("/list_member");
    });
  });

  //Deny Button
  const denyButtons = document.querySelectorAll("#RegisteredRefuseButton");
  denyButtons.forEach(denyButton => {
    denyButton.addEventListener("click", async function () {
      const refusalText = document.querySelector("#refusalText").value;
      //Confirm the registration (Click on the button)

      const refusal = {
        member: {
          id: denyButton.value
        },
        text: refusalText,
      };
      await denyMember(refusal);
      Redirect("/list_member");
    });
  });
  const line = document.querySelector("#RegisteredLine");
  line.dataset.memberId = member.id;
}

async function showDeniedMember(member) {
  const tbody = document.querySelector("#tbody_denied_members");
  tbody.innerHTML += `
      <tr id="DeniedLine">
        <td>${member.firstName}</td>
        <td>${member.lastName}</td>
        <td><input class="form-check-input" type="checkbox" value=${member.id} id="DeniedIsAdmin"></td>
        <td><button type="submit" class="btn btn-primary" id="DeniedConfirmButton" value=${member.id}>Confirmer</button></td>
      </tr>    
    `;

  // Is Admin Button
  const isAdminButtons = document.querySelectorAll("#DeniedIsAdmin");

  // Confirm Button
  const confirmButtons = document.querySelectorAll("#DeniedConfirmButton");
  for (const confirmButton of confirmButtons) {
    confirmButton.addEventListener("click", async function () {
      let isAdminButtonChecked;
      isAdminButtons.forEach(button => {
        if (button.value === confirmButton.value) {
          isAdminButtonChecked = button.checked;
        }
      });

      const member = await getOneMember(confirmButton.value);
      const confirmMember = {
        id: confirmButton.value,
        isAdmin: isAdminButtonChecked,
        version: member.version
      };
      await confirmInscription(confirmMember);
      Redirect("/list_member");
    });
  }

  const line = document.querySelector("#DeniedLine");
  line.dataset.memberId = member.id;
}

export default ListMemberPage;