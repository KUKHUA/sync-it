export default class StartSession {
    #startButton;
    #qrCodeImage;
    #codeText;
    #charset = "ABCDEFGHIJKMOPQRSTUWXYZabcdefghijkmopqstuwxyz0123456789!@";

    constructor(startButton, qrCodeImage, codeText){
        if(typeof startButton === "string")
            startButton = document.getElementById(startButton);

        if(typeof qrCodeImage === "string")
            qrCodeImage = document.getElementById(qrCodeImage);

        if(typeof codeText === "string")
            codeText = document.getElementById(codeText);

        if(!startButton || !qrCodeImage || !codeText)
            throw new Error("constructor: one of the elements provided does not exist.");

        this.#startButton = startButton;
        this.#qrCodeImage = qrCodeImage;
        this.#codeText = codeText;

        this.#startButton.addEventListener("click", this.#startPairing.bind(this));
    }

    #startPairing(){
        const array = new Uint8Array(8);
        crypto.getRandomValues(array);

        const pairID = Array.from(array, byte => this.#charset[byte % this.#charset.length]).join('');

        this.#codeText.textContent = pairID;

        QRCode.toDataURL(pairID, {errorCorrectionLevel: "H", version: 5}).then(dataUrl => {
            this.#qrCodeImage.src = dataUrl;
        });
    }
}