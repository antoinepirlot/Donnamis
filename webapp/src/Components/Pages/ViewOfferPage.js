
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
         <a id="interestButton" href="#" class="btn btn-primary">Je suis interessé(e) !</a>
      </div>
    </div>
    <!--<div class="col-md-4">
      <img src="" class="card-img" alt="JS">
    </div> -->
    
  </div>
</div>
`;

/**
 * Render the OfferPage :
 */
async function ViewOfferPage() {
  //get param from url
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  const page = document.querySelector("#page");
  page.innerHTML = viewOfferHtml;
  //get offer's infos with the id in param
  await getOffersInfo(urlParams.get("id"))
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
    date = date.getDate() + "/" + date.getMonth()+1 + "/" + date.getFullYear()

    document.querySelector("#title").innerHTML = offer.item.title
    document.querySelector("#offerer").innerHTML = `Offre proposée par : ${offer.member.firstName} ${offer.member.lastName} `
    document.querySelector("#type").innerHTML = `Type : ${offer.item.item_type}`
    document.querySelector("#description").innerHTML = `Description : ${offer.item.item_description}`
    document.querySelector("#availabilities").innerHTML = `Disponibilités : ${offer.time_slot}`
    document.querySelector("#pubDate").innerHTML = `Date de publication : ${date}`
  } catch (err) {
    console.error(err);
  }
}

export default ViewOfferPage;
