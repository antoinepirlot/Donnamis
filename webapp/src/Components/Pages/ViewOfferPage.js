import {
  getPayload,
} from "../../utils/session";
import {Redirect} from "../Router/Router";
import {showError} from "../../utils/ShowError";

const viewOfferHtml = `
<div id="offerCard" class="card mb-3">
  <div class="row no-gutters">
  <div class="col-md">
      <div class="card-body">
        <h2 id="title" class="card-title"></h2>
        <p id="offerer" class="text-muted"> </p>
        <h5 id="type" class="card-text"></h5>
        <h5 id="description" class="card-text"></h5>
        <h5 id="availabilities" class="card-text"></h5>
        <h5 id="pubDate" class="card-text"></h5>

        <form>
                <div class="form-check form-switch">
         <input class="form-check-input" type="checkbox" id="callWanted">
         <label class="form-check-label" for="callWanted">J'accepte d'être appelé</label>
        </div>
               <input id="interestButton" type="submit" class="btn btn-primary" value="Je suis interessé(e) !">
       </form>

       <div class="message" id="interestMessage"></div>
      </div>
    </div>
    <div class="col-md-4">
      <img src="" class="card-img" alt="JS">
    </div>
  </div>
</div>
`;

/**
 * Render the OfferPage :
 */
async function ViewOfferPage() {
  if (!await getPayload()) {
    Redirect("/");
    return;
  }
  //get param from url
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const page = document.querySelector("#page");
  const offerId = urlParams.get("id")

  page.innerHTML = viewOfferHtml;
  const button = document.querySelector("#offerCard");
  //get offer's infos with the id in param
  await getOffersInfo(offerId)
  //post an interest
  button.addEventListener("submit", postInterest);

}

async function getOffersInfo(idOffer) {
  try {

    const response = await fetch(`/api/offers/${idOffer}`);
    if (!response.ok) {
      // status code was not 200, error status code
      //TODO say that there are no offer for this id
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const offer = await response.json()
    var date = new Date(offer.date);
    date = date.getDate() + "/" + (date.getMonth() + 1) + "/"
        + date.getFullYear();

    document.querySelector("#title").innerHTML = offer.item.title
    document.querySelector(
        "#offerer").innerHTML = `Offre proposée par : ${offer.item.member.firstName} ${offer.item.member.lastName} `
    document.querySelector(
        "#type").innerHTML = `Type : ${offer.item.itemType.itemType}`
    document.querySelector(
        "#description").innerHTML = `Description : ${offer.item.itemDescription}`
    document.querySelector(
        "#availabilities").innerHTML = `Disponibilités : ${offer.timeSlot}`
    document.querySelector(
        "#pubDate").innerHTML = `Date de publication : ${date}`
  } catch (err) {
    console.error(err);
  }
}

async function postInterest(e) {
  console.log("postInterest");
  e.preventDefault();
  const payload = await getPayload();
  const memberId = payload.id;
  console.log("id= " + memberId);
  const interestMessage = document.querySelector("#interestMessage");
  //get param from url
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const offerId = urlParams.get("id");
  try {
    const request = {
      method: "POST",
      headers: {
        "Content-Type":
            "application/json"
      },
      body: JSON.stringify(
          {
            "idMember": memberId
          })
    };
    const callWanted = document.querySelector("#callWanted");
    let response = null;
    if(callWanted.checked){
      response = await fetch(`api/interests/${offerId}?call_wanted=true`, request);
    }else{
      response = await fetch(`api/interests/${offerId}`, request);
    }
    if (response.ok) {
      showError(
          "Votre intérêt pour cet article à été bien été enregistré.",
          "success", interestMessage);
    } else if (response.status === 409) {
      showError("Vous avez déjà mis une marque d'intérêt pour cette offre",
          "danger", interestMessage);
    } else if (response.status === 403) {
      showError("Votre numero de téléphone n'est pas renseigné, veuillez l'ajouter si vous désirez être appelé.",
          "danger", interestMessage);
    }
  } catch (err) {
    showError("Votre marque d'intérêt n'a pas pu être ajoutée", "danger",
        interestMessage);
    console.error(err);
  }

}

export default ViewOfferPage;
