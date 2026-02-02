package app;

import domain.*;
import pool.DataPool;
import exceptions.EntityNotFoundException;
import exceptions.ValidationException;

import java.util.HashSet;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

        Joblisting job1 = new Joblisting(1, "Software Engineer", "TechCorp", "IT", true);
        Joblisting job2 = new Joblisting(2, "IT designer", "DesignPro", "Design", false);
        Joblisting job3 = new Joblisting(3, "Developer", "Amigo", "IT", true);

        HashSet<Joblisting> setjob = new HashSet<>();
        setjob.add(job1);
        setjob.add(job3);
        System.out.println("Number of unique job listings in the set: " + setjob.size());
        System.out.println("a.equals b: " + job1.equals(job3));
        System.out.println("a.hashCode(): " + job1.hashCode());
        System.out.println("b.hashCode(): " + job3.hashCode());
        System.out.println("hashset contents" + setjob);

        Portal portal1 = new Portal(1,"FreelanceHub", "www.freelancehub.com", 5000, true);
        Portal portal2 = new Portal(2,"JobFinder", "www.jobfinder.com", 8000, true);
        try{
            portal1.setUsersActive(-10);
        }
        catch(ValidationException e){
            System.out.println("Validation error: " + e.getMessage());
        }

        User u1 = new Freelancer(1, "Diyar", "Kazakhstan", "IT", 4.8);
        User u2 = new Employer(2, "Aibek", "Kazakhstan", "TechCorp", "IT");
        User u3 = new Freelancer(3, "Anton", "Russia", "Design", 4.5);

        System.out.println("Portal info: ");
        System.out.println(portal1);
        System.out.println(portal2);

        System.out.println("JobListing info: ");
        System.out.println(job1);
        System.out.println(job2);
        System.out.println(job3);

        DataPool pool = new DataPool();

        pool.addUser(u1);
        pool.addUser(u2);
        pool.addUser(u3);

        pool.addJoblisting(job1);
        pool.addJoblisting(job2);
        pool.addJoblisting(job3);

        pool.addPortal(portal1);
        pool.addPortal(portal2);

        System.out.println("type id to find user");
        Scanner sc = new Scanner(System.in);
        int ent = sc.nextInt();
        try{
            User found = pool.findUserById(ent);
            System.out.println(found);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

        var active = pool.activeJob();
        System.out.println("Active job listings: " + active);
        System.out.println("Number of active job listings: " + active.size());

        System.out.println("All users:");
        for (User user : pool.getUsers()) {
            System.out.println(user);
        }

        Portal maxPortal = null;
        for (Portal p : pool.getPortals()) {
            if (maxPortal == null || p.getUsersActive() > maxPortal.getUsersActive()) {
                maxPortal = p;
            }
        }
        if (maxPortal == null) {
            System.out.println("No portals in pool");
        } else {
            System.out.println("Max active users portal: " + maxPortal);
        }

        System.out.println("Freelancers sorted by rating: ");
        for (Freelancer f : pool.sortFreelancersByRatingDesc()) {
            System.out.println(f);
        }
        System.out.println("Type user id to check their work:");
        int userid = sc.nextInt();
        try {
            User worker = pool.findUserById(userid);
            worker.work();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

        new DbDemo().run(sc);


    }
}