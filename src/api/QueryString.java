package api;

import java.util.HashMap;
import java.util.Map;

public class QueryString {
    public static Map<String, String> parse(String query) {
        Map<String, String> m = new HashMap<>();
        if (query == null || query.isBlank()) return m;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int eq = pair.indexOf('=');
            if (eq < 0) {
                m.put(urlDecode(pair), "");
            } else {
                String k = urlDecode(pair.substring(0, eq));
                String v = urlDecode(pair.substring(eq + 1));
                m.put(k, v);
            }
        }
        return m;
    }

    private static String urlDecode(String s) {
        return s.replace("+", " "); // минимально достаточно для твоего UI
    }
}