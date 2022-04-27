import {
  evaluateItemBackEnd,
  getAssignedItems
} from "../../../utils/BackEndRequests";
import {getAssignedItemHtml, getGivenItemHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {openModal} from "../../../utils/Modals";

let html = `
  <div>
    <div>
      <h1 class="display-3">Mes objets attribués</h1>
      <h5 class="text-secondary">Voici vos objets attribués en attente de votre récupération</h5>
    </div>
    <div id="myAssignedItems"></div>
    <div id="">
      <h1 class="display-3">Mes objets reçus</h1>
      <h5 class="text-secondary">Voici vos objets reçus, vous pouvez les évaluers</h5>
    </div>
    <div id="myReceivedItems"></div>
  </div>
  <div id="ratingModal" class="modal">
    <div class="modal-content">
      <span id="ratingCloseModal" class="close">&times;</span>
      <form id="ratingForm">
        <h5>Evaluer mon gain</h5><br>
        <div>
          <input type="radio" id="ratingChoice1"
           name="rating" value="1">
          <label for="ratingChoice1">1</label>
      
          <input type="radio" id="ratingChoice2"
           name="rating" value="2">
          <label for="ratingChoice2">2</label>
      
          <input type="radio" id="ratingChoice3"
           name="rating" value="3">
          <label for="ratingChoice3">3</label>
          
          <input type="radio" id="ratingChoice4"
           name="rating" value="4">
          <label for="ratingChoice4">4</label>
          
          <input type="radio" id="ratingChoice5"
           name="rating" value="5">
          <label for="ratingChoice5">5</label>
        </div>
        <p>Commentaire</p>
        <input type="text" id="textForm">
        <br>
        <button id="ratingButtonModal" class="btn btn-primary">Evaluer</button>
      </form>
    </div>
    <div id="errorMessage"></div>
  </div>
`;

const MyAssignedItems = async () => {
  if (!getPayload()) {
    Redirect("/login");
    return;
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = html;
  const items = await getAssignedItems();

  //Show items
  const myAssignedItems = document.querySelector("#myAssignedItems");
  myAssignedItems.innerHTML = "";
  const myReceivedItems = document.querySelector("#myReceivedItems");
  myReceivedItems.innerHTML = "";
  items.forEach((item) => {

    if (item.offerStatus === "assigned") {
      myAssignedItems.innerHTML += getAssignedItemHtml(item);
    } else if (item.offerStatus === "given") {
      myReceivedItems.innerHTML += getGivenItemHtml(item);
    }
  });
  const ratingButtons = document.querySelectorAll("#ratingButton");
  const ratingButtonModal = document.querySelector("#ratingButtonModal");
  ratingButtons.forEach((ratingButton) => {
    ratingButton.addEventListener("click", () => {
      openModal("#ratingModal", "#ratingCloseModal");
      ratingButtonModal.value = ratingButton.value;
    });
  });
  pageDiv.addEventListener("submit", await evaluateItem);

  if (items.length === 0) {
    pageDiv.innerHTML = `
   <h1 class="display-3">Aucun objets assignés</h1>
   <h5 class="text-secondary">Vous n'avez aucun objets assignés pour le moment</h5>
  `;
  } else {
    const ratingButton = document.querySelector("#ratingButtons");
    ratingButton.addEventListener("click", showRatingForm);
  }
}

function showRatingForm(e) {
  e.preventDefault();
  const showRatingButton = document.querySelector("#evaluateButton");
  showRatingButton.value = item.id;
  openModal("#ratingModal", "#ratingCloseModal");
  const ratingForm = document.querySelector("#ratingForm");
  ratingForm.addEventListener("submit", evaluateItem);
}

async function evaluateItem(e) {
  e.preventDefault();

  const ratingMessage = document.querySelector("#errorMessage");

  const ratingNote = document.querySelector(
      'input[name="rating"]:checked').value;
  const text = document.querySelector("#textForm").value;
  const member = {
    id: getPayload().id
  };
  // ATTENTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  const item = {
    id: 4
  };
  // ATTENTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

  const rating = {
    item: item,
    rating: ratingNote,
    text: text,
    member: member
  };

  try {
    await evaluateItemBackEnd(rating, ratingMessage);
  } catch (err) {
    showError("Votre évaluation n'a pas pu être ajotuée", "danger",
        ratingMessage);
  }

}

export default MyAssignedItems;