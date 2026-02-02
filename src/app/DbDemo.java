package app;

import exceptions.EntityNotFoundException;
import service.PortalService;
import service.JoblistingService;
import repository.PortalRepository;
import repository.JoblistingRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class DbDemo {
    private final PortalService portalService = new PortalService(new PortalRepository());
    private final JoblistingService jobService = new JoblistingService(new JoblistingRepository());

    public void run(Scanner sc){
        try {
            portalService.seed();
            jobService.seed();

            System.out.println("Enter Portal ID to find in DB:");
            int portId = sc.nextInt();

            try {
                System.out.println("Found: " + portalService.getById(portId));
                portalService.setWorking(portId, false);
                System.out.println("after setWorking(false) " + portalService.getById(portId));
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("All portals from DB: " + portalService.getAll());
            System.out.println("Enter portal ID to delete from DB:");
            int delPortId = sc.nextInt();
            portalService.deleteById(delPortId);
            System.out.println("Deleted. Now all portals: " + portalService.getAll());


            System.out.println("Enter Joblisting ID to find in DB:");
            int jobId = sc.nextInt();

            try {
                System.out.println("Found: " + jobService.getById(jobId));
                jobService.setActive(jobId, false);
                System.out.println("after setActive(false) " + jobService.getById(jobId));
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("All joblistings from DB: " + jobService.getAll());
            System.out.println("Enter joblisting ID to delete from DB:");
            int delJobId = sc.nextInt();
            jobService.deleteById(delJobId);
            System.out.println("Deleted. Now all joblistings: " + jobService.getAll());

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}