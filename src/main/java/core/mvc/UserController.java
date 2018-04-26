package core.mvc;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import next.controller.UserSessionUtils;
import next.model.User;
import next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest req, HttpServletResponse res) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }
        return new ModelAndView(new JspView("redirect:/users/loginForm")).addObject("users", userService.findAll());
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse res) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        userService.insert(user);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        User user = userService.findByUserId(userId);

        if (user == null) {
            return new ModelAndView(new JspView("/user/login.jsp")).addObject("loginFailed", true);
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            // session 에 세션아이디 이름으로 user 등록
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/"));
        }
        // 비밀번호 틀렸을 때
        return new ModelAndView(new JspView("/user/login.jsp")).addObject("loginFailed", true);
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse res) {
        User user = userService.findByUserId(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));

        log.debug("Update User : {}", updateUser);
        userService.update(updateUser);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/users/profile")
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");

        User user = userService.findByUserId(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        return new ModelAndView(new JspView("/user/profile.jsp")).addObject("user", user);
    }

    @RequestMapping(value = "/users/form")
    public ModelAndView createForm(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView createLoginForm(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }
}
