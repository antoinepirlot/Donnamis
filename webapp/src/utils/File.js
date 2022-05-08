import {sendPicture} from "./BackEndRequests";

async function sendFile(idItem, intputName) {
  const fileInput = document.querySelector(`input[name=${intputName}]`);
  const formData = new FormData();
  formData.append('file', fileInput.files[0]);
  try {
    await sendPicture(idItem, formData)
  } catch (e) {
    console.error(e);
  }
}

export {sendFile}