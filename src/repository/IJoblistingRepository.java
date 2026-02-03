package repository;

import domain.Joblisting;

import java.sql.SQLException;
import java.util.List;

public interface IJoblistingRepository {
    void insert(Joblisting j) throws SQLException;

    Joblisting findById(int id) throws SQLException;

    List<Joblisting> findAll() throws SQLException;

    void updateActive(int id, boolean isActive) throws SQLException;

    void deleteById(int id) throws SQLException;

    // Java feature: default interface method
    default boolean existsById(int id) throws SQLException {
        try {
            return findById(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
}