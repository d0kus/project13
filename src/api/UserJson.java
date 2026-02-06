package api;

import domain.User;

import java.util.List;

public class UserJson {
    public static String toJson(List<User> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (User u : list) {
            if (!first) sb.append(",");
            first = false;
            sb.append(toJson(u));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toJson(User u) {
        return "{"
                + "\"id\":" + u.getId() + ","
                + "\"name\":\"" + esc(u.getName()) + "\","
                + "\"country\":\"" + esc(u.getCountry()) + "\","
                + "\"sphere\":\"" + esc(u.getSphere()) + "\","
                + "\"role\":\"" + esc(u.getRole()) + "\""
                + "}";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}