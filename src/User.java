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

    public static String checkblank(String value){
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
        this.name = checkblank(name);
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = checkblank(country);
    }
    public String getSphere() {
        return sphere;
    }
    public void setSphere(String sphere) {
        this.sphere = checkblank(sphere);
    }

    public abstract String getRole();

    public abstract void work();

    public String toString(){
        return "ID: " + id + " Name: " + name + ", Country: " + country + ", Sphere: " + sphere;
    }




}



