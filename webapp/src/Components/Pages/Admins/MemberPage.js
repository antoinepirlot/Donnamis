import {getNumberOfItems, getOneMember} from "../../../utils/BackEndRequests";

const memberPageHtml = `
  <div id="memberPageContent" class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2 id="profilUsernameMemberPage" class="display-3"></h2>
  </div>
  <div id="errorMessage"></div>
`;

let idMember;

const MemberPage = async () => {
  const page = document.querySelector("#page");
  page.innerHTML = memberPageHtml;
  idMember = new URLSearchParams(window.location.search).get("id");
  const member = await getOneMember(idMember);
  const profilUsernameDiv = document.querySelector("#profilUsernameMemberPage");
  profilUsernameDiv.innerText = `Profile de: ${member.username}`;
  await showMemberInformation(member);
}

async function showMemberInformation(member) {
  const content = document.querySelector("#memberPageContent");
  let contentHtml = `
    <p>
      Prénom:${member.firstName}<br>
      Nom: ${member.lastName}<br>
      ${getAddressHtml(member.address)}<br>
      Statut: ${getActualState(member)}<br>
      Administrateur: ${member.isAdmin ? "Oui" : "Non"}<br>
      Numéro de téléphone: ${member.phoneNumber ? member.phoneNumber : "Aucun"}<br>
      Nombre d'objets offerts: ${await getNumberOfItems(member.id, "donated")}<br>
      Nombre d'objets donnés: ${await getNumberOfItems(member.id, "given")}<br>
      Nombre d'objets intéréssé mais non reçu: En cours de développement<br>
      Nombre d'objets reçus: En cours de développement<br>
  `;
  content.innerHTML += contentHtml;
}

function getActualState(member) {
  switch (member.actualState) {
    case "registered":
      return "Inscrit";
    case "confirmed":
      return "Confirmé";
    case "denied":
      return "Refusé";
    default:
      "Statut inconnu";
  }
}

function getAddressHtml(address) {
  let addressHtml = `
    Adresse: ${address.street} n°${address.buildingNumber}
  `;
  if (address.unitNumber) {
    addressHtml += `
      Boite ${address.unitNumber}
    `;
  }
  addressHtml += `
    ${address.postcode} ${address.commune}
    
  `;
  return addressHtml;
}

export default MemberPage