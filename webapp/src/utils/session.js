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
const getPayload = () => {
  let payload = localStorage.getItem("token");
  if (!payload) {
    payload = sessionStorage.getItem("token");
  }
  if (!payload) {
    return;
  }
  payload = payload.split('.')[1];
  return JSON.parse(window.atob(payload));
};

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