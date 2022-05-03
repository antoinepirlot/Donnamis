import {
  cancelOffer as cancelOfferBackEnd,
  chooseRecipient as chooseRecpientBackEnd,
  getInterestedMembers,
  getItem,
  getMyItems,
  markItemAs as markItemAsaBackEnd,
  offerAgain as offerAgainBackEnd
} from "../../../utils/BackEndRequests";
import {getObject, getPayload} from "../../../utils/session";
import {showError} from "../../../utils/ShowError";
import {openModal} from "../../../utils/Modals";
import {Redirect} from "../../Router/Router";
import {createItemsSearchBar} from "../../../utils/Search";
import {getMyItemsHtml} from "../../../utils/HtmlCode";

const myItemsPageHtml = `
  <div>
    <h1 class="display-3" id="all_items_title">Mes objets</h1>
    <div id="searchBarMyItemsPage">
    </div>
    <div id="searchDateMyItemsPage">
      <form>
      Entre le <input id="formStartDateMyItemsPage" type="date">
      et le <input id="formEndDateMyItemsPage" type="date">
      <input type="submit" class="btn btn-primary" value="Rechercher">
      </form>
    </div>
    <div class="row" id="myItems">
    </div>
  </div>
  <div id="errorMessageMyItemsPage"></div>
  
  <!-- The Modal offer again -->
<div id="myItemsPageModal" class="modal">
  <div class="modal-content">
    <span id="myItemsPageModalCloseButton" class="close">&times;</span>
    <form id="offerAgainForm">
      <h5>Pour offrir un objet à nouveau, il faut que vous entrez une nouvelle plage horaire</h5><br>
      <br>
      Disponibilités horaire<span id="asterisk">*</span>:<br>
      <textarea id="timeSlotFormOfferAgain" cols="30" rows="3"></textarea><br>
      <br>
      <input type="submit" value="Envoyer">
    </form>
  </div>
</div>

<!--Modal for choose recipient-->
<div id="chooseRecipientModal" class="modal">
  <div class="modal-content">
    <span id="chooseRecipientModalCloseButton" class="close">&times;</span>
    <form id="offerAgainForm">
      <h5>Sélectionnez un membre dans la liste des membres intéressés</h5><br>
      <br>
      Liste de membre<span id="asterisk">*</span>:<br>
      <input id="chooseRecipientMemberListForm" list="chooseRecipientMembersList" placeholder="Choisissez un membre.">
      <datalist id="chooseRecipientMembersList"></datalist><br>
      <br>
      <input type="submit" value="Envoyer">
    </form>
  </div>
</div>
`;

let idItem;
let items;

const MyItemsPage = async () => {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = myItemsPageHtml;
  items = await getMyItems();
  if (items.length === 0) {
    const message = "Vous n'avez aucune offre.";
    const errorMessageMyItemsPage = document.querySelector(
        "#errorMessageMyItemsPage");
    showError(message, "info", errorMessageMyItemsPage);
    return;
  }
  const myItemsDiv = document.querySelector("#myItems");
  myItemsDiv.innerHTML = await getMyItemsHtml(items);
  await showButtons();
  createItemsSearchBar(items, "#searchBarMyItemsPage", "#myItems",
      "myItemsPage");
  const dateForm = document.querySelector("#searchDateMyItemsPage");
  dateForm.addEventListener("submit", filterItemsByDate);
}

