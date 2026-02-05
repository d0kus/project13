package api;

import domain.Joblisting;
import java.util.List;

public class JoblistingJson {
    public static String toJson(List<Joblisting> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Joblisting j : list) {
            if (!first) sb.append(",");
            first = false;
            sb.append(toJson(j));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toJson(Joblisting j) {
        return "{"
                + "\"id\":" + j.getId() + ","
                + "\"portalId\":" + j.getPortalId() + ","
                + "\"jobTitle\":\"" + esc(j.getJobTitle()) + "\","
                + "\"company\":\"" + esc(j.getCompany()) + "\","
                + "\"sphere\":\"" + esc(j.getSphere()) + "\","
                + "\"active\":" + j.isActive()
                + "}";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}