import {getProfile} from "../../utils/BackEndRequests";
import {getPayload,} from "../../utils/session";
import {Redirect} from "../Router/Router";

const viewProfileHtml = `
  <div class="bg-info d-inline-flex d-flex flex-column rounded w-50 p-3">
    <h2>Mon profile</h2>
    <div id="username"></div>
    <div id="name"></div>
    <div id="phone"></div>
    <div id="adress"></div>
  </div>
`;

async function ProfilePage() {
  if (!await getPayload()) {
    Redirect("/");
    return;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML += viewProfileHtml;

  try {
    const payload = await getPayload();

    const profile = await getProfile(payload.id);
    const address = profile.address;

    document.querySelector("#username").innerHTML += profile.username;
    document.querySelector("#name").innerHTML += profile.lastName + " "
        + profile.firstName;
    const phone = profile.phoneNumber;
    if (phone == null) {
      document.querySelector("#phone").innerHTML += "Aucun numéro de téléphone";
    } else {
      document.querySelector("#phone").innerHTML += phone;
    }
    document.querySelector("#adress").innerHTML += address.street + " ";
    document.querySelector("#adress").innerHTML += address.buildingNumber + " ";
    document.querySelector("#adress").innerHTML += address.postcode + " ";
    document.querySelector("#adress").innerHTML += address.commune + " ";
  } catch (err) {
    console.error(err);
  }
}

export default ProfilePage;