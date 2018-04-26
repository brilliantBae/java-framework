package core.mvc;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// url 과 서블릿 매핑을 urlPatterns 속성을 통해 해주고 있음.
// 모든 클라이언트 요청을 받는 서블릿
// 서블릿 컨테이너 역할
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc", "next.dao", "next.service");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        log.debug("url : {}", url);

        ModelAndView modelAndView;
        HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(req);
        modelAndView = handlerExecution.handle(req, resp);
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }


}
