import LoginPage from "../Pages/Public/LoginPage";
import Logout from "../Logout/Logout";
import ListMemberPage from "../Pages/Admins/ListMemberPage";
import AllItemsPage from "../Pages/Admins/AllItemsPage"
import RegisterPage from "../Pages/Public/RegisterPage";
import ViewItemPage from "../Pages/Members/ViewItemPage";
import DonateAnItemPage from "../Pages/Members/DonateAnItemPage";
import {MyItemsPage} from "../Pages/Members/MyItemsPage";
import ProfilePage from "../Pages/Members/ProfilePage";
import HomePage from "../Pages/Public/HomePage";
import MyGivenItems from "../Pages/Members/MyGivenItems"
import MyAssignedItems from "../Pages/Members/MyAssignedItems";
import MemberPage from "../Pages/Admins/MemberPage";
import SearchMembersPage from "../Pages/Admins/SearchMembersPage";

// Configure your routes here
const routes = {
  "/": HomePage,
  "/login": LoginPage,
  "/register": RegisterPage,
  "/logout": Logout,
  "/list_member": ListMemberPage,
  //"/latest_items": LatestItemsOffersPage,
  "/all_items": AllItemsPage,
  "/item": ViewItemPage,
  "/offer_item": DonateAnItemPage,
  "/my_items": MyItemsPage,
  "/profil": ProfilePage,
  "/given_items": MyGivenItems,
  "/assigned_items": MyAssignedItems,
  "/search_members": SearchMembersPage,
  "/member": MemberPage,
};

/**
 * Deal with call and auto-render of Functional Components following click events
 * on Navbar, Load / Refresh operations, Browser history operation (back or next) or redirections.
 * A Functional Component is responsible to auto-render itself : Pages, Header...
 */

const Router = () => {
  /* Manage click on the Navbar */
  let navbarWrapper = document.querySelector("#navbarWrapper");
  navbarWrapper.addEventListener("click", (e) => {
    // To get a data attribute through the dataset object, get the property by the part of the attribute name after data- (note that dashes are converted to camelCase).
    let uri = e.target.dataset.uri;

    if (uri) {
      e.preventDefault();
      /* use Web History API to add current page URL to the user's navigation history 
       & set right URL in the browser (instead of "#") */
      window.history.pushState({}, uri, window.location.origin + uri);
      /* render the requested component
      NB : for the components that include JS, we want to assure that the JS included 
      is not runned when the JS file is charged by the browser
      therefore, those components have to be either a function or a class*/
      const componentToRender = routes[uri];
      if (routes[uri]) {
        componentToRender();
      } else {
        throw Error("The " + uri + " ressource does not exist");
      }
    }
  });

  /* Route the right component when the page is loaded / refreshed */
  window.addEventListener("load", (e) => {
    const componentToRender = routes[window.location.pathname];
    if (!componentToRender) {
      throw Error(
          "The " + window.location.pathname + " ressource does not exist."
      );
    }

    componentToRender();
  });

  // Route the right component when the user use the browsing history
  window.addEventListener("popstate", () => {
    const componentToRender = routes[window.location.pathname];
    componentToRender();
  });
};

/**
 * Call and auto-render of Functional Components associated to the given URL
 * @param {*} uri - Provides an URL that is associated to a functional component in the
 * routes array of the Router
 */

const Redirect = (uri) => {
  // use Web History API to add current page URL to the user's navigation history & set right URL in the browser (instead of "#")
  window.history.pushState({}, uri, window.location.origin + uri);
  // render the requested component
  const componentToRender = routes[uri];
  if (routes[uri]) {
    componentToRender();
  } else {
    throw Error("The " + uri + " ressource does not exist");
  }
};

export {Router, Redirect};
