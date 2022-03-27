import {getObject, getPayload} from "./session";
import {showError} from "./ShowError";

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
    showError("Aucun utilisateur pour ce username et ce mot de passe",
        "danger", loginMessage)
    return;
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
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  }
  const response = await fetch("/api/members/is_admin/", request);
  if (response.status === 200) {
    console.error("Needed a /me request");
  }
  return response.ok;
}

async function getRegisteredMembers() {
  try {
    const options = {
      method: "GET",
      headers: {
        "Authorization": getObject("token")
      }
    };

    const response = await fetch("/api/members/list_registered", options);
    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    return await response.json();
  } catch (error) {
    console.error("ListMemberPage::error: ", error);
  }
}

async function getDeniedMembers() {
  const options = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  try {
    //Membres en attente d'acceptation
    const response = await fetch("/api/members/list_denied", options);
    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    return await response.json();
  } catch (error) {
    console.error("ListMemberPage::error: ", error);
  }
}

async function confirmMember(id) {
  const request = {
    method: "PUT",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const url = `/api/members/confirm/${id}`;
  const response = await fetch(url, request);
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
  const payload = await getPayload();
  const url = `/api/members/confirmAdmin/${id}`;
  const response = await fetch(url, request);
  if (!response.ok) {
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
}

async function denyMember(id) {
  try {
    const request = {
      method: "PUT",
      headers: {
        "Authorization": getObject("token")
      }
    };
    const url = `/api/members/denies/${id}`;
    const reponse = await fetch(url, request);
    if (!reponse.ok) {
      throw new Error(
          "fetch error : " + reponse.status + " : " + reponse.statusText
      );
    }
  } catch (error) {
    console.error("ListMemberPage::error::deny registration:", error);
  }
}

async function getItems() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("/api/items/all_offered_items", request);
  if (!response.ok) {
    throw new Error("Erreur lors du fetch");
  }
  return await response.json();
}

async function getOffers() {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch("/api/offers/all_offers", request);
  if (!response.ok) {
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

async function getOffer(idOffer) {
  const request = {
    method: "GET",
    headers: {
      "Authorization": getObject("token")
    }
  };
  const response = await fetch(`/api/offers/${idOffer}`, request);
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

async function offerAnItem(item) {
  const request = {
    method: "POST",
    headers: {
      "Authorization": getObject("token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify(item)
  };
  const response = await fetch("/api/items/offer", request);
  if (!response.ok) {
    showError("Can't offer the item");
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  return await response.json();
}

export {
  login,
  register,
  isAdmin,
  getRegisteredMembers,
  getDeniedMembers,
  confirmMember,
  confirmAdmin,
  denyMember,
  getItems,
  getOffers,
  getLatestOffers,
  getOffer,
  getItemsTypes,
  offerAnItem
};