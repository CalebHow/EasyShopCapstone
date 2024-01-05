package org.yearup.data.mysql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;
@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {
    private final JdbcTemplate jdbcTemplate;
    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getProfile(String name) {
        return null;
    }

    @Override
    public Profile getByUserId(String userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new ProfileRowMapper());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve profile by user ID", e);
        }
    }@Override
    public Profile update(String userName, Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, email = ?, " +
                "address = ?, city = ?, state = ?, zip = ? WHERE userName = ?";
        try {
            jdbcTemplate.update(sql, profile.getFirstName(), profile.getLastName(), profile.getPhone(),
                    profile.getEmail(), profile.getAddress(), profile.getCity(), profile.getState(),
                    profile.getZip(), userName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update profile", e);
        }
        return profile;
    }
    private static class ProfileRowMapper implements org.springframework.jdbc.core.RowMapper<Profile> {
        @Override
        public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
            Profile profile = new Profile();
            profile.setUserId(Integer.parseInt(rs.getString("userName")));
            profile.setFirstName(rs.getString("first_name"));
            profile.setLastName(rs.getString("last_name"));
            profile.setPhone(rs.getString("phone"));
            profile.setEmail(rs.getString("email"));
            profile.setAddress(rs.getString("address"));
            profile.setCity(rs.getString("city"));
            profile.setState(rs.getString("state"));
            profile.setZip(rs.getString("zip"));
            return profile;
        }
    }
}
