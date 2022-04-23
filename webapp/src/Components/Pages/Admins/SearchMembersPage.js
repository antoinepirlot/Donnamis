import {getPayload,} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {getAllMembers} from "../../../utils/BackEndRequests";

const viewSearchbarHtml = `
  <input class="form-control me-2" id="searchInput" type="search" placeholder="Rechercher un membre" aria-label="Rechercher">
  <div>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Nom</th>
          <th scope="col">Prénom</th>
          <th scope="col">Voir membre</th>
        </tr>
      </thead>
      <tbody id="tbody_all_members">
      </tbody>
    </table>
</div>
`;

async function SearchMembersPage() {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = viewSearchbarHtml;
  const members = await getAllMembers();
  showFilterMembers(members)
  //Listener pour chaque frappe au clavier
  const searchInput = document.getElementById('searchInput');
  searchInput.addEventListener('keyup', function () {
        //Empty the table
        const tbody = document.querySelector("#tbody_all_members");
        tbody.innerHTML = "";

        const input = searchInput.value;
        const result = members.filter(
            member => member.lastName.toLowerCase().includes(input)
                || member.firstName.toLowerCase().includes(
                    input))

        showFilterMembers(result)

      }
  )
}

function showFilterMembers(members) {
  members.forEach((member) => {
    const tbody = document.querySelector("#tbody_all_members");
    tbody.innerHTML += `
      <tr id="MemberLine">
        <td>${member.firstName}</td>
        <td>${member.lastName}</td>
        <td><a href="/member?id=${member.id}" type="button" class="btn btn-primary">Voir les détails</a></td>
      </tr>    
    `;
  })
}

export default SearchMembersPage;