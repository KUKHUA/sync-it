import DarkModeSwitch from "./global/js/DarkModeSwitch.js";
import TextTunnel from "./global/js/TextTunnel/TextTunnel.js";
const darkMode = new DarkModeSwitch(document.getElementById("darkModeSwitch"));

const eventSource = new EventSource("http://127.0.0.1:1337");
eventSource.onmessage = function (event) {
    window.location.reload();
};

function log(data){
    console.log(data);
}

document.addEventListener("DOMContentLoaded", () => {
    window.tun = new TextTunnel(); 
    tun.streamChannel("blah", log);
});
