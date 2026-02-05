package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.Joblisting;
import domain.Portal;
import exceptions.ValidationException;
import service.JoblistingService;
import service.PortalService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class JoblistingHandler implements HttpHandler {
    private final JoblistingService jobService;
    private final PortalService portalService;

    public JoblistingHandler(JoblistingService jobService, PortalService portalService) {
        this.jobService = jobService;
        this.portalService = portalService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        String path = ex.getRequestURI().getPath();

        try {
            if (path.equals("/api/joblistings")) {

                if (method.equalsIgnoreCase("GET")) {
                    List<Joblisting> jobs = jobService.getAll();
                    HttpUtil.sendJson(ex, 200, JoblistingJson.toJson(jobs));
                    return;
                }

                // RULE: POST is employer/admin only
                if (method.equalsIgnoreCase("POST")) {
                    Auth.requireEmployerOrAdmin(ex);

                    String body = HttpUtil.readBody(ex);
                    Joblisting j = JoblistingParse.fromJson(body);

                    Portal p = portalService.getById(j.getPortalId());
                    jobService.create(j, p);

                    HttpUtil.sendJson(ex, 201, "{\"status\":\"created\"}");
                    return;
                }

                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            if (path.startsWith("/api/joblistings/")) {
                String[] parts = path.split("/");
                if (parts.length < 4) {
                    HttpUtil.sendText(ex, 400, "Bad Request");
                    return;
                }

                int id = Integer.parseInt(parts[3]);

                // RULE: PATCH/DELETE are employer/admin only
                if (parts.length == 4 && method.equalsIgnoreCase("DELETE")) {
                    Auth.requireEmployerOrAdmin(ex);

                    jobService.deleteById(id);
                    HttpUtil.sendJson(ex, 200, "{\"status\":\"deleted\"}");
                    return;
                }

                if (parts.length == 5 && parts[4].equals("active") && method.equalsIgnoreCase("PATCH")) {
                    Auth.requireEmployerOrAdmin(ex);

                    String body = HttpUtil.readBody(ex);
                    boolean active = JoblistingParse.parseActive(body);
                    jobService.setActive(id, active);

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