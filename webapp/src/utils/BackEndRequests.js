import {getObject, getPayload} from "./session";

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

export {
  getRegisteredMembers,
  getDeniedMembers,
  confirmMember,
  confirmAdmin,
    denyMember
};