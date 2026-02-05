package api;

import domain.Portal;
import exceptions.ValidationException;

public class PortalParse {
    public static Portal fromJson(String json) {
        int id = (int) num(json, "id");
        String portalName = str(json, "portalName");
        String url = str(json, "url");
        int usersActive = (int) num(json, "usersActive");
        boolean working = bool(json, "working");
        return new Portal(id, portalName, url, usersActive, working);
    }

    public static boolean parseWorking(String json) {
        return bool(json, "working");
    }

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