package next.controller;

import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.UserDao;
import next.model.Response;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfileController implements Controller {
    private static final long serialVersionUID = 1L;

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String userId = req.getParameter("userId");

        UserDao userDao = new UserDao();
        User user = userDao.findByUserId(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        return new ModelAndView(new JspView("/user/profile.jsp")).addObject("user", user);
    }
}
