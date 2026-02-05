package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticFileHandler implements HttpHandler {
    private final Path webRoot;

    public StaticFileHandler(Path webRoot) {
        this.webRoot = webRoot;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";

        if (!path.equals("/index.html") && !path.equals("/app.js") && !path.equals("/styles.css")) {
            HttpUtil.sendText(ex, 404, "Not Found");
            return;
        }

        Path file = webRoot.resolve(path.substring(1)).normalize();

        if (!file.startsWith(webRoot)) {
            HttpUtil.sendText(ex, 403, "Forbidden");
            return;
        }

        if (!Files.exists(file) || Files.isDirectory(file)) {
            HttpUtil.sendText(ex, 404, "Not Found");
            return;
        }

        byte[] bytes = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", contentType(path));
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    private static String contentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        return "application/octet-stream";
    }
}