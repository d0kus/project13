public class Employer extends  User {
    private String Company;

    public Employer(int Id, String Name, String Country, String Company, String Sphere) {
        super(Id, Name, Country, Sphere);
        setCompany(Company);
    }

    public String getCompany() {
        return Company;
    }
    public void setCompany(String company) {
        this.Company = checkblank(company);
    }
    @Override
    public String getRole() {
        return "Employer";
    }
    @Override
    public String shortInfo() {
        return super.shortInfo() + " Company: " + Company;
    }
}
