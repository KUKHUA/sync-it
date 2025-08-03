import ui from "/lib/js/beer.min.js";

export default class DarkModeSwitch {
  #switchElement;

  constructor(switchElement) {
    if(typeof switchElement === "string")
        switchElement = document.getElementById(switchElement);

    if(!switchElement) 
      throw new Error("constructor: provided switchElemnet is not a vaild html element.");
    
    this.#switchElement = switchElement;
    if (localStorage["darkMode"]) ui("mode", localStorage["darkMode"]);
    
    this.#switchElement.checked = ui("mode") === "dark";

    this.#switchElement.addEventListener("change", this.#onChange.bind(this));
  }

  #onChange() {
    const changeTo = !(ui("mode") === "dark");
    this.#switchElement.checked = changeTo;
    ui("mode", changeTo ? "dark" : "light");
    localStorage["darkMode"] = changeTo ? "dark" : "light";
  }
}
