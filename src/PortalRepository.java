import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortalRepository {

    public void insert(Portal p) throws SQLException {
        String sql = "insert into portals (id, portal_name, url, users_active, working) values (?, ?, ?, ?, ?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getPortalName());
            ps.setString(3, p.getUrl());
            ps.setInt(4, p.getUsersActive());
            ps.setBoolean(5, p.isWorking());
            ps.executeUpdate();
        }
    }

    public Portal findById(int id) throws SQLException {
        String sql = "select id, portal_name, url, users_active, working from portals where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Portal p = new Portal();
                p.setId(rs.getInt("id"));
                p.setPortalName(rs.getString("portal_name"));
                p.setUrl(rs.getString("url"));
                p.setUsersActive(rs.getInt("users_active"));
                p.setWorking(rs.getBoolean("working"));
                return p;
            }
        }
    }

    public List<Portal> findAll() throws SQLException {
        String sql = "select id, portal_name, url, users_active, working from portals order by id";
        List<Portal> result = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Portal p = new Portal();
                p.setId(rs.getInt("id"));
                p.setPortalName(rs.getString("portal_name"));
                p.setUrl(rs.getString("url"));
                p.setUsersActive(rs.getInt("users_active"));
                p.setWorking(rs.getBoolean("working"));
                result.add(p);
            }
        }
        return result;
    }

    public void updateWorking(int id, boolean working) throws SQLException {
        String sql = "update portals set working = ? where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, working);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
    public void updateUsersActive(int id, int usersActive) throws SQLException {
        String sql = "update portals set users_active = ? where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usersActive);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "delete from portals where id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public void deleteAll() throws SQLException {
        String sql = "delete from portals";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
}