import {
  evaluateItemBackEnd,
  getAssignedItems
} from "../../../utils/BackEndRequests";
import {getShowItemsGivenAndAssignedHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showError} from "../../../utils/ShowError";
import {openModal} from "../../../utils/Modals";

let ratingModal = `
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
        <p></p>
        <input type="submit" value="Evaluer">
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
  const items = await getAssignedItems();
  pageDiv.innerHTML = getShowItemsGivenAndAssignedHtml(items);

  pageDiv.innerHTML += ratingModal;

  if (items.length == 0) {
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