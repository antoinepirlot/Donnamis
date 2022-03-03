import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";
import {disconnect} from "../../utils/session";

const Logout = () => {
  console.log("Logout");
  // clear the user session data from the localStorage
  disconnect();

  // re-render the navbar (for a non-authenticated user)
  Navbar();
  Redirect("/");
};

export default Logout;
