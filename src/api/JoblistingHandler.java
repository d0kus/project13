package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.Joblisting;
import exceptions.EntityNotFoundException;
import service.JoblistingService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JoblistingHandler implements HttpHandler {
    private final JoblistingService jobService;

    public JoblistingHandler(JoblistingService jobService) {
        this.jobService = jobService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            String method = ex.getRequestMethod();
            String path = ex.getRequestURI().getPath(); // /api/joblistings or /api/joblistings/101

            // ===== /api/joblistings =====
            if (path.equals("/api/joblistings")) {

                if (method.equals("GET")) {
                    List<Joblisting> all = jobService.getAll();
                    HttpUtil.sendJson(ex, 200, JoblistingJson.toJson(all));
                    return;
                }

                if (method.equals("POST")) {
                    String body = HttpUtil.readBody(ex);
                    Map<String, String> obj = SimpleJson.parseObject(body);

                    Joblisting j = new Joblisting(
                            Integer.parseInt(obj.get("id")),
                            obj.get("jobTitle"),
                            obj.get("company"),
                            obj.get("sphere"),
                            Boolean.parseBoolean(obj.get("active"))
                    );

                    jobService.create(j);

                    HttpUtil.sendJson(ex, 201, JoblistingJson.toJson(j));
                    return;
                }

                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            // ===== PATCH /api/joblistings/{id}/active =====
            if (path.startsWith("/api/joblistings/") && path.endsWith("/active")) {
                if (!method.equals("PATCH")) {
                    HttpUtil.sendText(ex, 405, "Method Not Allowed");
                    return;
                }

                String idPart = path.substring("/api/joblistings/".length(), path.length() - "/active".length());
                int id = Integer.parseInt(idPart);

                String body = HttpUtil.readBody(ex);
                Map<String, String> obj = SimpleJson.parseObject(body);

                if (!obj.containsKey("active")) {
                    HttpUtil.sendJson(ex, 400, "{\"error\":\"Missing field 'active'\"}");
                    return;
                }

                boolean active = Boolean.parseBoolean(obj.get("active"));
                jobService.setActive(id, active);

                Joblisting updated = jobService.getById(id);
                HttpUtil.sendJson(ex, 200, JoblistingJson.toJson(updated));
                return;
            }

            // ===== /api/joblistings/{id} =====
            if (path.startsWith("/api/joblistings/")) {
                String idStr = path.substring("/api/joblistings/".length());
                int id = Integer.parseInt(idStr);

                if (method.equals("GET")) {
                    Joblisting j = jobService.getById(id);
                    HttpUtil.sendJson(ex, 200, JoblistingJson.toJson(j));
                    return;
                }

                if (method.equals("DELETE")) {
                    jobService.deleteById(id);
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