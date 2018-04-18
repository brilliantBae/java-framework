package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

    ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws IOException;
}
