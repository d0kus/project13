package pool;

import domain.Freelancer;
import domain.Joblisting;
import domain.Portal;
import domain.User;

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

    public List<Freelancer> sortFreelancersByRatingDesc() {
        List<Freelancer> fs = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Freelancer f)
                fs.add(f);
        }
        fs.sort(java.util.Comparator.comparingDouble(Freelancer::getRating).reversed());
        return fs;
    }


}

