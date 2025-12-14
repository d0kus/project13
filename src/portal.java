public class portal{
    private String portalName;
    private String url;
    private int usersActive;
    private boolean working;

    public portal() {

    }
    public portal(String portalName, String url, int usersActive, boolean working) {
        this.portalName = portalName;
        this.url = url;
        this.usersActive = usersActive;
        this.working = working;
    }
    public String getPortalName() {
        return portalName;
    }
    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
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
    public void printinfo()
    {
        System.out.println("Portal: " + portalName);
        System.out.println("URL: " + url);
        System.out.println("Active Users: " + usersActive);
        System.out.println("Working: " + working);
    }

}