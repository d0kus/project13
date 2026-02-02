package domain;

public abstract class User {
    private final int id;
    private String name;
    private String country;
    private String sphere;

    protected User(int id, String name, String country, String sphere) {
        this.id = id;
        setName(name);
        setCountry(country);
        setSphere(sphere);
    }

    public static String checkBlank(String value){
        if (value == null || value.isEmpty()) {
            return "N/A";
        }
        return value;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = checkBlank(name);
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = checkBlank(country);
    }
    public String getSphere() {
        return sphere;
    }
    public void setSphere(String sphere) {
        this.sphere = checkBlank(sphere);
    }

    public abstract String getRole();

    public abstract void work();

    public String toString(){
        return "ID: " + id + " Name: " + name + ", Country: " + country + ", Sphere: " + sphere;
    }




}



