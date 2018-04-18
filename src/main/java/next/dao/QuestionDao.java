package next.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.jdbc.KeyHolder;
import next.model.Question;

public class QuestionDao {
    private static final Logger log = LoggerFactory.getLogger(QuestionDao.class);

    public Question insert(Question question) {
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfAnswer)  VALUES (?, ?, ?, ?, ?)";
        KeyHolder holder = new KeyHolder();

        JdbcTemplate.update(sql, holder, question.getWriter(), question.getTitle(),
                question.getContents(), question.getCreatedDate(), question.getCountOfComment());

        Long key = holder.getId();
        log.debug("key : {}", key);

        return findById(key);
    }

    public Question findById(Long id) {
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS WHERE questionId = ?";

        RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"), rs.getString(2),
                rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));

        return JdbcTemplate.queryForObject(sql, rm, id);
    }

    public List<Question> findAll() {
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS";

        RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"), rs.getString(2),
                rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));

        return JdbcTemplate.query(sql, rm);
    }
}
