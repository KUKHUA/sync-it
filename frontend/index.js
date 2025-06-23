import DarkModeSwitch from "/global/js/DarkModeSwitch.js";
const darkMode = new DarkModeSwitch(document.getElementById("darkModeSwitch"));

const eventSource = new EventSource("http://127.0.0.1:1337");
eventSource.onmessage = function (event) {
    window.location.reload();
};
