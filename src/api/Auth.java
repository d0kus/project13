package api;

import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Auth {
    public static final String ROLE_GUEST = "guest";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_EMPLOYER = "employer";
    public static final String ROLE_FREELANCER = "freelancer";

    private static final Map<String, String> USERS = new HashMap<>();
    static {
        USERS.put("admin:admin", ROLE_ADMIN);
        USERS.put("employer:employer", ROLE_EMPLOYER);
        USERS.put("freelancer:freelancer", ROLE_FREELANCER);
    }

    public static String roleOf(HttpExchange ex) {
        String h = ex.getRequestHeaders().getFirst("Authorization");
        if (h == null || !h.startsWith("Basic ")) return ROLE_GUEST;

        String b64 = h.substring("Basic ".length()).trim();
        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return ROLE_GUEST;
        }

        // decoded is "user:pass"
        return USERS.getOrDefault(decoded, ROLE_GUEST);
    }

    public static void requireAdmin(HttpExchange ex) {
        requireRole(ex, ROLE_ADMIN);
    }

    public static void requireEmployerOrAdmin(HttpExchange ex) {
        requireRole(ex, ROLE_ADMIN, ROLE_EMPLOYER);
    }

    public static void requireRole(HttpExchange ex, String... allowedRoles) {
        String role = roleOf(ex);
        for (String r : allowedRoles) {
            if (r.equals(role)) return;
        }
        throw new UnauthorizedException("Forbidden for role=" + role);
    }
}