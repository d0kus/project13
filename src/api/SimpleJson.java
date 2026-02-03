package api;

import java.util.HashMap;
import java.util.Map;

public class SimpleJson {
                                                                                // Только плоский JSON объект: {"a":1,"b":"x","c":true}
    public static Map<String, String> parseObject(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;

        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        if (json.trim().isEmpty()) return map;

        String[] parts = json.split(",");
        for (String part : parts) {
            String[] kv = part.split(":", 2);
            if (kv.length != 2) continue;

            String key = strip(kv[0]);
            String value = strip(kv[1]);
            map.put(key, value);
        }
        return map;
    }

    private static String strip(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s.trim();
    }
}