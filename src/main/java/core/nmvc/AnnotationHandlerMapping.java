package core.nmvc;

import java.lang.reflect.Method;
import java.util.*;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.di.factory.BeanFactory;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        // annotation 붙은 클래스들 빈으로 모두 등록
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = new BeanFactory(beanScanner.getBeans());

        beanFactory.initialize();

        beanScanner.getControllers().forEach(annotatedClass -> {
            // 1. @RequestMapping 붙은 method 만 필터.
            Object bean = beanFactory.getBean(annotatedClass);
            List<Method> annotatedMethods = getMethods(bean.getClass());
            // 2. 필터한 메소드 맵에 넣는다.
            annotatedMethods.forEach(m -> {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(bean, m));
            });
        });
    }

    private List<Method> getMethods(Class<?> annotatedClass) {
        return Arrays.stream(annotatedClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(RequestMapping.class)
        ).collect(Collectors.toList());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {// 해당 url 과 http method 에 해당하는 handlerExecution 을 가져옴.
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
        // handlerExecution.handle(); -> requestMapping 달린 해당 메소드 실행.
    }
}
