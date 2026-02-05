package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.Joblisting;
import domain.Portal;
import service.JoblistingService;
import service.PortalService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class StatsHandler implements HttpHandler {
    private final PortalService portalService;
    private final JoblistingService jobService;

    public StatsHandler(PortalService portalService, JoblistingService jobService) {
        this.portalService = portalService;
        this.jobService = jobService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                HttpUtil.sendText(ex, 405, "Method Not Allowed");
                return;
            }

            List<Portal> portals = portalService.getAll();
            List<Joblisting> jobs = jobService.getAll();

            long portalsWorking = portals.stream().filter(Portal::isWorking).count();
            long portalsNotWorking = portals.size() - portalsWorking;

            long jobsActive = jobs.stream().filter(Joblisting::isActive).count();
            long jobsInactive = jobs.size() - jobsActive;

            Portal topPortal = portals.stream()
                    .max(Comparator.comparingInt(Portal::getUsersActive))
                    .orElse(null);

            String topPortalJson = (topPortal == null)
                    ? "null"
                    : "{"
                    + "\"id\":" + topPortal.getId() + ","
                    + "\"portalName\":\"" + esc(topPortal.getPortalName()) + "\","
                    + "\"usersActive\":" + topPortal.getUsersActive()
                    + "}";

            String json = "{"
                    + "\"portalsTotal\":" + portals.size() + ","
                    + "\"portalsWorking\":" + portalsWorking + ","
                    + "\"portalsNotWorking\":" + portalsNotWorking + ","
                    + "\"jobsTotal\":" + jobs.size() + ","
                    + "\"jobsActive\":" + jobsActive + ","
                    + "\"jobsInactive\":" + jobsInactive + ","
                    + "\"topPortalByUsersActive\":" + topPortalJson
                    + "}";

            HttpUtil.sendJson(ex, 200, json);

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