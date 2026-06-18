package com.example.secopsdemo.repository;

import com.example.secopsdemo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findByUsernameUnsafe(String username) {
        // VULN: SQL Injection - string concatenation instead of prepared statements.
        String sql = "SELECT id, username, password, role FROM users WHERE username = '" + username + "'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        ));
    }

    public int updateRoleUnsafe(String username, String role) {
        // VULN: SQL Injection in update path.
        String sql = "UPDATE users SET role = '" + role + "' WHERE username = '" + username + "'";
        return jdbcTemplate.update(sql);
    }
}
