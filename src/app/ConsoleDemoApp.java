package app;

import domain.*;
import exceptions.EntityNotFoundException;
import exceptions.ValidationException;
import pool.DataPool;

import java.util.HashSet;
import java.util.Scanner;

public class ConsoleDemoApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> demoOopPolymorphism();
                case "2" -> demoCollectionsEqualsHashCode();
                case "3" -> demoDataPoolSearchFilterSort(sc);
                case "4" -> demoExceptionsAndValidation(sc);
                case "5" -> new DbDemo().run(sc); // optional DB demo
                case "0" -> {
                    System.out.println("Bye.");
                    return;
                }
                default -> System.out.println("Unknown option.");
            }

            System.out.println("\n--- Press Enter to continue ---");
            sc.nextLine();
        }
    }

    private static void printMenu() {
        System.out.println("""
                === Project13 Console Demo ===
                1) OOP: inheritance + polymorphism (User.work())
                2) Collections: HashSet + equals/hashCode
                3) DataPool: search/filter/sort
                4) Exceptions + Validation
                5) DB demo (seed/find/update/delete) [optional]
                0) Exit
                """);
        System.out.print("Choose: ");
    }

    // 1) OOP: polymorphism
    private static void demoOopPolymorphism() {
        System.out.println("== 1) OOP polymorphism demo ==");

        User u1 = new Freelancer(1, "Diyar", "Kazakhstan", "IT", 4.8);
        User u2 = new Employer(2, "Aibek", "Kazakhstan", "TechCorp", "IT");

        // Polymorphic call: runtime dispatch based on actual type
        System.out.println("Calling work() on User references:");
        u1.work();
        u2.work();
    }

    // 2) Collections: HashSet uniqueness + equals/hashCode
    private static void demoCollectionsEqualsHashCode() {
        System.out.println("== 2) Collections + equals/hashCode demo ==");

        Joblisting a = new Joblisting(1, "Software Engineer", "TechCorp", "IT", true);
        Joblisting b = new Joblisting(1, "Software Engineer", "TechCorp", "IT", true);

        HashSet<Joblisting> set = new HashSet<>();
        set.add(a);
        set.add(b);

        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.hashCode(): " + a.hashCode());
        System.out.println("b.hashCode(): " + b.hashCode());
        System.out.println("HashSet size (should be 1 if equals/hashCode are correct): " + set.size());
        System.out.println("HashSet contents: " + set);
    }

    // 3) DataPool: search/filter/sort
    private static void demoDataPoolSearchFilterSort(Scanner sc) {
        System.out.println("== 3) DataPool search/filter/sort demo ==");

        DataPool pool = seedPool();

        System.out.print("Enter user id to search (e.g., 1..3): ");
        int id = Integer.parseInt(sc.nextLine().trim());

        try {
            User found = pool.findUserById(id);
            System.out.println("Found user: " + found);
        } catch (EntityNotFoundException e) {
            System.out.println("Not found: " + e.getMessage());
        }

        System.out.println("Active joblistings: " + pool.activeJob());
        System.out.println("Freelancers sorted by rating desc: " + pool.sortFreelancersByRatingDesc());
    }

    // 4) Exceptions + validation
    private static void demoExceptionsAndValidation(Scanner sc) {
        System.out.println("== 4) Exceptions + validation demo ==");

        Portal portal = new Portal(1, "FreelanceHub", "www.freelancehub.com", 5000, true);

        System.out.println("Try to set usersActive to -10 (should throw ValidationException):");
        try {
            portal.setUsersActive(-10);
        } catch (ValidationException e) {
            System.out.println("Caught ValidationException: " + e.getMessage());
        }

        DataPool pool = seedPool();
        System.out.print("Enter a non-existing user id (e.g., 999): ");
        int id = Integer.parseInt(sc.nextLine().trim());

        try {
            pool.findUserById(id);
        } catch (EntityNotFoundException e) {
            System.out.println("Caught EntityNotFoundException: " + e.getMessage());
        }
    }

    private static DataPool seedPool() {
        DataPool pool = new DataPool();

        pool.addUser(new Freelancer(1, "Diyar", "Kazakhstan", "IT", 4.8));
        pool.addUser(new Employer(2, "Aibek", "Kazakhstan", "TechCorp", "IT"));
        pool.addUser(new Freelancer(3, "Anton", "Russia", "Design", 4.5));

        pool.addJoblisting(new Joblisting(1, "Software Engineer", "TechCorp", "IT", true));
        pool.addJoblisting(new Joblisting(2, "IT designer", "DesignPro", "Design", false));
        pool.addJoblisting(new Joblisting(3, "Developer", "Amigo", "IT", true));

        pool.addPortal(new Portal(1, "FreelanceHub", "www.freelancehub.com", 5000, true));
        pool.addPortal(new Portal(2, "JobFinder", "www.jobfinder.com", 8000, true));

        return pool;
    }
}