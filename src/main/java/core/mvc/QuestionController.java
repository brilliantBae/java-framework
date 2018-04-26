package core.mvc;

import core.annotation.Controller;
import core.annotation.Inject;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.service.AnswerService;
import next.service.QuestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    private QuestionService questionService;

    private AnswerService answerService;

    @Inject
    public QuestionController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @RequestMapping(value = "/qna/form")
    public ModelAndView questionForm(HttpServletRequest req, HttpServletResponse res) throws IOException {
        return new ModelAndView(new JspView("/qna/form.jsp"));
    }

    @RequestMapping(value = "/qna", method = RequestMethod.POST)
    public ModelAndView createQuestion(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Question question = new Question(req.getParameter("writer"), req.getParameter("title"), req.getParameter("contents"));
        log.debug("Question : {}", question);

        questionService.insert(question);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/qna")
    public ModelAndView showQuestion(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // questions
        Long questionId = Long.parseLong(req.getParameter("questionId"));
        Question question = questionService.findById(questionId);

        req.setAttribute("question", question);
        // answers

        return new ModelAndView(new JspView("/qna/show.jsp")).addObject("answers", answerService.findByQuestionId(questionId));
    }
}
