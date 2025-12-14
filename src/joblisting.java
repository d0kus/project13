public class joblisting {
    private String jobTitle;
    private String company;
    private String sphere;
    private boolean isActive;

    public joblisting() {

    }

    public joblisting(String jobTitle, String company, String sphere, boolean isActive) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.sphere = sphere;
        this.isActive = isActive;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSphere() {
        return sphere;
    }

    public void setSphere(String sphere) {
        this.sphere = sphere;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeJobTitle(String newJobTitle) {
        this.jobTitle = newJobTitle;
    }

    public void changeCompany(String newCompany) {
        this.company = newCompany;
    }

    public void changeSphere(String newSphere) {
        this.sphere = newSphere;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void printinfo() {
        System.out.println("Job Title: " + jobTitle + ", Company: " + company + ", Sphere: " + sphere + ", Active: " + isActive);
    }
}