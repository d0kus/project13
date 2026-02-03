package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.Portal;
import exceptions.EntityNotFoundException;
import service.PortalService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PortalHandler implements HttpHandler {
    private final PortalService portalService;

    public PortalHandler(PortalService portalService) {
        this.portalService = portalService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            String method = ex.getRequestMethod();
            String path = ex.getRequestURI().getPath(); // /api/portals or /api/portals/5

            // ===== /api/portals =====
            if (path.equals("/api/portals")) {

                if (method.equals("GET")) {
                    List<Portal> all = portalService.getAll();
                    HttpUtil.sendJson(ex, 200, PortalJson.toJson(all));
                    return;
                }

                if (method.equals("POST")) {
                    String body = HttpUtil.readBody(ex);
                    Map<String, String> obj = SimpleJson.parseObject(body);

                    Portal p = new Portal(
                            Integer.parseInt(obj.get("id")),
                            obj.get("portalName"),
                            obj.get("url"),
                            Integer.parseInt(obj.get("usersActive")),
                            Boolean.parseBoolean(obj.get("working"))
                    );

                    portalService.create(p);

                    HttpUtil.sendJson(ex, 201, PortalJson.toJson(p));
                    return;
                }

                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            // ===== PATCH /api/portals/{id}/working =====
            if (path.startsWith("/api/portals/") && path.endsWith("/working")) {
                if (!method.equals("PATCH")) {
                    HttpUtil.sendText(ex, 405, "Method Not Allowed");
                    return;
                }

                String idPart = path.substring("/api/portals/".length(), path.length() - "/working".length());
                int id = Integer.parseInt(idPart);

                String body = HttpUtil.readBody(ex);
                Map<String, String> obj = SimpleJson.parseObject(body);

                if (!obj.containsKey("working")) {
                    HttpUtil.sendJson(ex, 400, "{\"error\":\"Missing field 'working'\"}");
                    return;
                }

                boolean working = Boolean.parseBoolean(obj.get("working"));
                portalService.setWorking(id, working);

                Portal updated = portalService.getById(id);
                HttpUtil.sendJson(ex, 200, PortalJson.toJson(updated));
                return;
            }

            // ===== /api/portals/{id} =====
            if (path.startsWith("/api/portals/")) {
                String idStr = path.substring("/api/portals/".length());
                int id = Integer.parseInt(idStr);

                if (method.equals("GET")) {
                    Portal p = portalService.getById(id);
                    HttpUtil.sendJson(ex, 200, PortalJson.toJson(p));
                    return;
                }

                if (method.equals("DELETE")) {
                    portalService.deleteById(id);
                    ex.sendResponseHeaders(204, -1);
                    ex.close();
                    return;
                }

                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            HttpUtil.sendText(ex, 404, "Not Found");

        } catch (EntityNotFoundException e) {
            HttpUtil.sendJson(ex, 404, "{\"error\":\"" + esc(e.getMessage()) + "\"}");
        } catch (NumberFormatException e) {
            HttpUtil.sendJson(ex, 400, "{\"error\":\"Bad id/number format\"}");
        } catch (SQLException e) {
            HttpUtil.sendJson(ex, 500, "{\"error\":\"DB error\"}");
        } catch (Exception e) {
            HttpUtil.sendJson(ex, 500, "{\"error\":\"Server error\"}");
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}