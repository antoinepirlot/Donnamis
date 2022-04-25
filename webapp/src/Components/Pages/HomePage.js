import {getAllItemsByOfferStatus} from "../../utils/BackEndRequests";
import {checkIfMemberLoggedIn, getShowItemsHtml} from "../../utils/HtmlCode";

const tableHtml = `
  <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Bienvenue sur Donnamis</h1>
      <h5 class="text-secondary">Voici les derniers objets mis en ligne</h5>
    </div>
    <div class="row" id="all_latest_items">
    </div>
  </div>
  
    <!-- The Modal ask if the member want to log in or register -->
  <div id="homePageModal" class="modal">
    <div class="modal-content">
      <span id="homePageModalCloseButton" class="close">&times;</span>
      <h5>Vous devez être connecté pour voir les détails de cet objet</h5><br>
      <button id="iHaveAnAccountButton" type="submit" class="btn btn-primary" >J'ai déjà un compte</button>
      <button id="iDontHaveAnAccountButton" type="submit" class="btn btn-danger" >Je veux créer un compte</button>
    </div>
  </div>
`;

const LatestItemsOffersPage = async () => {
  const pageDiv = document.querySelector("#page");
  console.log("he")
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus("donated");
  let tbody = document.querySelector("#all_latest_items");
  tbody.innerHTML = getShowItemsHtml(items);
  checkIfMemberLoggedIn("#homePageModal", "#homePageModalCloseButton");
};

export default LatestItemsOffersPage;
