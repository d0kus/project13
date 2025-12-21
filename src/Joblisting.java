public class Joblisting {
    private int Id;
    private String jobTitle;
    private String Company;
    private String Sphere;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = checkblank(jobTitle);
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        this.Company = checkblank(company);
    }

    public String getSphere() {
        return Sphere;
    }

    public void setSphere(String sphere) {
        this.Sphere = checkblank(sphere);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeJobTitle(String newJobTitle) {
        this.jobTitle = checkblank(newJobTitle);
    }

    public void changeCompany(String newCompany) {
        this.Company = checkblank(newCompany);
    }

    public void changeSphere(String newSphere) {
        this.Sphere = checkblank(newSphere);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "Joblisting [Id=" + Id + ", jobTitle=" + jobTitle + ", Company=" + Company + ", Sphere=" + Sphere
                + ", isActive=" + isActive + "]";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joblisting that = (Joblisting) o;
        return Id == that.Id;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(Id);
    }

}