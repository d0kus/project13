package service;

import domain.Employer;
import domain.Freelancer;
import domain.User;
import exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> users = new ArrayList<>();

    public UserService() {
        // Seed demo users
        users.add(new Freelancer(1, "Diyar", "Kazakhstan", "IT", 4.8));
        users.add(new Employer(2, "Aibek", "Kazakhstan", "TechCorp", "IT"));
        users.add(new Freelancer(3, "Anton", "Russia", "Design", 4.5));
    }

    public List<User> getAll() {
        return users;
    }

    public User getById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        throw new EntityNotFoundException("User with id=" + id + " not found");
    }

    /** Полиморфизм: поведение зависит от реального типа (Freelancer/Employer). */
    public String workMessage(int id) {
        User u = getById(id);
        String role = u.getRole();
        u.work();

        if ("Freelancer".equalsIgnoreCase(role)) {
            return u.getName() + " is freelancing: searching projects and completing tasks.";
        }
        if ("Employer".equalsIgnoreCase(role)) {
            return u.getName() + " is hiring: posting jobs and reviewing candidates.";
        }
        return u.getName() + " is working.";
    }
}