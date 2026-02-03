package service;

import domain.Joblisting;
import repository.IJoblistingRepository;
import exceptions.EntityNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class JoblistingService {
    private final IJoblistingRepository repo;

    public JoblistingService(IJoblistingRepository repo) {
        this.repo = repo;
    }

    public void seed() throws SQLException {
        try {
            repo.insert(new Joblisting(101, "Software Engineer", "TechCorp", "IT", true));
            repo.insert(new Joblisting(102, "Data Analyst", "DataSolutions", "Analytics", true));
            repo.insert(new Joblisting(103, "Project Manager", "BusinessInc", "Management", false));
            System.out.println("Inserted joblistings 101, 102, 103 into DB.");
        } catch (Exception e) {
            System.out.println("Insert skipped/failed(maybe already exist): " + e.getMessage());
        }
    }
    public Joblisting getById(int id) throws SQLException {
        return repo.findById(id);
    }

    public void setActive(int id, boolean active) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
        }
        repo.updateActive(id, active);
    }

    public List<Joblisting> getAll() throws SQLException {
        return repo.findAll();
    }

    public void deleteById(int id) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
        }
        repo.deleteById(id);
    }

    public void create(Joblisting j) throws java.sql.SQLException {
        repo.insert(j);
    }
}
