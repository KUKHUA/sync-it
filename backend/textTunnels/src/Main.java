import http.*;

public class Main {
      public static void main(String[] args) {
        http server = new http(8000);
        server.start();
        server.addHandler("/", new SSEConnect());
    }
}
