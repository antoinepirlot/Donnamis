import {
  addNewItemsType,
  getItemsTypes,
  offerAnItem
} from "../../../utils/BackEndRequests";
import {showError} from "../../../utils/ShowError";
import {getPayload} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

const htmlForm = `
  <div>
    <h1 class="display-3">Offrir un objet</h1>
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
      <input id="itemTypeFormList" list="itemsTypes" placeholder="Séléctionne le type d'objet"><br>
      <datalist id="itemsTypes"></datalist>
      <br>
      <input type="submit" value="Offrir">
    </form> <br><br>
    <form>
      <label>Select File</label>
      <input name="file" type= "file" /> <br/><br/>
      <button onclick="return sendFile()">Send</button>
    </form>
    
  </div>
  <div id="errorMessageOfferAnItemPage"></div>
`;

const sendFile = () => {
    console.log("TESSTTT-----------------")
    const fileInput = document.querySelector('input[name=file]');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    const options = {
        method: 'POST',
        body: formData
    };
    fetch('http://localhost:8080/upload/image', options);
    return false;
}


const OfferAnItemPage = async () => {
  const page = document.querySelector("#page");
  page.innerHTML = htmlForm;
  const offerItemForm = document.querySelector("#offerItemForm");
  await showItemsTypes();
  offerItemForm.addEventListener("submit", await offerItem);
  let itemsTypes;

  const DonateAnItemPage = async () => {
    if (!getPayload()) {
      Redirect("/");
      return;
    }
    const page = document.querySelector("#page");
    page.innerHTML = htmlForm;
    const offerItemForm = document.querySelector("#offerItemForm");
    itemsTypes = await getItemsTypes();
    showItemsTypes();
    offerItemForm.addEventListener("submit", await offerItem);
  };

  function showItemsTypes() {
    const itemsTypeList = document.querySelector("#itemsTypes");
    itemsTypeList.innerHTML = "";
    itemsTypes.forEach(itemsType => {
      itemsTypeList.innerHTML += `
      <option value="${itemsType.itemType}">
    `;
    });
  }

  async function offerItem(e) {
    const date = new Date();
    e.preventDefault();
    const errorMessageOfferAnItemPage = document.querySelector(
        "#errorMessageOfferAnItemPage");
    const title = document.querySelector("#titleForm").value;
    const itemDescription = document.querySelector("#itemDescriptionForm").value;
    let photo = document.querySelector("#photoForm").value;
    if (photo === "") {
      photo = null;
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
      photo: photo,
      itemType: itemsType,
      member: member,
      offerList: offersList,
      lastOfferDate: date
    }
    try {
      if (!itemsTypes.find((type) => type.itemType === itemTypeValue)) {
        await addNewItemsType(itemsType);
      }
      await offerAnItem(item);
      const message = "Ajout réussi!"
      showError(message, "success", errorMessageOfferAnItemPage);
    } catch (error) {
      console.error(error);
      showError("Une erreur est survenue.", "danger", errorMessageOfferAnItemPage);
    }
  }
}
export {OfferAnItemPage};