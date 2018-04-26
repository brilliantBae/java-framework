package next.controller;

import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateQuestionController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(CreateQuestionController.class);

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Question question = new Question(req.getParameter("writer"), req.getParameter("title"), req.getParameter("contents"));
        log.debug("Question : {}", question);

        QuestionDao questionDao = new QuestionDao();
        questionDao.insert(question);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
