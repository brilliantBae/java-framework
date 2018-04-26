package core.mvc;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import next.dao.QuestionDao;
import next.model.Question;
import next.service.QuestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private QuestionService questionService;

    @Inject
    public HomeController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Question> questions = questionService.findAll();
        log.debug("questions : {}", questions.toString());

        return new ModelAndView(new JspView("home.jsp")).addObject("questions", questions);
    }

}
