package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.UserDao;
import next.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.User;

import java.io.IOException;

public class CreateUserController implements Controller {
    // 서블릿으로 구현된 것을 컨트롤러로 변경한다.
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        UserDao userDao = new UserDao();
        userDao.insert(user);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
