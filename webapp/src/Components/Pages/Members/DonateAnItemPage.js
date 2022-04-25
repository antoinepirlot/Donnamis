import {
  addNewItemsType,
  getItemsTypes,
  offerAnItem,
  sendPicture
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import {showItemsTypes} from "../../../utils/HtmlCode";

const htmlForm = `
  <div>
    <h1 class="display-3">Offrir un objet</h1>
    <div id="errorMessageOfferAnItemPage"></div>
    <form id="offerItemForm">
      Nom de l'objet<span id="asterisk">*</span>: <br>
      <textarea id="titleForm" cols="30" rows="3"></textarea><br>
      <br>
      Description de l'objet<span id="asterisk">*</span>:<br>
      <textarea id="itemDescriptionForm" cols="30" rows="3"></textarea><br>
      <br>
      
      <br>
      Disponibilités horaire<span id="asterisk">*</span>:<br>
      <textarea id="timeSlotForm" cols="30" rows="3"></textarea><br>
      <br>
      Type de l'objet<span id="asterisk">*</span>:<br>
      <input id="itemTypeFormList" list="itemsTypesDonateAnItemPage" placeholder="Séléctionne le type d'objet"><br>
      <datalist id="itemsTypesDonateAnItemPage"></datalist>
      <br>
      <label>Select File</label>
      <input id="photoName" name="file" type= "file" /> <br/><br/>
      <input type="submit" value="Offrir">
    </form>
    
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
  const date = new Date();
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
    timeSlot: timeSlot
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
    lastOfferDate: date
  }
  try {
    if (!itemsTypes.find((type) => type.itemType === itemTypeValue)) {
      await addNewItemsType(itemsType);
    }
    const idItem = await offerAnItem(item);
    await sendFile(idItem);
    const message = "Ajout réussi!";
    showError(message, "success", errorMessageOfferAnItemPage);
  } catch (error) {
    console.error(error);
    showError("Une erreur est survenue.", "danger",
        errorMessageOfferAnItemPage);
  }
}

async function sendFile(idItem) {
  const fileInput = document.querySelector('input[name=file]');
  const formData = new FormData();
  formData.append('file', fileInput.files[0]);
  try {
    await sendPicture(idItem, formData)
    showError("L'iùage à été uploadé.", "success", errorMessageOfferAnItemPage);
  } catch (e) {
    console.error(e);
    showError("Problème lors de l'upload de l'image.", "danger",
        errorMessageOfferAnItemPage);
  }
}

export default DonateAnItemPage;