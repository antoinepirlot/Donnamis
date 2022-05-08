import {getObject, getPayload} from "./session";
import {showError} from "./ShowError";
import {Redirect} from "../Components/Router/Router";

async function addNewItemsType(itemsType) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(itemsType)
  };
  const response = await fetch("/api/items_types", request);
  if (!response.ok) {
    if (response.status === 409) {//Conflict
      return;
    }
    throw new Error("Error while adding a new items type.");
  }
}

async function cancelOffer(id) {
  const request = {
    method: "DELETE",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const url = `/api/items/${id}`;
  const response = await fetch(url, request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function chooseRecipient(recipient, recipientMessage) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(recipient)
  };
  const response = await fetch("/api/recipients", request);
  if (!response.ok) {
    throw new Error("Error while fetching");
  } else if (response.status === 409) {
    showError("Ce membre ne peut pas recevoir d'objet pour le moment",
        recipientMessage);
    console.log("ERREUR MEMBRE");
  }
}

async function confirmInscription(member) {
  const request = {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": getObject("token")
    },
    body: JSON.stringify(member)
  };
  const response = await fetch("/api/members/confirm", request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function denyMember(refusal) {
  const request = {
    method: "PUT", headers: {
      "Content-Type": "application/json", "Authorization": getObject("token")
    }, body: JSON.stringify(refusal)
  };
  const response = await fetch("/api/members/deny", request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText);
  }
}

async function evaluateItemBackEnd(rating, ratingMessage) {
  const request = {
    method: "POST", headers: {
      "Authorization": getObject("token"), "Content-Type": "application/json"
    }, body: JSON.stringify(rating)
  };
  const response = await fetch("api/ratings", request);

  if (response.ok) {
    showError(
        "Evaluation enregistrée",
        "success", ratingMessage
    );
  } else if (response.status === 409) {
    showError("Vous ne pouvez évaluer une offre qu'une seule fois", "danger",
        ratingMessage);
  } else if (response.status === 400) {
    showError("Information manquante", "danger", ratingMessage);
  }
  return response.ok;
}

async function getAllItems(offerStatus) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  let url = "/api/items/all_items";
  if (offerStatus) {
    url += "/" + offerStatus;
  }
  const response = await fetch(url, request);
  if (!response.ok) {
    if (response.status === 401) {
      Redirect("/logout");
    }
    throw new Error("Erreur lors du fetch");
  }
  return await response.json();
}

async function getAllItemsByMemberIdAndOfferStatus(idMember, offerStatus) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/items/${idMember}/${offerStatus}`,
      request);
  if (!response.ok) {
    throw new Error("Error while fetching member's items");
  }
  if (response.status === 204) {
    return;
  }
  return await response.json();
}

/**
 * Get all members from the database
 * @returns all members
 */
async function getAllMembers() {
  const options = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };

  const response = await fetch("/api/members", options);
  if (!response.ok) {
    // status code was not 200, error status code
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
}

async function getAllPublicItems() {
  const request = {
    method: "GET"
  };
  const response = await fetch("/api/items/all_items/public", request);
  if (!response.ok) {
    throw new Error("Erreur lors du fetch");
  }
  return await response.json();
}

async function getAllRatings() {
  const request = {
    method: "GET", headers: {
      "Authorization": getObject("token"), "Content-Type": "application/json"
    }
  };
  const response = await fetch(`/api/ratings/all/${getPayload().id}`, request);
  if (!response.ok) {
    if (response.status === 404) {
      return;
    }
    throw new Error("Error while getting all ratings of a member");
  }
  return await response.json();
}

async function getAssignedItems() {
  const idMember = getPayload().id;
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/items/assigned_items/${idMember}`,
      request);
  if (!response.ok) {
    throw new Error("Erreur lors du fetch");
  }
  if (response.status === 204) {
    return;
  }
  return await response.json();
}

async function getGivenItems() {
  const idMember = getPayload().id;
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/items/given_items/${idMember}`,
      request);
  if (!response.ok) {
    throw new Error("Erreur lors du fetch");
  }
  if (response.status === 204) {
    return;
  }
  return await response.json();
}

async function getInterestedMembers(idOffer) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/members/interested/${idOffer}`, request);
  if (!response.ok) {
    throw new Error("Error while fetching interested members.");
  }
  return response.status === 200 ? await response.json() : null;
}

async function getItem(idItem) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/items/${idItem}`, request);
  if (!response.ok) {
    // status code was not 200, error status code
    showError("There's no offer with this id");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json()
}

async function getItemsTypes() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("/api/items_types/all", request);
  if (!response.ok) {
    showError("There's no items type");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
}

async function getMyItems() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const idMember = getPayload().id;
  const response = await fetch(`/api/items/member_items/${idMember}`, request);
  if (!response.ok) {
    // status code was not 200, error status code
    showError("There's no offer with this id");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json()
}

async function getNumberOfInterestedMembers(idItem) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(
      `/api/items/count_interested_members/${idItem}/`, request);
  if (!response.ok) {
    throw new Error(
        "Error while fetching count of received or not received items of member: "
        + idItem);
  }
  return await response.json();
}

async function getNumberOfItems(idMember, offerStatus) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/items/count/${idMember}/${offerStatus}`,
      request);
  if (!response.ok) {
    throw new Error("Error while fetching the number of items.");
  }
  return await response.json();
}

