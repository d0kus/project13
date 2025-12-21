import java.util.ArrayList;
import java.util.List;

public class DataPool {
    private final List<User> users = new ArrayList<>();
    private final List<Joblisting> joblistings = new ArrayList<>();
    private final List<Portal> portals = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }
    public void addJoblisting(Joblisting joblisting) {
        joblistings.add(joblisting);
    }
    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public List<User> getUsers() {
        return users;
    }
    public List<Joblisting> getJoblistings() {
        return joblistings;
    }
    public List<Portal> getPortals() {
        return portals;
    }

    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public List<Joblisting> activeJob() {
        List<Joblisting> result = new ArrayList<>();
        for (Joblisting listing : joblistings) {
            if (listing.isActive()) {
                result.add(listing);
            }
        }
        return result;
    }

    public List<Freelancer> sortFreelancersByRatingDescManual() {
        List<Freelancer> fs = new ArrayList<>();
        for (User u : users) {
            if (u.getClass() == Freelancer.class)
                fs.add((Freelancer) u);
        }
        for (int i = 0; i < fs.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < fs.size(); j++) {
                if (fs.get(j).getRating() > fs.get(maxIndex).getRating()) {
                    maxIndex = j;
                }
            }
            Freelancer tmp = fs.get(i);
            fs.set(i, fs.get(maxIndex));
            fs.set(maxIndex, tmp);
        }

        return fs;
    }


}

