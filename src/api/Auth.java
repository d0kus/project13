package api;

import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Auth {
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin";

    public static void requireAdmin(HttpExchange ex) {
        if (!"admin".equals(roleOf(ex))) {
            throw new UnauthorizedException("Admin required");
        }
    }

    public static String roleOf(HttpExchange ex) {
        String h = ex.getRequestHeaders().getFirst("Authorization");
        if (h == null || !h.startsWith("Basic ")) return "guest";

        String b64 = h.substring("Basic ".length()).trim();
        String decoded = new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8);

        int idx = decoded.indexOf(':');
        if (idx < 0) return "guest";

        String user = decoded.substring(0, idx);
        String pass = decoded.substring(idx + 1);

        if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) return "admin";
        return "guest";
    }
}