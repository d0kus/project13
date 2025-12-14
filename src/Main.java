public class Main{
    public static void main(String[] args) {
        joblisting job1 = new joblisting("Software Engineer", "TechCorp", "IT", true);
        joblisting job2 = new joblisting("IT designer", "DesignPro", "Design", false);

        portal portal1 = new portal("FreelanceHub", "www.freelancehub.com", 5000, true);
        portal portal2 = new portal();
        portal2.setPortalName("JobConnect");
        portal2.setUrl("www.jobconnect.com");
        portal2.setUsersActive(3000);
        portal2.setWorking(true);

        freelancer freelancer1 = new freelancer("Diyar", "IT", 4.5);
        freelancer freelancer2 = new freelancer("Aibek", "Design", 4.3);

        System.out.println("Portal info: ");
        portal1.printinfo();
        portal2.printinfo();
        System.out.println("JobListing info: ");
        job1.printinfo();
        job2.printinfo();
        System.out.println("Freelancer info: ");
        freelancer1.showinfo();
        freelancer2.showinfo();
        System.out.println("Freelancer rating comparison: ");
        int comparisonResult = freelancer1.compareRating(freelancer2);
        if (comparisonResult > 0) {
            System.out.println("freelancer1 has a higher rating than freelancer2.");
        }
        else if (comparisonResult < 0) {
            System.out.println("freelancer2 has a higher rating than freelancer1.");
        } else {
            System.out.println("Both freelancers have the same rating.");
        }

        System.out.println("Count active job listings: ");
        int activeJobs = 0;
        if (job1.isActive()) {
            activeJobs++;
        }
        if (job2.isActive()) {
            activeJobs++;
        }
        System.out.println("Number of active job listings: " + activeJobs);

        System.out.println("Max active users on portals: ");
        portal[] portals = {portal1, portal2};
        int maxActiveUsers = 0;
        for(int i = 0; i < portals.length; i++) {
            int currentUsers = portals[i].getUsersActive();
            System.out.println("Portal " + (i + 1) + " has " + currentUsers + " active users.");
            if(currentUsers > maxActiveUsers) {
                maxActiveUsers = currentUsers;
            }
        }
        System.out.println("Max active users on portals: " + maxActiveUsers);

    }
}