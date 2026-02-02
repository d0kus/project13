package domain;

import exceptions.ValidationException;

public class Portal{
    private int id;
    private String portalName;
    private String url;
    private int usersActive;
    private boolean working;

    public Portal() {

    }
    public Portal(int id, String portalName, String url, int usersActive, boolean working) {
        setId(id);
        setPortalName(portalName);
        setUrl(url);
        setUsersActive(usersActive);
        setWorking(working);
    }


    public int getId() {
        return id;
    }
    public void setId(int Id) {
        this.id = Id;
    }

    public String getPortalName() {
        return portalName;
    }
    public void setPortalName(String portalName) {
        this.portalName = User.checkBlank(portalName);
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = User.checkBlank(url);
    }
    public int getUsersActive() {
        return usersActive;
    }
    public void setUsersActive(int usersActive) {
        if (usersActive < 0) {
            throw new ValidationException("Active users cannot be negative");
        }
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
        return "Portal ID: " + id + " Name: " + portalName + " URL: " + url + " Active Users: " + usersActive + " Working: " + working;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portal that = (Portal) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}