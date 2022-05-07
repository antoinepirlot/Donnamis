import {
  evaluateItemBackEnd,
  getAllRatings,
  getAssignedItems,
  getItem
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
      <div id="myAssignedItems"></div>
    </div>
    <div>
      <h1 class="display-3">Mes objets reçus</h1>
      <h5 class="text-secondary">Voici vos objets reçus, vous pouvez les évaluers</h5>
      <div id="myReceivedItems"></div>
    </div>
  </div>
  
  <!--Rating Modal-->
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
        <!--Value of button is defined later and it match with item'id-->
        <button id="ratingButtonModal" class="btn btn-primary" value="">Evaluer</button>
      </form>
    </div>
    <div id="errorMessage"></div>
  </div>
`;

let idIdem;

const MyAssignedItems = async () => {
  if (!getPayload()) {
    Redirect("/login");
    return;
  }
  const items = await getAssignedItems();
  const pageDiv = document.querySelector("#page");
  if (!items) {
    pageDiv.innerHTML = `
      <h1 class="display-3">Aucun objets assignés</h1>
      <h5 class="text-secondary">Vous n'avez aucun objets assignés pour le moment</h5>
    `;
    return;
  }
  pageDiv.innerHTML = html;

  //Show items
  const myAssignedItems = document.querySelector("#myAssignedItems");
  myAssignedItems.innerHTML = "";
  const myReceivedItems = document.querySelector("#myReceivedItems");
  myReceivedItems.innerHTML = "";
  const ratings = await getAllRatings();

  items.forEach((item) => {
    if (item.offerStatus === "assigned") {
      myAssignedItems.innerHTML += getAssignedItemHtml(item);
    } else if (item.offerStatus === "given") {
      myReceivedItems.innerHTML += getGivenItemHtml(item, ratings);
    }
  });
  const ratingButtons = document.querySelectorAll("#ratingButton");
  const ratingButtonModal = document.querySelector("#ratingButtonModal");
  ratingButtons.forEach((ratingButton) => {
    idIdem = ratingButton.value;
    ratingButton.addEventListener("click", () => {
      openModal("#ratingModal", "#ratingCloseModal");
      ratingButtonModal.value = idIdem;
      ratingButton.addEventListener("click", showRatingForm);
    });
  });
  pageDiv.addEventListener("submit", await evaluateItem);
}

function showRatingForm(e) {
  e.preventDefault();
  const showRatingButton = document.querySelector("#ratingButtonModal");
  showRatingButton.value = idIdem;
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
  const idItem = document.querySelector("#ratingButtonModal").value;
  const member = {
    id: getPayload().id
  };
  const item = await getItem(idItem);

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

  Redirect("/assigned_items")

}

export default MyAssignedItems;