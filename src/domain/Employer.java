package domain;

public class Employer extends User {
    private String company;

    public Employer(int id, String name, String country, String company, String sphere) {
        super(id, name, country, sphere);
        setCompany(company);
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = checkBlank(company);
    }

    @Override
    public String getRole() {
        return "domain.Employer";
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
