public class Employer extends  User {
    private String company;

    public Employer(int Id, String Name, String Country, String Company, String Sphere) {
        super(Id, Name, Country, Sphere);
        setCompany(Company);
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = checkblank(company);
    }

    @Override
    public String getRole() {
        return "Employer";
    }

    @Override
    public void work() {
        System.out.println(getName() + " from " + getCompany()
                + " is posting vacancies in " + getSphere());
    }

    @Override
    public String toString() {
        return getRole() +", " + super.toString() + ", Company: " + company;
    }
}
