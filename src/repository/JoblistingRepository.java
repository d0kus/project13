package repository;

import domain.Joblisting;
import exceptions.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoblistingRepository implements IJoblistingRepository {

    @Override
    public void insert(Joblisting j) throws SQLException {
        String sql = "insert into joblistings (id, portal_id, job_title, company, sphere, active) values (?, ?, ?, ?, ?, ?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, j.getId());
            ps.setInt(2, j.getPortalId());
            ps.setString(3, j.getJobTitle());
            ps.setString(4, j.getCompany());
            ps.setString(5, j.getSphere());
            ps.setBoolean(6, j.isActive());
            ps.executeUpdate();
        }
    }

    @Override
    public Joblisting findById(int id) throws SQLException {
        String sql = "select id, portal_id, job_title, company, sphere, active from joblistings where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
                }

                Joblisting j = new Joblisting();
                j.setId(rs.getInt("id"));
                j.setPortalId(rs.getInt("portal_id"));
                j.setJobTitle(rs.getString("job_title"));
                j.setCompany(rs.getString("company"));
                j.setSphere(rs.getString("sphere"));
                j.setActive(rs.getBoolean("active"));
                return j;
            }
        }
    }

    @Override
    public List<Joblisting> findAll() throws SQLException {
        String sql = "select id, portal_id, job_title, company, sphere, active from joblistings order by id";
        List<Joblisting> result = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Joblisting j = new Joblisting();
                j.setId(rs.getInt("id"));
                j.setPortalId(rs.getInt("portal_id"));
                j.setJobTitle(rs.getString("job_title"));
                j.setCompany(rs.getString("company"));
                j.setSphere(rs.getString("sphere"));
                j.setActive(rs.getBoolean("active"));
                result.add(j);
            }
        }

        return result;
    }

    @Override
    public void updateActive(int id, boolean isActive) throws SQLException {
        String sql = "update joblistings set active = ? where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
            }
        }
    }

    @Override
    public void deactivateByPortalId(int portalId) throws SQLException {
        String sql = "update joblistings set active = false where portal_id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, portalId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "delete from joblistings where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
            }
        }
    }
}