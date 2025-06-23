import ui from "/lib/js/beer.min.js";
export default class DarkModeSwitch {
  #checkBoxElement;
  constructor(checkBoxElement) {
    this.#checkBoxElement = checkBoxElement;
    if (localStorage["darkMode"]) ui("mode", localStorage["darkMode"]);
    // Set the initial state of the checkbox based on the current mode
    this.#checkBoxElement.checked = ui("mode") === "dark";

    this.#checkBoxElement.addEventListener("change", this.#onChange.bind(this));
  }

  #onChange() {
    const changeTo = !(ui("mode") === "dark");
    this.#checkBoxElement.checked = changeTo;
    ui("mode", changeTo ? "dark" : "light");
    localStorage["darkMode"] = changeTo ? "dark" : "light";
  }
}
