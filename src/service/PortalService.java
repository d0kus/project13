package service;

import domain.Portal;
import repository.IPortalRepository;
import exceptions.EntityNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class PortalService {
    private final IPortalRepository repo;

    public PortalService(IPortalRepository repo) {
        this.repo = repo;
    }

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

    // NOTE: каскад будет делаться в PortalHandler (там есть доступ к jobService)
    public void setWorking(int id, boolean working) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Portal with id=" + id + " not found");
        }
        repo.updateWorking(id, working);
    }

    public List<Portal> getAll() throws SQLException {
        return repo.findAll();
    }

    public void deleteById(int id) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Portal with id=" + id + " not found");
        }
        repo.deleteById(id);
    }

    public void create(Portal p) throws SQLException {
        repo.insert(p);
    }
}