package next.dao;

import core.annotation.Inject;

import core.annotation.Repository;
import core.jdbc.KeyHolder;
import next.model.Answer;
import next.model.Result;

import java.util.List;

@Repository
public class AnswerDao {
    private JdbcTemplate jdbcTemplate;

    @Inject
    public AnswerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Answer insert(Answer answer) {
        String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";
        KeyHolder holder = new KeyHolder();
        // 로그인한 사용자 넣어주기.
        jdbcTemplate.update(sql, holder, answer.getWriter(), answer.getContents(),
                answer.getCreatedDate(), answer.getQuestionId());

        return findById(holder.getId());
    }

    public Answer findById(Long id) {
        String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId = ?";
        RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"), rs.getString(2),
                rs.getString(3), rs.getDate(4), rs.getLong(5));
        return jdbcTemplate.queryForObject(sql, rm, id);
    }

    public List<Answer> findAll() throws DataAccessException {
        RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"), rs.getString(2),
                rs.getString(3), rs.getDate(4), rs.getLong(5));
        return jdbcTemplate.query("SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS", rm);
    }

    public List<Answer> findByQuestionId(Long questionId) {
        RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"), rs.getString(2),
                rs.getString(3), rs.getDate(4), rs.getLong(5));
        return jdbcTemplate.query("SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId = ?", rm, questionId);
    }

    public Result delete(Long id) {
        String sql = "DELETE FROM ANSWERS WHERE answerId = ?";
        jdbcTemplate.update(sql, id);
        if (findById(id) == null) return Result.ok();
        return Result.fail("Error message");
    }
}
