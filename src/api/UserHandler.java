package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.EntityNotFoundException;
import service.UserService;

import java.io.IOException;

public class UserHandler implements HttpHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        String path = ex.getRequestURI().getPath();

        try {
            // GET /api/users
            if (path.equals("/api/users")) {
                if (!"GET".equalsIgnoreCase(method)) {
                    HttpUtil.sendText(ex, 405, "Method Not Allowed");
                    return;
                }
                HttpUtil.sendJson(ex, 200, UserJson.toJson(userService.getAll()));
                return;
            }

            // POST /api/users/{id}/work
            // (HttpServer mapping will be /api/users, so тут прилетит полный path)
            if (path.startsWith("/api/users/")) {
                String[] parts = path.split("/");
                // ["", "api", "users", "{id}", "work"]
                if (parts.length == 5 && "work".equals(parts[4])) {
                    if (!"POST".equalsIgnoreCase(method)) {
                        HttpUtil.sendText(ex, 405, "Method Not Allowed");
                        return;
                    }
                    int id = Integer.parseInt(parts[3]);
                    String msg = userService.workMessage(id);
                    HttpUtil.sendJson(ex, 200, "{\"message\":\"" + esc(msg) + "\"}");
                    return;
                }
            }

            HttpUtil.sendText(ex, 404, "Not Found");

        } catch (EntityNotFoundException enf) {
            HttpUtil.sendJson(ex, 404, "{\"error\":\"" + esc(enf.getMessage()) + "\"}");
        } catch (Exception e) {
            HttpUtil.sendJson(ex, 500, "{\"error\":\"Server error\"}");
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}