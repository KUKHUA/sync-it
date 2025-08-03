import DarkModeSwitch from "./global/js/ui/DarkModeSwitch.js";
import StartSession from "./global/js/ui/StartSession.js";
import TextTunnel from "./global/js/TextTunnel/TextTunnel.js";

// Setup UI interactions.
new DarkModeSwitch("darkModeSwitch");
new StartSession("startButton", "qrCodeImage", "codeText");


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
