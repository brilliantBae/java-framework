package next.dao;

import java.util.List;

import next.model.User;

public class UserDao {

    public void insert(User user) throws DataAccessException {
        JdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws DataAccessException {
        JdbcTemplate.update("UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(),
                user.getName(), user.getEmail(), user.getUserId());
    }

    public void delete(String userId) throws DataAccessException {
        JdbcTemplate.update("DELETE FROM USERS WHERE userid=?", userId);
    }

    public List<User> findAll() throws DataAccessException {
        RowMapper<User> rm = rs ->
                new User(rs.getString("userId"), rs.getString(2), rs.getString(3), rs.getString(4));
        return JdbcTemplate.query("SELECT userId, password, name, email FROM USERS", rm);
    }

    public User findByUserId(String userId) throws DataAccessException {
        RowMapper<User> rm = rs ->
                new User(rs.getString("userId"), rs.getString(2), rs.getString(3), rs.getString(4));
        return JdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?", rm, userId);
    }

}