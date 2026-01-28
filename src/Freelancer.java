public class Freelancer extends User {
    private double rating;

    public Freelancer(int Id, String Name, String Country, String Sphere, double Rating) {
        super(Id, Name, Country, Sphere);
        if (Rating < 0) {
            setRating(0);
        } else if (Rating > 5) {
            setRating(5);
        } else {
            setRating(Rating);
        }

    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        if (rating < 0) {
            this.rating = 0;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = rating;
        }
    }

    public void changeSphere(String newSphere) {
        setSphere(newSphere);
    }

    public void increaseRating(double increment) {
        setRating(this.rating + increment);
    }

    @Override
    public String getRole() {
        return "Freelancer";
    }

    @Override
    public void work() {
        System.out.println(getName() + " is completing tasks in " + getSphere()
                + ", rating: " + getRating());
    }

    @Override
    public String toString() {
        return getRole() + ", " + super.toString() + ", Rating: " + rating;
    }

    public void showinfo() {
        System.out.println(toString());
    }

    public int compareRating(Freelancer other) {
        return Double.compare(this.rating, other.rating);
    }
}
