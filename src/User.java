public abstract class User {
    private final int Id;
    private String Name;
    private String Country;
    private String Sphere;

    protected User(int id, String name, String country, String sphere) {
        this.Id = id;
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
        return Id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = checkblank(name);
    }
    public String getCountry() {
        return Country;
    }
    public void setCountry(String country) {
        this.Country = checkblank(country);
    }
    public String getSphere() {
        return Sphere;
    }
    public void setSphere(String sphere) {
        this.Sphere = checkblank(sphere);
    }

    public abstract String getRole();

    public abstract void work();

    public String toString(){
        return "ID: " + Id + " Name: " + Name + ", Country: " + Country + ", Sphere: " + Sphere;
    }




}



