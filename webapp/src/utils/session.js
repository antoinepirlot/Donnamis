const STORE_NAME = "member";

/**
 * Get the Object that is in the localStorage under the storeName key
 * @param {string} storeName
 * @returns
 */
const getSessionObject = (storeName) => {
  const retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject)
    return;
  return JSON.parse(retrievedObject);
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
 * @param {String} storeName
 */
const removeSessionObject = (storeName) => {
  localStorage.removeItem(storeName);
};

export { getSessionObject, setSessionObject, setLocalObject, removeSessionObject };