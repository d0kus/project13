package service;

import domain.Portal;
import repository.PortalRepository;

import java.sql.SQLException;
import java.util.List;

public class PortalService {
    private final PortalRepository repo = new PortalRepository();

    public void seed() throws SQLException {
        try {
            repo.insert(new Portal(1, "Indeed", "https://www.indeed.com", 20000, true));
            repo.insert(new Portal(2, "LinkedIn", "https://www.linkedin.com", 70000, true));
            repo.insert(new Portal(3, "Monster", "https://www.monster.com", 9500, false));
            System.out.println("Inserted portals 1, 2, 3 into DB.");
        } catch (Exception e) {
            System.out.println("Insert skipped/failed(maybe already exist): " + e.getMessage());
        }
    }
    public Portal getById(int id) throws SQLException {
        return repo.findById(id);
    }

    public void setWorking(int id, boolean working) throws SQLException {
        repo.updateWorking(id, working);
    }

    public List<Portal> getAll() throws SQLException {
        return repo.findAll();
    }

    public void deleteById(int id) throws SQLException {
        repo.deleteById(id);
    }
}
