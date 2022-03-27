const htmlForm = `
  <div>
    <form id="offerItemForm">
      Nom de l'objet<span id="asterisk">*</span>: <br>
      <textarea id="titleForm" cols="30" rows="3"></textarea><br>
      <br>
      Description de l'objet<span id="asterisk">*</span>:<br>
      <textarea id="itemDescriptionForm" cols="30" rows="3"></textarea><br>
      <br>
      Image:<br>
      <input id="photoForm" type="image"><br>
      <br>
      Disponibilités horaire<span id="asterisk">*</span>:<br>
      <textarea id="timeSlotForm" cols="30" rows="3"></textarea><br>
      <br>
      Type de l'objet<span id="asterisk">*</span>:<br>
      <datalist id="itemTypeForm"></datalist>
      <br>
      <input type="submit" value="Offrir">
    </form>
  </div>
`;

const OfferAnItemPage = async () => {
  const page = document.querySelector("#page");
  page.innerHTML = htmlForm;
  const offerItemForm = document.querySelector("#offerItemForm");
  await showItemsTypes();
  offerItemForm.addEventListener("submit", await offerItem);
};

async function showItemsTypes() {

}

async function offerItem(e) {
  e.preventDefault();
  const title = document.querySelector("#titleForm").value;
  const itemDescription = document.querySelector("#itemDescriptionForm").value;
  const photo = document.querySelector("#photoForm").value;
  const timeSlot = document.querySelector("#timeSlotForm").value;
  const itemType = document.querySelector("#itemTypeForm").value;
}


export {OfferAnItemPage};