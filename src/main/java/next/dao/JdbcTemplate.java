package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate {

    public static void update(String query, PreparedStatementSetter pss) throws DataAccessException {// 변하지 않는 부분
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pss.setValues(pstmt);
            // implement spl statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public static void update(String query, Object... objects) throws DataAccessException {
        PreparedStatementSetter pss = pstmt -> setValues(pstmt, objects);

        update(query, pss);
    }

    private static void setValues(PreparedStatement pstmt, Object[] objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }

    public static <T> List<T> query(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws DataAccessException {
        List<T> objects = new ArrayList<>();
        try (Connection dbConn = ConnectionManager.getConnection();
             PreparedStatement pstmt = dbConn.prepareStatement(sql)
        ) {
            pss.setValues(pstmt);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                T object = rm.mapRow(rs);
                objects.add(object);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return objects;
    }

    public static <T> List<T> query(String sql, RowMapper<T> rm, Object... objects) throws DataAccessException {
       PreparedStatementSetter pss = pstmt -> setValues(pstmt, objects);
       return query(sql, rm, pss);
    }

    public static <T> T queryForObject(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws DataAccessException {
        return query(sql, rm, pss).get(0);
    }

    public static <T> T queryForObject(String sql, RowMapper<T> rm, Object... objects) throws DataAccessException {
        // object -> pss
        PreparedStatementSetter pss = pstmt -> setValues(pstmt, objects);
        return queryForObject(sql, rm, pss);
    }

}
