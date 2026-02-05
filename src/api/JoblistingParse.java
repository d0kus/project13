package api;

import domain.Joblisting;
import exceptions.ValidationException;

public class JoblistingParse {

    // ожидаем JSON примерно:
    // { "id":1, "portalId":2, "jobTitle":"...", "company":"...", "sphere":"...", "active":true }
    public static Joblisting fromJson(String json) {
        int id = (int) num(json, "id");
        int portalId = (int) num(json, "portalId");
        String jobTitle = str(json, "jobTitle");
        String company = str(json, "company");
        String sphere = str(json, "sphere");
        boolean active = bool(json, "active");

        return new Joblisting(id, portalId, jobTitle, company, sphere, active);
    }

    public static boolean parseActive(String json) {
        return bool(json, "active");
    }

    // супер-простой парсер без библиотек (под твой проект)
    private static String str(String json, String key) {
        String pat = "\"" + key + "\"";
        int i = json.indexOf(pat);
        if (i < 0) throw new ValidationException("Missing field: " + key);
        int colon = json.indexOf(":", i);
        int q1 = json.indexOf("\"", colon + 1);
        int q2 = json.indexOf("\"", q1 + 1);
        if (q1 < 0 || q2 < 0) return "";
        return json.substring(q1 + 1, q2);
    }

    private static long num(String json, String key) {
        String pat = "\"" + key + "\"";
        int i = json.indexOf(pat);
        if (i < 0) throw new ValidationException("Missing field: " + key);
        int colon = json.indexOf(":", i);
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        return Long.parseLong(json.substring(start, end));
    }

    private static boolean bool(String json, String key) {
        String pat = "\"" + key + "\"";
        int i = json.indexOf(pat);
        if (i < 0) throw new ValidationException("Missing field: " + key);
        int colon = json.indexOf(":", i);
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (json.startsWith("true", start)) return true;
        if (json.startsWith("false", start)) return false;
        throw new ValidationException("Invalid boolean field: " + key);
    }
}