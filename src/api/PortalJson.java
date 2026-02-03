package api;

import domain.Portal;

import java.util.List;

public class PortalJson {

    public static String toJson(Portal p) {
        return "{"
                + "\"id\":" + p.getId() + ","
                + "\"portalName\":\"" + esc(p.getPortalName()) + "\","
                + "\"url\":\"" + esc(p.getUrl()) + "\","
                + "\"usersActive\":" + p.getUsersActive() + ","
                + "\"working\":" + p.isWorking()
                + "}";
    }

    public static String toJson(List<Portal> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Portal p : list) {
            if (!first) sb.append(",");
            first = false;
            sb.append(toJson(p));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}