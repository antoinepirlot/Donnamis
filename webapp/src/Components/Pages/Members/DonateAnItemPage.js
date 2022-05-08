import {
  addNewItemsType,
  getItemsTypes,
  offerAnItem
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showItemsTypes} from "../../../utils/HtmlCode";
import {sendFile} from "../../../utils/File";

const htmlForm = `
  <h1 class="display-3">Offrir un objet</h1>
  <div class="form">
    <form id="offerItemForm" class="d-flex bd-highlight mb-3 shadow-lg p-3 mb-5 bg-white rounded">
      <div id="left" class="mr-auto p-2 bd-highlight" xmlns="http://www.w3.org/1999/html">
          <div class="mb-3">
            <label class="form-label">Nom de l'objet<span id="asterisk">*</span></label>
            <textarea id="titleForm" class="form-control" cols="30" rows="3"></textarea><br>
          </div>
          <div class="mb-3">
            <label class="form-label">Description de l'objet<span id="asterisk">*</span></label>
            <textarea id="itemDescriptionForm" class="form-control" cols="30" rows="3"></textarea><br>
          </div>
          <div class="mb-3">
            <label class="form-label">Disponibilités horaire<span id="asterisk">*</span></label>
            <textarea id="timeSlotForm" class="form-control" cols="30" rows="3"></textarea><br>
          </div>
        </div>
        <div id="right" class="p-2 bd-highlight">
          <div class="mb-3">
            <label class="form-label">Type de l'objet<span id="asterisk">*</span></label>
            <input id="itemTypeFormList" class="form-control" list="itemsTypesDonateAnItemPage" placeholder="Séléctionne le type d'objet"><br>
            <datalist id="itemsTypesDonateAnItemPage"></datalist>
          </div>
          <div class="mb-3">
            <label class="form-label">Ajouter une photo</label>
            <input id="photoName" class="form-control" name="newItemFile" type= "file" /> <br><br>
          </div>
          <input class="btn btn-primary" type="submit" value="Offrir"><br>
          <span id="asterisk">* Champs obligatoires</span>
        </div>
      </div>
    </form>
    <div id="errorMessageOfferAnItemPage"></div>
  </div>
`;

let itemsTypes;
let errorMessageOfferAnItemPage;

const DonateAnItemPage = async () => {
  if (!getPayload()) {
    Redirect("/");
    return;
  }
  const page = document.querySelector("#page");
  page.innerHTML = htmlForm;
  errorMessageOfferAnItemPage = document.querySelector(
      "#errorMessageOfferAnItemPage");
  const offerItemForm = document.querySelector("#offerItemForm");
  itemsTypes = await getItemsTypes();
  showItemsTypes("#itemsTypesDonateAnItemPage", itemsTypes);
  offerItemForm.addEventListener("submit", await offerItem);
};

async function offerItem(e) {
  e.preventDefault();
  const title = document.querySelector("#titleForm").value;
  const itemDescription = document.querySelector("#itemDescriptionForm").value;
  let photoName = document.querySelector("#photoName").value;
  if (photoName === "") {
    photoName = null;
  }
  const timeSlot = document.querySelector("#timeSlotForm").value;
  const itemTypeValue = document.querySelector("#itemTypeFormList").value;
  const payload = getPayload();
  const offer = {
    timeSlot: timeSlot,
  };
  const offersList = [offer];
  const member = {
    id: payload.id
  };
  const itemsType = {
    itemType: itemTypeValue
  };
  const item = {
    itemDescription: itemDescription,
    title: title,
    photo: photoName,
    itemType: itemsType,
    member: member,
    offerList: offersList,
    lastOffer: offer
  }
  try {
    try {
      if (!itemsTypes.find((type) => type.itemType === itemTypeValue)) {
        await addNewItemsType(itemsType);
      }
    } catch (e) {

    }
    const idItem = await offerAnItem(item);
    await sendFile(idItem, "newItemFile");
    const message = "Ajout réussi!";
    await DonateAnItemPage();
    showError(message, "success", errorMessageOfferAnItemPage);
  } catch (error) {
    console.error(error);
    showError("Une erreur est survenue.", "danger",
        errorMessageOfferAnItemPage);
  }
}

export default DonateAnItemPage;