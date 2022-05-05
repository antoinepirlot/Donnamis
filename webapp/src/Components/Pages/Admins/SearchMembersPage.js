import {isAdmin,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {getAllMembers} from "../../../utils/BackEndRequests";

const viewSearchbarHtml = `
  <h1 class="display-3" id="search_member_title">Rechercher des membres</h1>
  <input class="form-control me-2 w-75 d-inline-block" id="searchInput" type="search" placeholder="Rechercher un membre avec nom, prénom ou adresse" aria-label="Rechercher">
  <div class="w-75 d-inline-block">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Prénom</th>
          <th scope="col">Nom</th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody id="tbody_all_members">
      </tbody>
    </table>
</div>
`;

//'he' is a library to decode HTML element from a string
let he = require('he');

async function SearchMembersPage() {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = viewSearchbarHtml;
  const members = await getAllMembers();
  const tbody = document.querySelector("#tbody_all_members");
  showFilterMembers(members)
  //Listener pour chaque frappe au clavier
  const searchInput = document.getElementById('searchInput');
  searchInput.addEventListener('keyup', function () {

        //Empty the table
        tbody.innerHTML = "";

        const input = searchInput.value.toLowerCase().trim();
        const result = members.filter(
            member => he.decode(member.lastName).toLowerCase().includes(input)
                || he.decode(member.firstName).toLowerCase().includes(
                    input)
                || he.decode(member.address.postcode).includes(input)
                || he.decode(member.address.commune).toLowerCase().includes(input));

        if (result.length < 1) {
          tbody.innerHTML = `<h1 class="display-6" id="SearchErrorMessageMember">Il n'y a aucun résultat pour cette recherche</h1>`;
        } else {
          showFilterMembers(result)
        }
      }
  )
}

function showFilterMembers(members) {
  const tbody = document.querySelector("#tbody_all_members");
  tbody.innerHTML = "";
  members.forEach((member) => {
    tbody.innerHTML += `
      <tr id="MemberLine">
        <td>${he.decode(member.firstName)}</td>
        <td>${he.decode(member.lastName)}</td>
        <td><a href="/member?id=${member.id}" type="button" class="btn btn-primary">Voir détails</a></td>
      </tr>    
    `;
  })
}

export default SearchMembersPage;