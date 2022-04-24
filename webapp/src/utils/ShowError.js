function showError(message, alertType, element) {
  element.innerHTML = `
    <p class="alert-${alertType}">${message}</p>
  `;
}

export {showError};