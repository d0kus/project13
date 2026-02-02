package service;

import domain.Joblisting;
import repository.IJoblistingRepository;
import repository.JoblistingRepository;

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
        repo.updateActive(id, active);
    }

    public List<Joblisting> getAll() throws SQLException {
        return repo.findAll();
    }

    public void deleteById(int id) throws SQLException {
        repo.deleteById(id);
    }
}
