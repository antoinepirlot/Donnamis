import {getAllItemsByOfferStatus} from "../../../utils/BackEndRequests";
import {checkIfMemberLoggedIn, getShowItemsHtml} from "../../../utils/HtmlCode";
import {getPayload} from "../../../utils/session";
import {createItemsSearchBar} from "../../../utils/Search";

const tableHtml = `
  <div>
    <div id="all_latest_items_title">
      <h1 class="display-3">Bienvenue sur Donnamis</h1>
      <h5 class="text-secondary">Voici les derniers objets mis en ligne</h5>
    </div>
     <div id="searchBarHomePage">
     </div> 
    <div class="row" id="all_offered_items">
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

const HomePage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = tableHtml;
  const items = await getAllItemsByOfferStatus("donated");
  const tbody = document.querySelector("#all_offered_items");
  tbody.innerHTML = getShowItemsHtml(items);
  checkIfMemberLoggedIn("#homePageModal", "#homePageModalCloseButton");

  //Searching an item
  //Listener pour chaque frappe au clavier
  if (getPayload()) {
    createItemsSearchBar(items, "#searchBarHomePage", "#all_offered_items",
        "homePage");
  }
}

//function setSeeItemEvent(itemButtons) {
//  itemButtons.forEach(itemButton => {
//    itemButton.addEventListener("click", (e) => {
//      e.preventDefault();
//      const id = itemButton.querySelector("#itemIdButton").value;
//      //setLocalObject("idItem", id);
//      Redirect(`/item?id=${id}`);
//    });
//  })
//}
export default HomePage;
