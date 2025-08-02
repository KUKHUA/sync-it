package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

public final class Middleware {
    public static void returnWithString(String error, int httpStatus, HttpExchange exchange, String contentType){
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", contentType);

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

    public static void returnWithString(String error, int httpStatus, HttpExchange exchange){
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

    public static boolean sseWithString(String data, HttpExchange exchange){
        String sseData = "data: " + data + "\n\n";

        byte[] dataBytes = sseData.getBytes();
        OutputStream os = exchange.getResponseBody();
        try {
            os.write(dataBytes);
            os.flush();
        } catch (Exception err){
            return false;
        }

        return true;
    }

    public static String bodyAsString(HttpExchange exchange){
        String bodyString = "";

        try {
            bodyString = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
        } catch (Exception err){

        } finally {
            return bodyString;
        }
    }

}