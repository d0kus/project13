public class Portal{
    private int Id;
    private String portalName;
    private String url;
    private int usersActive;
    private boolean working;

    public Portal() {

    }
    public Portal(int Id,String portalName, String url, int usersActive, boolean working) {
        setId(Id);
        setPortalName(portalName);
        setUrl(url);
        setUsersActive(usersActive);
        setWorking(working);
    }

    private static String checkblank(String value){
        if (value == null || value.isEmpty()) {
            return "N/A";
        }
        return value;
    }
    public int getId() {
        return Id;
    }
    public void setId(int Id) {
        this.Id = Id;
    }

    public String getPortalName() {
        return portalName;
    }
    public void setPortalName(String portalName) {
        this.portalName = checkblank(portalName);
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = checkblank(url);
    }
    public int getUsersActive() {
        return usersActive;
    }
    public void setUsersActive(int usersActive) {
        this.usersActive = usersActive;
    }
    public boolean isWorking() {
        return working;
    }
    public void setWorking(boolean working) {
        this.working = working;
    }

    @Override
    public String toString() {
        return "Portal ID: " + Id + " Name: " + portalName + " URL: " + url + " Active Users: " + usersActive + " Working: " + working;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portal that = (Portal) o;
        return Id == that.Id;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(Id);
    }

}