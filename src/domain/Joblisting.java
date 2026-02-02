package domain;

public class Joblisting {
    private int id;
    private String jobTitle;
    private String company;
    private String sphere;
    private boolean isActive;

    public Joblisting() {

    }

    public Joblisting(int Id, String jobTitle, String company, String sphere, boolean isActive) {
        setId(Id);
        setJobTitle(jobTitle);
        setCompany(company);
        setSphere(sphere);
        setActive(isActive);
    }

    public int getId() {
        return id;
    }
    public void setId(int Id) {
        this.id = Id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = User.checkBlank(jobTitle);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = User.checkBlank(company);
    }

    public String getSphere() {
        return sphere;
    }

    public void setSphere(String sphere) {
        this.sphere = User.checkBlank(sphere);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeJobTitle(String newJobTitle) {
        this.jobTitle = User.checkBlank(newJobTitle);
    }

    public void changeCompany(String newCompany) {
        this.company = User.checkBlank(newCompany);
    }

    public void changeSphere(String newSphere) {
        this.sphere = User.checkBlank(newSphere);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "domain.Joblisting [Id=" + id + ", jobTitle=" + jobTitle + ", Company=" + company + ", Sphere=" + sphere
                + ", isActive=" + isActive + "]";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joblisting that = (Joblisting) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}