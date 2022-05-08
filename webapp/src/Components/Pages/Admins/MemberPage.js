import {
  getAllItemsByMemberIdAndOfferStatus,
  getNumberOfItems,
  getNumberOfReceivedOrNotReceivedItems,
  getOneMember,
  setMemberAvailability,
  setRecipientUnavailable
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getShowItemsHtml} from "../../../utils/HtmlCode";
import {Redirect} from "../../Router/Router";
import {isAdmin} from "../../../utils/session";

const memberPageHtml = `
  <div class="whiteCard">
    <div id="memberPageContent" ><!--class="d-flex bd-highlight mb-3 shadow-lg p-3 mb-5 bg-white rounded" -->
    </div>
  </div>
  <div id="donatedItemsMemberPage">
  
  </div>
  <div id="donatedItemsMemberPageMessage">
  </div>
  <div id="receivedItemsMemberPage">
  </div>
  <div id="receivedItemsMemberPageMessage"></div>
  <div id="errorMessage"></div>
  
`;

let idMember;
let he = require('he');

const MemberPage = async () => {
  if (!isAdmin()) {
    Redirect("/");
    return;
  }
  idMember = new URLSearchParams(window.location.search).get("id");
  const member = await getOneMember(idMember);
  const page = document.querySelector("#page");
  page.innerHTML = `<h1 class="display-3" id="login_title">Profile de ${he.decode(
      member.username)}</h1>`;
  page.innerHTML += memberPageHtml;
  await showMemberInformation(member);
  await showDonatedItems(member);
  await showReceivedItems(member);
}

async function showDonatedItems(member) {
  const donatedItems = await getAllItemsByMemberIdAndOfferStatus(member.id,
      "donated");
  if (!donatedItems) {
    const messageDiv = document.querySelector("#donatedItemsMemberPageMessage");
    messageDiv.innerHTML = `<h1 class="display-6">${member.username} n'a donné aucun objet</h1>`;
    return;
  }
  const donatedItemsDiv = document.querySelector("#donatedItemsMemberPage");
  donatedItemsDiv.innerHTML += getShowItemsHtml(donatedItems);
}

async function showReceivedItems(member) {
  const receivedItems = await getAllItemsByMemberIdAndOfferStatus(member.id,
      "given");
  if (!receivedItems) {
    const messageDiv = document.querySelector(
        "#receivedItemsMemberPageMessage");
    messageDiv.innerHTML = `<h2 class="display-6">${member.username} n'a reçus aucun objet</h2>`;
    return;
  }
  const receivedItemsDiv = document.querySelector("#receivedItemsMemberPage");
  receivedItemsDiv.innerHTML += getShowItemsHtml(receivedItems);
}

async function showMemberInformation(member) {
  const content = document.querySelector("#memberPageContent");
  let contentHtml = `
      <div>
          <div class="profile-head">
            <h5>
                ${he.decode(member.firstName)+ " " + he.decode(member.lastName)}
            </h5>
          </div>
      </div>
        <div class="nav nav-tabs" id="nav-tab" role="tablist">
        
          <button class="nav-link active" id="nav-home-tab" data-bs-toggle="tab" 
          data-bs-target="#nav-home" type="button" role="tab" aria-controls="nav-home" 
          aria-selected="true">Profile</button> 
          
          <button class="nav-link" id="nav-profile-tab" data-bs-toggle="tab" 
          data-bs-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" 
          aria-selected="false">Adresse</button> 
        </div>
      </nav>
      <div class="tab-content" id="myTabContent">
        <div class="col-md-8">
          <div class="tab-content profile-tab" id="myTabContent">
            <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
        <!-- USER'S INFO -->
          <div class="row">
            <div class="col-md-11">
              <label>Prénom</label>
            </div>
            <div class="col-md-1">
              <p>${he.decode(member.firstName)}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Nom</label>
            </div>
            <div class="col-md-1">
              <p>${he.decode(member.lastName)}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Statut</label>
            </div>
            <div class="col-md-1">
              <p>${getActualState(member)}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Admin</label>
            </div>
            <div class="col-md-1">
              <p>${member.isAdmin ? "Oui" : "Non"}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Numéro de téléphone</label>
            </div>
            <div class="col-md-1">
              <p>${member.phoneNumber ? member.phoneNumber : "Aucun"}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Nombre d'objets offerts</label>
            </div>
            <div class="col-md-1">
              <p>${await getNumberOfItems(member.id, "donated")}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Nombre d'objets donnés</label>
            </div>
            <div class="col-md-1">
              <p>${await getNumberOfItems(member.id, "given")}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Nombre de marque d'intérets</label>
            </div>
            <div class="col-md-1">
              <p>${await getNumberOfReceivedOrNotReceivedItems(member.id, false)}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-11">
              <label>Nombre d'objets reçus</label>
            </div>
            <div class="col-md-1">
              <p>${await getNumberOfReceivedOrNotReceivedItems(member.id, true)}</p>
            </div>
          </div>
        </div>
            <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
              <!-- ADDRESS'S USER INFO  -->
              <div class="row">
                <div class="col-md-6">
                  <label>Rue </label>
                </div>
                <div class="col-md-6">
                  <p>${member.address.street}</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <label>Numéro </label>
                </div>
                <div class="col-md-6">
                  <p>${member.address.buildingNumber}</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <label>Numéro de boite </label>
                </div>
                <div class="col-md-6">
                  <p>${member.address.unitNumber ? member.address.unitNumber : "Aucun" }</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <label>Code postal </label>
                </div>
                <div class="col-md-6">
                  <p>${member.address.postcode}</p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <label>Commune </label>
                </div>
                <div class="col-md-6">
                  <p>${member.address.commune}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>  
      `
  content.innerHTML += contentHtml;
  const pageErrorDiv = document.querySelector("#errorMessage");
  let button;

  //Create button if member confirmed or unavailable
  if (member.actualState === 'confirmed' || member.actualState
      === 'unavailable') {
    content.innerHTML += `<button id="markUnavailableButton" class="btn btn-primary"></button>`;
    button = document.querySelector("#markUnavailableButton");

    //Change the value of the button
    if (member.actualState === 'confirmed') {
      button.innerHTML = "Marquer Indisponible";
    } else if (member.actualState === 'unavailable') {
      button.innerHTML = "Marquer Disponible";
    }

    button.addEventListener("click", async function () {

      const memberUnavailable = {
        id: member.id,
        actualState: member.actualState === "confirmed" ? "unavailable"
            : "confirmed",
        version: member.version
      };

      const recipient = {
        member: memberUnavailable
      };
      try {
        await setMemberAvailability(memberUnavailable);
        if (memberUnavailable.actualState === "unavailable") {
          await setRecipientUnavailable(recipient);
        }
        await MemberPage();
      } catch (err) {
        console.error(err);
        showError("Une erreur est survenue.", "danger", pageErrorDiv);
      }
    });
  }
}

function getActualState(member) {
  switch (member.actualState) {
    case "registered":
      return "Inscrit";
    case "confirmed":
      return "Confirmé";
    case "denied":
      return "Refusé";
    case "unavailable":
      return "Malade";
    default:
      return "Statut inconnu";
  }
}

function getAddressHtml(address) {
  let addressHtml = `
    Adresse : ${address.street} n°${address.buildingNumber}
  `;
  if (address.unitNumber) {
    addressHtml += `
      Boite ${address.unitNumber}
    `;
  }
  addressHtml += `
    ${address.postcode} ${address.commune}
    
  `;
  return he.decode(addressHtml);
}

export default MemberPage