package repository;

import domain.Joblisting;
import exceptions.EntityNotFoundException;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class JoblistingRepository implements IJoblistingRepository {

    public void insert(Joblisting j) throws SQLException {
        String sql = "insert into joblistings (id, job_title, company, sphere, is_active) values (?, ?, ?, ?, ?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, j.getId());
            ps.setString(2, j.getJobTitle());
            ps.setString(3, j.getCompany());
            ps.setString(4, j.getSphere());
            ps.setBoolean(5, j.isActive());
            ps.executeUpdate();
        }
    }
    public Joblisting findById(int id) throws SQLException {
        String sql = "select id, job_title, company, sphere, is_active from joblistings where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Joblisting with id=" + id + " not found");
                }

                Joblisting j = new Joblisting();
                j.setId(rs.getInt("id"));
                j.setJobTitle(rs.getString("job_title"));
                j.setCompany(rs.getString("company"));
                j.setSphere(rs.getString("sphere"));
                j.setActive(rs.getBoolean("is_active"));
                return j;
            }
        }
    }
    public List<Joblisting> findAll() throws SQLException {
        String sql = "select id, job_title, company, sphere, is_active from joblistings order by id";
        List<Joblisting> result = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Joblisting j = new Joblisting();
                j.setId(rs.getInt("id"));
                j.setJobTitle(rs.getString("job_title"));
                j.setCompany(rs.getString("company"));
                j.setSphere(rs.getString("sphere"));
                j.setActive(rs.getBoolean("is_active"));
                result.add(j);
            }
        }

        return result;
    }
    public void updateActive(int id, boolean isActive) throws SQLException {
        String sql = "update joblistings set is_active = ? where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "delete from joblistings where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