async function getNumberOfReceivedOrNotReceivedItems(idMember, received) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(
      `/api/items/count_assigned_items/${idMember}/${received}`, request);
  if (!response.ok) {
    throw new Error(
        "Error while fetching count of received or not received items of member: "
        + idMember);
  }
  return await response.json();
}

async function getOneMember(idMember) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/members/${idMember}`, request);
  if (!response.ok) {
    throw new Error("Error while fetching one user.");
  }
  return await response.json();
}

async function getRefusal(username) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/refusals/${username}`, request);
  if (!response.ok) {
    if (response.status === 404) {
      return false;
    }
    throw new Error("Error while fetching refusal message.");
  }
  return await response.json();
}

async function login(username, password) {
  const request = {
    method: "POST",
    headers: {
      "Content-Type":
          "application/json"
    },
    body: JSON.stringify({
      username: username,
      password: password
    })
  };
  const response = await fetch("api/members/login", request);
  if (!response.ok) {
    if (response.status === 403) {
      return 403;
    }
    if (response.status === 404) {
      return false;
    }
    throw new Error("Error while login member.");
  }
  return await response.json();
}

/**
 * Fetch to "items" to mark item as given or not given
 * @param given {boolean} true if it marks item as given or false to not given
 * @param item the item to update
 */
async function markItemAs(given, item) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(item)
  };
  let url;
  if (given) {
    url = "/api/items/given";
  } else {
    url = "/api/items/not_given"
  }
  const response = await fetch(url, request);
  if (!response.ok) {
    throw new Error("Error while updating item's offer status");
  }
}

async function me() {
  const request = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("api/members/me", request);
  if (!response.ok) {
    Redirect("/logout");
    throw new Error("Problème lors du rafraichissement du token");
  }
  return await response.json();
}

async function modifyMember(member) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(member)
  };
  const response = await fetch("/api/members/modify", request);
  if (!response.ok) {
    showError("Impossible de modifier le profil");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function modifyTheItem(item) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(item)
  };
  const response = await fetch("/api/items/modify", request);
  if (!response.ok) {
    showError("Impossible de modifier l'objet");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return response.ok;
}

async function offerAgain(offer) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(offer)
  };
  const response = await fetch(`api/offers/add_offer`, request);
  return response.ok;
}

async function offerAnItem(item) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(item)
  };
  const response = await fetch("/api/items", request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
}

/**
 * Ask backend to mark an interest for an item.
 * @returns {Promise<number>} true if the request has been done otherwise false
 */
async function postInterest(interest) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(interest)
  };
  const response = await fetch("api/interests", request);

  if (!response.ok) {
    return response.status;
  }
}

async function sendPicture(idItem, formData) {
  const request = {
    method: 'POST',
    headers: {
      "Authorization": getObject("token")
    },
    body: formData
  };
  const response = await fetch(`/api/images/upload/${idItem}`, request);
  if (!response.ok) {
    throw new Error("Error while uploading an image.");
  }
}

async function setMemberAvailability(member) {
  const request = {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(member)
  };
  const response = await fetch("api/members/availability", request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function setRecipientUnavailable(recipient) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(recipient)
  };
  const response = await fetch("api/recipients/unavailable", request);

  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText);
  }
}

async function register(member) {
  const request = {
    method: "POST",
    headers: {
      "Content-Type":
          "application/json"
    },
    body: JSON.stringify(member)
  };
  const response = await fetch("api/members/register", request);
  const registerMessage = document.querySelector("#registerMessage");
  if (!response.ok) {
    showError("Echec de l'inscription", "danger", registerMessage);
    return;
  }
  showError(
      "Votre inscription à bien été prise en compte. Veuillez patienter la validation de votre compte.",
      "success", registerMessage)
}

export {
  addNewItemsType,
  cancelOffer,
  chooseRecipient,
  confirmInscription,
  denyMember,
  evaluateItemBackEnd,
  getAllItems,
  getAllItemsByMemberIdAndOfferStatus,
  getAllMembers,
  getAllPublicItems,
  getAllRatings,
  getAssignedItems,
  getGivenItems,
  getInterestedMembers,
  getItem,
  getItemsTypes,
  getMyItems,
  getNumberOfInterestedMembers,
  getNumberOfItems,
  getNumberOfReceivedOrNotReceivedItems,
  getOneMember,
  getRefusal,
  login,
  markItemAs,
  me,
  modifyMember,
  modifyTheItem,
  offerAgain,
  offerAnItem,
  postInterest,
  sendPicture,
  setMemberAvailability,
  setRecipientUnavailable,
  register,
};