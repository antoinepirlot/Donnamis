/**
 * Get the Object that is in the localStorage or sessionStorage
 * under the storeName key.
 * @param {string} storeName
 * @returns the retrived object
 */
const getObject = (storeName) => {
  let retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject) {
    retrievedObject = sessionStorage.getItem(storeName);
  }
  if (!retrievedObject) {
    return;
  }
  return JSON.parse(retrievedObject);
};

/**
 * Get the token payload from the local or session storage.
 * @returns the payload object
 */
const getPayload = async () => {
  let token = localStorage.getItem("token");
  if (!token) {
    token = sessionStorage.getItem("token");
  }
  if (!token) {
    return;
  }
  let payload = token.split('.')[1];
  console.log("Payload : " + payload)
  console.log("token : " + token)
  payload = JSON.parse(window.atob(payload));
  // we divided by 1000 because jwt token does contains only 10 digit and 13 for Date.now()
  if(Date.now() / 1000 >= payload.exp){
    console.log("TOKEN EXPIRED")
    await refreshToken(payload.username)
    await getPayload();
  }
  return payload;
};
// eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlkIjoxLCJleHAiOjE2NDY1MjM5MjIsInVzZXJuYW1lIjoibmlrZXNha291In0.eErD7SA2kVpFoGtJt56CtmFAIybtu1GTdOt15HgOe0o
const refreshToken = async (username) => {
  const request = {
    method : "POST",
    headers: {
      "Content-Type":
          "application/json"
    },
    body: JSON.stringify({
      username: username
    })
  };
  try{
    const response = await fetch("api/members/refreshToken", request);
    if(!response.ok){
      throw new Error("Problème lors du refresh du token");
    }
    const token = await response.json();
    if(localStorage.getItem("token")){
      setLocalObject("token", JSON.stringify(token));
    }else{
      setSessionObject("token", JSON.stringify(token));
    }
  }catch (e){
    console.error(e);
  }
}

/**
 * Set the object in the sessionObject under the storeName key.
 * When the session ends, this data is removed.
 * @param {string} storeName
 * @param {Object} object
 */
const setSessionObject = (storeName, object) => {
  const storageValue = JSON.stringify(object);
  sessionStorage.setItem(storeName, storageValue);
};

/**
 * Set the object in the localStorage under the storeName key.
 * Even if the session is ended, datas stay on localstorage
 * @param {string} storeName
 * @param {Object} object
 */
const setLocalObject = (storeName, object) => {
  const storageValue = JSON.stringify(object);
  localStorage.setItem(storeName, storageValue);
};

/**
 * Remove the object in the localStorage under the storeName key
 */
const disconnect = () => {
  localStorage.clear();
  sessionStorage.clear();
};

export {getObject, getPayload, setSessionObject, setLocalObject, disconnect};