/*
 * ChangeDetector -  detects modifications, deletions, or creations of files and folders.
 * Copyright (C) 2025 KUKHUA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.json.JSONException;

import http.Middleware;

import java.net.URI;



/**
 * The {@code SSEConnect} class implements the HttpHandler interface to handle Server-Sent Events (SSE) connections.
 * It sets the appropriate headers for SSE and adds the client to a store for managing active connections.
 */
public final class SSEConnect implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange){
        try {
            Headers headers = exchange.getResponseHeaders();
            String path = exchange.getRequestURI().getPath();
            String prefix = "/channel/";
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

                Middleware.errorWithString(outerObject.toString(), 400, exchange);
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

                Middleware.errorWithString(outerObject.toString(), 400, exchange);
                return;
            }

            headers.set("Cache-Control", "no-cache");
            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Access-Control-Allow-Methods", "*");
            headers.set("Access-Control-Allow-Headers", "*");
            headers.set("Content-Type", "text/event-stream");
            headers.set("Connection", "keep-alive");

            exchange.sendResponseHeaders(200, 0);
            ClientStore.get().addClient(channel, exchange);
            System.out.println("New SSE client connected.");
        } catch(IOException e ){
            System.out.println("Client IO failure with the SSE server. Maybe client crashed?" + e.getMessage());
        }
    }

}