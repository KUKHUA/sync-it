import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONException;

import http.Middleware;
import http.ClientStore;

import java.net.URI;
import java.util.List;

public final class SSESend implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange){
            Headers headers = exchange.getResponseHeaders();
            String path = exchange.getRequestURI().getPath();
            String prefix = "/send/channel/";
            String channel = "";

            if(path.startsWith(prefix)){
                channel = path.substring(prefix.length());

                if(channel.contains("/")){
                    channel = channel.replace("/", "");
                }
            }

            if(channel.length() > 512){
                JSONObject outerObject = new JSONObject();

                JSONObject dataObject = new JSONObject();
                dataObject.put("errorMessage", "Channel name is too long.");
                dataObject.put("errorType", "invaild_channel_length");

                JSONObject packetObject = new JSONObject();
                packetObject.put("type", "error");
                packetObject.put("source", "server");

                outerObject.put("packet", packetObject); 
                outerObject.put("data", dataObject);

                Middleware.returnWithString(outerObject.toString(), 400, exchange);
                return;
            }

            if(channel == null || channel.isEmpty()){
                JSONObject outerObject = new JSONObject();

                JSONObject dataObject = new JSONObject();
                dataObject.put("errorMessage", "Missing or invalid channel in URL path.");
                dataObject.put("errorType", "missing_field");

                JSONObject packetObject = new JSONObject();
                packetObject.put("type", "error");
                packetObject.put("source", "server");

                outerObject.put("packet", packetObject);
                outerObject.put("data", dataObject);

                Middleware.returnWithString(outerObject.toString(), 400, exchange);
                return;
            }

            //Middleware.returnWithString(Middleware.bodyAsString(exchange), 200, exchange, "idk");
            List<HttpExchange> clients = ClientStore.get().getChannelClients(channel);
            String requestBody = Middleware.bodyAsString(exchange);
            for(HttpExchange client : clients){
                Middleware.sseWithString(requestBody, client);
            }

            JSONObject outerObject = new JSONObject();

            JSONObject dataObject = new JSONObject();
            dataObject.put("successfulClients", 1);
            dataObject.put("disconnectedCleints", 0);

            JSONObject packetObject = new JSONObject();
            packetObject.put("type", "status_report");
            packetObject.put("source", "server");

            outerObject.put("packet", packetObject);
            outerObject.put("data", dataObject);

            Middleware.returnWithString(outerObject.toString(), 200, exchange);

    }
}