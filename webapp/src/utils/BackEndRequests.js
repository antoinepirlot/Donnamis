import {getObject, getPayload} from "./session";
import {showError} from "./ShowError";
import {Redirect} from "../Components/Router/Router";

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
    if (response.status === 404) {
      return false;
    }
    throw new Error("Error while login member.");
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

async function isAdmin() {
  const request = {
    method: "HEAD",
    headers: {
      "Authorization": getObject("token")
    }
  }
  const response = await fetch("/api/members/is_admin", request);
  return response.ok;
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

async function confirmAdmin(id) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const url = `/api/members/confirmAdmin/${id}`;
  const response = await fetch(url, request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function denyMember(refusal) {
  try {
    const request = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": getObject("token")
      },
      body: JSON.stringify(refusal)
    };
    const reponse = await fetch("/api/members/deny", request);
    if (!reponse.ok) {
      throw new Error(
          "fetch error : " + reponse.status + " : " + reponse.statusText
      );
    }
  } catch (error) {
    console.error("ListMemberPage::error::deny registration:", error);
  }
}

async function getAllItemsByOfferStatus(offerStatus) {
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
  console.log(url)
  const response = await fetch(url, request);
  if (!response.ok) {
    if (response.status === 401) {
      Redirect("/logout");
    }
    throw new Error("Erreur lors du fetch");
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
  return await response.json();
}

async function getAllOffers() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("/api/offers/all_offers", request);
  if (!response.ok) {
    if (response.status === 401) {
      Redirect("/logout");
    }
    // status code was not 200, error status code
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
}

async function getLatestOffers() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("/api/offers/latest_offers", request);
  if (!response.ok) {
    // status code was not 200, error status code
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
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
    throw new Error("Error while adding a new items type.");
  }
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
    showError("Can't offer the item");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return response.ok;
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
  return await response.json();
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

/**
 * Ask backend to mark an interest for an item.
 * @returns {Promise<boolean>} true if the request has been done otherwise false
 */
async function postInterest(interest, interestMessage) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(interest)
  };
  const response = await fetch("api/interests", request);
  console.table(response);
  if (response.ok) {
    showError(
        "Votre intérêt pour cet article à été bien été enregistré.",
        "success", interestMessage);
  } else if (response.status === 409) {
    showError("Vous avez déjà mis une marque d'intérêt pour cette offre",
        "danger", interestMessage);
  } else if (response.status === 403) {
    showError(
        "Votre numero de téléphone n'est pas renseigné, veuillez l'ajouter si vous désirez être appelé.",
        "danger", interestMessage);
  }
  return response.ok;
}

async function chooseRecipient(recipient) {
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
  }
}

export {
  login,
  getRefusal,
  me,
  register,
  isAdmin,
  getAllMembers,
  getInterestedMembers,
  getOneMember,
  confirmInscription,
  confirmAdmin,
  denyMember,
  getAllItemsByOfferStatus,
  getAssignedItems,
  getAllOffers,
  getLatestOffers,
  getMyItems,
  markItemAs,
  cancelOffer,
  getItem,
  getItemsTypes,
  addNewItemsType,
  offerAnItem,
  offerAgain,
  postInterest,
  modifyMember,
  chooseRecipient
};