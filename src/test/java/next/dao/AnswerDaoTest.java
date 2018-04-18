package next.dao;

import core.jdbc.ConnectionManager;
import next.model.Answer;
import next.model.Result;
import next.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.junit.Assert.*;

public class AnswerDaoTest {

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }


    @Test
    public void insert() throws Exception {
        Answer answer = new Answer("jiwon", "contents", 1L);
        AnswerDao answerDao = new AnswerDao();
        Answer created = answerDao.insert(answer);
        assertEquals("jiwon", created.getWriter());
        assertEquals(6, created.getAnswerId());
    }

    @Test
    public void findById() throws Exception {
        AnswerDao answerDao = new AnswerDao();
        Answer answer = answerDao.findById(1L);
        String firstWriter = "eungju";
        assertEquals(firstWriter, answer.getWriter());
        assertEquals(1L, answer.getAnswerId());
    }

    @Test
    public void findAll() throws Exception {
        AnswerDao answerDao = new AnswerDao();
        List<Answer> answers = answerDao.findAll();
        assertEquals(5, answers.size());
    }

    @Test
    public void findByQuestionId() throws Exception {
        AnswerDao answerDao = new AnswerDao();
        List<Answer> answers = answerDao.findByQuestionId(7L);
        assertEquals(2, answers.size());
    }

    @Test
    public void deleteAnswer() throws Exception {
        AnswerDao answerDao = new AnswerDao();
        Result rs = answerDao.delete(5L);
        answerDao.delete(4L);

        assertEquals(3, answerDao.findAll().size());
        assertEquals(true, rs.isStatus());
    }
}