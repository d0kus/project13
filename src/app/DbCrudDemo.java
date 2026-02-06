package app;

import domain.Portal;
import exceptions.EntityNotFoundException;
import repository.IPortalRepository;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class DbCrudDemo {
    private final IPortalRepository portalRepo;

    public DbCrudDemo(IPortalRepository portalRepo) {
        this.portalRepo = portalRepo;
    }

    public void run(Scanner sc) {
        System.out.println("== DB CRUD demo (portals) ==");

        // choose an ID unlikely to collide
        int id = 9000 + new Random().nextInt(900);

        Portal p = new Portal(id, "DemoPortal", "https://demo.example", 123, true);

        try {
            // 1) CREATE
            System.out.println("\n1) CREATE");
            portalRepo.insert(p);
            System.out.println("Inserted portal id=" + id);

            // 2) READ
            System.out.println("\n2) READ");
            Portal fromDb = portalRepo.findById(id);
            System.out.println("Read from DB: " + fromDb);

            // 3) UPDATE
            System.out.println("\n3) UPDATE");
            System.out.println("Set working=false for id=" + id);
            portalRepo.updateWorking(id, false);

            Portal updated = portalRepo.findById(id);
            System.out.println("After update: " + updated);

            // 4) DELETE
            System.out.println("\n4) DELETE");
            portalRepo.deleteById(id);
            System.out.println("Deleted portal id=" + id);

            System.out.println("\n5) READ after delete (should fail)");
            try {
                portalRepo.findById(id);
                System.out.println("ERROR: expected not found, but record still exists!");
            } catch (EntityNotFoundException e) {
                System.out.println("OK (not found): " + e.getMessage());
            }

            System.out.println("\nCRUD cycle finished.");

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Press Enter to continue ---");
        sc.nextLine();
    }
}