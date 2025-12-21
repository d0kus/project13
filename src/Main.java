import java.util.Arrays;
import java.util.HashSet;

public class Main{
    public static void main(String[] args) {
        Joblisting job1 = new Joblisting(1, "Software Engineer", "TechCorp", "IT", true);
        Joblisting job2 = new Joblisting(2, "IT designer", "DesignPro", "Design", false);
        Joblisting job3 = new Joblisting();
        job3.setId(1);
        job3.setJobTitle("Data Scientist");
        job3.setCompany("DataSolutions");
        job3.setSphere("Data Science");
        job3.setActive(false);
        job3.activate();

        HashSet<Joblisting> set = new HashSet<>();
        set.add(job1);
        set.add(job3);
        System.out.println("Number of unique job listings in the set: " + set.size());
        System.out.println("a.equals b: " + job1.equals(job3));
        System.out.println("a.hashCode(): " + job1.hashCode());
        System.out.println("b.hashCode(): " + job3.hashCode());
        System.out.println("hashset contents" + set);



        Portal portal1 = new Portal(1,"FreelanceHub", "www.freelancehub.com", 5000, true);
        Portal portal2 = new Portal();
        portal2.setId(2);
        portal2.setPortalName("JobConnect");
        portal2.setUrl("www.jobconnect.com");
        portal2.setUsersActive(3000);
        portal2.setWorking(true);

        HashSet<Portal> set2 = new HashSet<>();
        set2.add(portal1);
        set2.add(portal2);
        System.out.println("Number of unique portals in the set: " + set2.size());
        System.out.println("portal1.equals portal2: " + portal1.equals(portal2));
        System.out.println("portal1.hashCode(): " + portal1.hashCode());
        System.out.println("portal2.hashCode(): " + portal2.hashCode());
        System.out.println("hashset contents" + set2);
        System.out.println(set2.contains(new Portal(1, "AnyTitle", "AnyCompany", 1222, true)));

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

        System.out.println("users info: ");
        User[] users = {u1, u2, u3};
        for (User user : users) {
            System.out.println(Arrays.toString(users));
        }

        DataPool pool = new DataPool();

        pool.addUser(u1);
        pool.addUser(u2);
        pool.addUser(u3);

        pool.addJoblisting(job1);
        pool.addJoblisting(job2);
        pool.addJoblisting(job3);

        pool.addPortal(portal1);
        pool.addPortal(portal2);

        System.out.println("findUserById(2): " + pool.findUserById(2));

        System.out.println("Active job listings: " + pool.activeJob());
        System.out.println("Number of active job listings: " + pool.activeJob().size());

        System.out.println("All users:");
        for (User user : pool.getUsers()) {
            System.out.println(user);
        }

        int maxActiveUsers = 0;
        for (Portal p : pool.getPortals()) {
            if (p.getUsersActive() > maxActiveUsers) {
                maxActiveUsers = p.getUsersActive();
            }
        }
        System.out.println("Max active users on portals: " + maxActiveUsers);
        System.out.println("Freelancers sorted by rating: ");
        for (Freelancer f : pool.sortFreelancersByRatingDescManual()) {
            System.out.println(f);
        }
    }
}