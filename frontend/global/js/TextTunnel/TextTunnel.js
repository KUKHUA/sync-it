export default class TextTunnel {
    #instanceList;
    #instance;

    constructor(instanceList) {
        if(Array.isArray(instanceList) && instanceList.length > 0) // exists, is an array, and has smth in it
            this.#instanceList = instanceList;

        else if (instanceList && !Array.isArray(instanceList)) // exists but not an array
            throw new Error("constructor: Provided instanceList is not an array.")

        else this.#instanceList = ["http://localhost:8000"];
    }

    streamChannel(channelName, callback, instance){
        if(channelName && typeof channelName !== "string" || channelName === "") // exists and is not a string
            throw new Error("streamChannel: Provided channelName is not a valid string.");

        if(!this.#instanceList || !this.#instanceList[0] || !Array.isArray(this.#instanceList)) 
            throw new Error("streamChannel: instanceList is not a valid array.")
        if(!instance) instance = this.#instanceList[0];

        if(instance && !(instance instanceof URL)){ // exists and not an url object.                                                      
            if(typeof instance === "string"){ // if its a string, try making it into a url object.
                try { 
                    instance = new URL(instance);
                } catch (err){
                    throw new Error("streamChannel: Provided instance is a string, but not a valid URL.");
                }
                
            } else {
                throw new Error("streamChannel: Provided instance is not a string or URL object.");
            }
        }

        if(typeof callback !== "function") 
            throw new Error("streamChannel: provided callback is not a function");

        // channelName, callback and instance have all been checked,
        // and from now on we can assume they are vaild.

        channelName = encodeURIComponent(channelName);
        const streamURL = new URL("/stream/channel/" + channelName, instance);

        const streamer = new EventSource(streamURL);
        streamer.onmessage = (data) => {
            callback(data);
        };
    }

    async sendChannel(channelName, data, instance){
        if(channelName && typeof channelName !== "string" || channelName === "") // exists and is not a string
            throw new Error("streamChannel: Provided channelName is not a valid string.");

        if(!this.#instanceList || !this.#instanceList[0] || !Array.isArray(this.#instanceList)) 
            throw new Error("streamChannel: instanceList is not a valid array.")
        if(!instance) instance = this.#instanceList[0];

        if(instance && !(instance instanceof URL)){ // exists and not an url object.                                                      
            if(typeof instance === "string"){ // if its a string, try making it into a url object.
                try { 
                    instance = new URL(instance);
                } catch (err){
                    throw new Error("streamChannel: Provided instance is a string, but not a valid URL.");
                }
                
            } else {
                throw new Error("streamChannel: Provided instance is not a string or URL object.");
            }
        }

        // channelName, callback and instance have all been checked,
        // and from now on we can assume they are vaild.

        channelName = encodeURIComponent(channelName);
        const sendURL = new URL("/send/channel/" + channelName, instance);

        const sendRequest = await fetch(sendURL, {
            method: "POST",
            body: data,
        })

        return sendRequest.json();
    }
}