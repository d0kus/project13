package builder;

import domain.Portal;

import java.util.Map;

public class PortalBuilder {
    private int id;
    private String portalName;
    private String url;
    private int usersActive;
    private boolean working;

    public static PortalBuilder fromMap(Map<String, String> obj) {
        return new PortalBuilder()
                .id(Integer.parseInt(obj.get("id")))
                .portalName(obj.get("portalName"))
                .url(obj.get("url"))
                .usersActive(Integer.parseInt(obj.get("usersActive")))
                .working(Boolean.parseBoolean(obj.get("working")));
    }

    public PortalBuilder id(int id) { this.id = id; return this; }
    public PortalBuilder portalName(String portalName) { this.portalName = portalName; return this; }
    public PortalBuilder url(String url) { this.url = url; return this; }
    public PortalBuilder usersActive(int usersActive) { this.usersActive = usersActive; return this; }
    public PortalBuilder working(boolean working) { this.working = working; return this; }

    public Portal build() {
        return new Portal(id, portalName, url, usersActive, working);
    }
}