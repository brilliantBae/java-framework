package next.dao;

import core.jdbc.ConnectionManager;
import next.model.Answer;
import next.model.Question;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.junit.Assert.*;

public class QuestionDaoTest {
    QuestionDao questionDao;

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        questionDao = new QuestionDao();
    }


    @Test
    public void insert() throws Exception {
        Question question = new Question("writer", "title", "contents");
        Question created = questionDao.insert(question);
        assertEquals("writer", created.getWriter());
        assertEquals(9, created.getQuestionId());
    }

    @Test
    public void findById() throws Exception {
        Question question = questionDao.findById(1L);
        String firstWriter = "자바지기";
        assertEquals(firstWriter, question.getWriter());
    }

    @Test
    public void findAll() throws Exception {
        List<Question> list = questionDao.findAll();
        assertEquals(8, list.size());
    }


}