public class freelancer {
    private String name;
    private String Sphere;
    private double rating;

    public freelancer() {

    }

    public freelancer(String name, String Sphere, double rating) {
        this.name = name;
        this.Sphere = Sphere;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSphere() {
        return Sphere;
    }
    public void setSphere(String Sphere) {
        this.Sphere = Sphere;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public void changeSphere(String newSphere) {
        this.Sphere = newSphere;
    }
    public void increaseRating(double increment) {
        this.rating += increment;
    }

    public void showinfo() {
        System.out.println("Freelancer Name: " + name);
        System.out.println("Sphere: " + Sphere);
        System.out.println("Rating: " + rating);
    }

    public int compareRating(freelancer other) {
        return Double.compare(this.rating, other.rating);
    }
}
