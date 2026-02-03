package repository;

import domain.Portal;

import java.sql.SQLException;
import java.util.List;

public interface IPortalRepository {
    void insert(Portal p) throws SQLException;

    Portal findById(int id) throws SQLException;

    List<Portal> findAll() throws SQLException;

    void updateWorking(int id, boolean working) throws SQLException;

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