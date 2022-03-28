/**
 * Get the Object that is in the localStorage or sessionStorage
 * under the storeName key.
 * @param {string} storeName
 * @returns the retrived object
 */
function getObject(storeName) {
  let retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject) {
    retrievedObject = sessionStorage.getItem(storeName);
  }
  if (!retrievedObject) {
    return;
  }
  return JSON.parse(retrievedObject);
}

/**
 * Get the token payload from the local or session storage.
 * @returns the payload object
 */
async function getPayload() {
  let token = localStorage.getItem("token");
  if (!token) {
    token = sessionStorage.getItem("token");
  }
  if (!token) {
    return;
  }
  let payload = token.split('.')[1];
  payload = JSON.parse(window.atob(payload));
  // we divided by 1000 because jwt token does contains only 10 digit and 13 for Date.now()
  return payload;
}

/**
 * Set the object in the sessionObject under the storeName key.
 * When the session ends, this data is removed.
 * @param {string} storeName
 * @param {Object} object
 */
function setSessionObject(storeName, object) {
  const storageValue = JSON.stringify(object);
  sessionStorage.setItem(storeName, storageValue);
}

/**
 * Set the object in the localStorage under the storeName key.
 * Even if the session is ended, datas stay on localstorage
 * @param {string} storeName
 * @param {Object} object
 */
function setLocalObject(storeName, object) {
  const storageValue = JSON.stringify(object);
  localStorage.setItem(storeName, storageValue);
}

/**
 * Remove the local object
 * @param storeName the path to the object
 * @returns {string} the removed object
 */
function removeLocalObject(storeName) {
  const removedObject = localStorage.getItem(storeName);
  localStorage.removeItem(storeName);
  return removedObject;
}

/**
 * Remove the session object
 * @param storeName the path to the object
 * @returns {string} the removed object
 */
function removeSessionObject(storeName) {
  const removedObject = sessionStorage.getItem(storeName);
  sessionStorage.removeItem(storeName);
  return removedObject;
}

/**
 * Remove the object in the localStorage under the storeName key
 */
function disconnect() {
  localStorage.clear();
  sessionStorage.clear();
}

export {
  getObject,
  getPayload,
  setSessionObject,
  setLocalObject,
  disconnect,
  removeLocalObject,
  removeSessionObject
};