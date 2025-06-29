package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Middleware {
    public static void errorWithString(String error, int httpStatus, HttpExchange exchange){
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        byte[] errorBytes = error.getBytes();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(httpStatus, errorBytes.length);
            os.write(errorBytes);
            os.flush();
        } catch (Exception err){

        } finally {
            exchange.close();
        }
    }
}