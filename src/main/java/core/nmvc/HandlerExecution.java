package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);
    private Object controllerBean;
    private Method method;

    public HandlerExecution(Object controllerBean, Method method) {// handlerExecution 이 class , method 정보 모두 갖고 있을 수 있도록 했다.
        this.controllerBean = controllerBean;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res) {
        // 맵에서 해당 메소드 선택해 그 메소드 실행.
        // 해당 클래스 가져오기
        ModelAndView modelAndView = new ModelAndView();
        try {
            // 해당 메소드의 파라미터들 다 넣어주어야 함.
            modelAndView = (ModelAndView) this.method.invoke(controllerBean, req, res);
        } catch (IllegalAccessException e) {
            log.debug("IllegalAccessException occured " + e.getMessage());
        } catch (InvocationTargetException e) {
            log.debug("InvocationTargetException occured " + e.getMessage());
        }
        return modelAndView;
    }

}
