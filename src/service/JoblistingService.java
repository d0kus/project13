package service;

import domain.Joblisting;
import domain.Portal;
import exceptions.EntityNotFoundException;
import exceptions.ValidationException;
import repository.IJoblistingRepository;

import java.sql.SQLException;
import java.util.List;

public class JoblistingService {
    private final IJoblistingRepository repo;

    public JoblistingService(IJoblistingRepository repo) {
        this.repo = repo;
    }

    public List<Joblisting> getAll() throws SQLException {
        return repo.findAll();
    }

    public Joblisting getById(int id) throws SQLException {
        return repo.findById(id);
    }

    public void create(Joblisting j, Portal portal) throws SQLException {
        if (portal == null) {
            throw new ValidationException("portalId does not exist");
        }
        if (!portal.isWorking()) {
            throw new ValidationException("Cannot create joblisting: portal is not working");
        }
        repo.insert(j);
    }

    public void setActive(int id, boolean active) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
        }
        repo.updateActive(id, active);
    }

    public void deleteById(int id) throws SQLException {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
        }
        repo.deleteById(id);
    }

    public void deactivateByPortalId(int portalId) throws SQLException {
        repo.deactivateByPortalId(portalId);
    }
}