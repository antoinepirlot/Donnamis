/**
 * Make modal alive.
 * @param modalId the modal id in html (must start with "#")
 * @param closeModalId the span close modal id in html (must start with "#")
 */
function openModal(modalId, closeModalId) {
  if (modalId[0] !== "#" || closeModalId[0] !== "#") {
    throw new Error(`The modalId and/or closeModalId must start with "#"`);
  }
  const modal = document.querySelector(modalId);
  modal.style.display = "block";
  setCloseModalEvent(modal, closeModalId);
}

/**
 * Create the close event on modal.
 *
 * @param modal the modal to close
 * @param elementId the id of modal span (must start with "#")
 */
function setCloseModalEvent(modal, elementId) {
  const closeModalButton = document.querySelector(elementId);
  closeModalButton.addEventListener("click", () => {
    modal.style.display = "none";
  });
}

/**
 * Close the modal.
 * @param modalId the modal id in html (must start with "#")
 */
function closeModal(modalId) {
  const modal = document.querySelector(modalId);
  modal.style.display = "none";
}

export {
  openModal,
  closeModal
};