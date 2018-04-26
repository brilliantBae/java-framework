package next.controller;

import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.UserDao;
import next.model.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserController implements Controller {

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }
        UserDao userDao = new UserDao();

        return new ModelAndView(new JspView("redirect:/users/loginForm")).addObject("users", userDao.findAll());
    }
}
