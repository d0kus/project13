package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.Portal;
import exceptions.ValidationException;
import service.JoblistingService;
import service.PortalService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortalHandler implements HttpHandler {
    private final PortalService portalService;
    private final JoblistingService jobService;

    public PortalHandler(PortalService portalService, JoblistingService jobService) {
        this.portalService = portalService;
        this.jobService = jobService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        String path = ex.getRequestURI().getPath();

        try {
            // RULE: only admin can mutate portals; everyone can GET
            if (!method.equalsIgnoreCase("GET")) {
                Auth.requireAdmin(ex);
            }

            if (path.equals("/api/portals")) {

                if (method.equalsIgnoreCase("GET")) {
                    List<Portal> portals = portalService.getAll();
                    Map<String, String> params = QueryString.parse(ex.getRequestURI().getQuery());

                    String workingParam = params.get("working");
                    if (workingParam != null && !workingParam.isBlank()) {
                        boolean w = Boolean.parseBoolean(workingParam);
                        portals = portals.stream()
                                .filter(p -> p.isWorking() == w)
                                .collect(Collectors.toList());
                    }

                    String sort = params.get("sort");
                    if ("usersActiveAsc".equals(sort)) {
                        portals.sort(Comparator.comparingInt(Portal::getUsersActive));
                    } else if ("usersActiveDesc".equals(sort)) {
                        portals.sort((a, b) -> Integer.compare(b.getUsersActive(), a.getUsersActive()));
                    }

                    HttpUtil.sendJson(ex, 200, PortalJson.toJson(portals));
                    return;
                }

                if (method.equalsIgnoreCase("POST")) {
                    String body = HttpUtil.readBody(ex);
                    Portal p = PortalParse.fromJson(body);
                    portalService.create(p);
                    HttpUtil.sendJson(ex, 201, "{\"status\":\"created\"}");
                    return;
                }

                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            if (path.startsWith("/api/portals/")) {
                String[] parts = path.split("/");
                if (parts.length < 4) {
                    HttpUtil.sendText(ex, 400, "Bad Request");
                    return;
                }

                int id = Integer.parseInt(parts[3]);

                if (parts.length == 4 && method.equalsIgnoreCase("DELETE")) {
                    portalService.deleteById(id);
                    HttpUtil.sendJson(ex, 200, "{\"status\":\"deleted\"}");
                    return;
                }

                if (parts.length == 5 && parts[4].equals("working") && method.equalsIgnoreCase("PATCH")) {
                    String body = HttpUtil.readBody(ex);
                    boolean working = PortalParse.parseWorking(body);

                    portalService.setWorking(id, working);

                    if (!working) {
                        jobService.deactivateByPortalId(id);
                    }

                    HttpUtil.sendJson(ex, 200, "{\"status\":\"updated\"}");
                    return;
                }

                HttpUtil.sendText(ex, 404, "Not Found");
                return;
            }

            HttpUtil.sendText(ex, 404, "Not Found");

        } catch (UnauthorizedException ue) {
            HttpUtil.sendJson(ex, 403, "{\"error\":\"" + esc(ue.getMessage()) + "\"}");
        } catch (ValidationException ve) {
            HttpUtil.sendJson(ex, 400, "{\"error\":\"" + esc(ve.getMessage()) + "\"}");
        } catch (SQLException se) {
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