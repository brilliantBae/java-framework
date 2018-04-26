package next.controller;

import core.mvc.Controller;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.AnswerDao;
import next.model.Answer;
import next.model.Response;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AddAnswerController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // create Answer object using data from Http request

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(UserSessionUtils.USER_SESSION_KEY);
        String questionId = req.getParameter("questionId");
        log.debug("questionId:{}", questionId);

        Answer answer = new Answer(user.getUserId(), req.getParameter("contents"), Long.parseLong(req.getParameter("questionId")));

        log.debug("answer : {}", answer.toString());
        AnswerDao answerDao = new AnswerDao();
        Answer savedAnswer = answerDao.insert(answer);

        return new ModelAndView(new JsonView()).addObject("answer", savedAnswer);
    }

}
