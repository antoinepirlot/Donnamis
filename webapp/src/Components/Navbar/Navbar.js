// When using Bootstrap to style components, the CSS is imported in index.js
// However, the JS has still to be loaded for each Bootstrap's component that needs it.
// Here, because our JS component 'Navbar' has the same name as Navbar Bootstrap's component
// we change the name of the imported Bootstrap's 'Navbar' component

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

import {getObject} from "../../utils/session";

const navBarHtml = `
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Donnamis</a>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul id="navbarLinks" class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link" aria-current="page" href="#" data-uri="/">Home</a>
          </li>
        </ul>
      </div>
    </div>
    <div id="usernameNavbar"></div>
  </nav>
`;

const loginLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/login">Login</a>
          </li>
`;

const registerLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/register">Register</a>
          </li>
`;

const logoutLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/logout">Logout</a>
          </li>
`;

const listMemberLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/list_member">List Member</a>
          </li>
`;

const latestItemsLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/latest_items">Latest Items</a>
          </li>
`;

const allItemsLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/all_items">All Items</a>
          </li>
`;

const allOfferedItemsLinkHtml = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/all_offered_items">All Offered Items</a>
          </li>
`;

const offersDetail = `
          <li class="nav-item">
            <a class="nav-link" href="#" data-uri="/offer?id=1">Offer detail</a>
          </li>
`;

const Navbar = async () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  navbarWrapper.innerHTML = navBarHtml;
  const memberDTO = getObject("memberDTO");
  const links = document.querySelector("#navbarLinks");
  if (memberDTO) {
    links.innerHTML += listMemberLinkHtml;
    document.querySelector("#usernameNavbar").innerHTML = memberDTO.username
    links.innerHTML += latestItemsLinkHtml;
    links.innerHTML += allItemsLinkHtml;
    links.innerHTML += allOfferedItemsLinkHtml;
    links.innerHTML += offersDetail;
    links.innerHTML += logoutLinkHtml;
  } else {
    links.innerHTML += loginLinkHtml;
    links.innerHTML += registerLinkHtml;
  }
};

export default Navbar;