async function showButtons() {
  /*************/
  /*Offer again*/
  /*************/
  const offerAgainButtons = document.querySelectorAll("#offerAgainButton");
  offerAgainButtons.forEach((offerAgainButton) => {
    offerAgainButton.addEventListener("click", async () => {
      idItem = offerAgainButton.value;
      openModal("#myItemsPageModal", "#myItemsPageModalCloseButton");
      const item = await getItem(idItem);
      const timeSlotTextArea = document.querySelector(
          "#timeSlotFormOfferAgain");
      timeSlotTextArea.innerHTML = item.offerList[0].timeSlot;
      const offerAgainForm = document.querySelector("#offerAgainForm");
      offerAgainForm.addEventListener("submit", await offerAgain);
    })
  });

  /*********************************/
  /*Choose a recipient for the item*/
  /*********************************/
  // Mettre cette fonction dans HtmlCode.js
  const chooseRecipientButtons = document.querySelectorAll(
      "#chooseRecipientButton");

  for (const chooseRecipientButton of chooseRecipientButtons) {

    idItem = chooseRecipientButton.value;
    const item = items.find((item) => item.id == idItem);
    const members = await getInterestedMembers(item.offerList[0].id);
    chooseRecipientButton.addEventListener("click", async () => {

      console.log(members);
      openModal("#chooseRecipientModal", "#chooseRecipientModalCloseButton");
      const memberList = document.querySelector(
          "#chooseRecipientMembersList");
      memberList.innerHTML = ""; //empties the datalist of old members
      members.forEach((member) => {
        memberList.innerHTML += `
          <option value="${member.username}">
        `;
      });
      const chooseRecipientModal = document.querySelector(
          "#chooseRecipientModal");
      chooseRecipientModal.addEventListener("submit", await chooseRecipient);
    });
  }

  /********************/
  /*Mark item as given*/
  /********************/
  const markReceivedButtons = document.querySelectorAll("#markReceivedButton");
  markReceivedButtons.forEach((markReceivedButton) => {
    markReceivedButton.addEventListener("click", async () => {
      idItem = markReceivedButton.value;
      await markItemAs(true);
    });
  });

  /***************************/
  /*Mark item as not received*/
  /***************************/
  const markNotGivenButtons = document.querySelectorAll("#markNotGivenButton");
  markNotGivenButtons.forEach((markNotGivenButton) => {
    markNotGivenButton.addEventListener("click", async () => {
      idItem = markNotGivenButton.value;
      await markItemAs(false);
    });
  });

  /*****************/
  /*Cancel an offer*/
  /*****************/
  const cancelButtons = document.querySelectorAll("#itemCancelled");
  cancelButtons.forEach(cancelButton => {
    cancelButton.addEventListener("click", async () => {
      await cancelOfferBackEnd(cancelButton.value);
      await MyItemsPage()
    });
  });
}

async function offerAgain(e) {
  e.preventDefault();
  const timeSlot = document.querySelector("#timeSlotFormOfferAgain").value;
  const newOffer = {
    idItem: idItem,
    timeSlot: timeSlot,
  }
  try {
    await offerAgainBackEnd(newOffer);
    await MyItemsPage();
  } catch (e) {
    console.error(e);
  }
}

async function chooseRecipient(e) {
  e.preventDefault();
  const errorDiv = document.querySelector("#errorMessageMyItemsPage");
  showError("Sélection du receveur en cours...", "info", errorDiv);
  const recipientUsername = document.querySelector(
      "#chooseRecipientMemberListForm").value;
  const recipient = {
    item: {
      id: idItem
    },
    member: {
      username: recipientUsername
    }
  }
  try {
    await chooseRecpientBackEnd(recipient)
    showError("Vous avez choisi l'utilisateur " + recipientUsername
        + " comme receveur.", "success", errorDiv);
    await MyItemsPage();
  } catch (e) {
    showError("Impossible de choisir le receveur.", "danger", errorDiv);
  }
}

async function markItemAs(given) {
  const errorDiv = document.querySelector("#errorMessageMyItemsPage");
  showError("Le changement est en cours...", "info", errorDiv);

  const item = await getItem(idItem);
  const itemMark = {
    id: idItem,
    member: {
      id: getPayload().id,
      version: getObject("memberDTO").version
    },
    version: item.version
  }
  try {
    await markItemAsaBackEnd(given, itemMark);
    showError("L'objet à bien été marqué comme donné.", "success", errorDiv);
    await MyItemsPage();
  } catch (e) {
    showError("L'objet n'a pas été marqué comme donné.", "danger", errorDiv);
  }
}

function filterItemsByDate(e) {
  e.preventDefault();
  let startDate = document.querySelector("#formStartDateMyItemsPage").value;
  let endDate = document.querySelector("#formEndDateMyItemsPage").value;
  if (startDate === "" || endDate === "") {
    const myItemsDiv = document.querySelector("#myItems");
    myItemsDiv.innerHTML = getMyItemsHtml(items);
    return;
  }
  startDate = new Date(startDate);
  endDate = new Date(endDate);
  const myItemsDiv = document.querySelector("#myItems");
  const filteredItems = items.filter(item =>
      new Date(item.lastOffer.date).getDate() >= startDate.getDate()
      && new Date(item.lastOffer.date).getDate() <= endDate.getDate()
      && new Date(item.lastOffer.date).getMonth() >= startDate.getMonth()
      && new Date(item.lastOffer.date).getMonth() <= endDate.getMonth()
      && new Date(item.lastOffer.date).getFullYear() >= startDate.getFullYear()
      && new Date(item.lastOffer.date).getFullYear() <= endDate.getFullYear()
  );
  if (!filteredItems) {
    const errorDiv = document.querySelector("#errorMessageMyItemsPage");
    showError("Aucun objet pour ces dates.", "info", errorDiv);
  }
  myItemsDiv.innerHTML = getMyItemsHtml(filteredItems);
}

export default MyItemsPage;