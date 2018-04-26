package core.nmvc;

import java.lang.reflect.Method;
import java.util.*;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.*;
import core.di.factory.BeanFactory;
import next.exception.NoConfigurationFileException;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private Class<?> configuration;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object configurationFilePackage) {
        this.configuration = detectConfigurationFile(configurationFilePackage);
        this.basePackage = configuration.getAnnotation(ComponentScan.class).basePackages();
    }

    private Class<?> detectConfigurationFile(Object configurationFilePackage) {
        return new Reflections(configurationFilePackage).getTypesAnnotatedWith(Configuration.class).
                stream().findFirst().orElseThrow(() -> new NoConfigurationFileException("There is no configuration file"));
    }

    public void initialize() {
        // annotation 붙은 클래스들 빈으로 모두 등록
        ClassPathBeanScanner cpbs = new ClassPathBeanScanner(basePackage);
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(configuration);
        BeanDefinition beanDefinition = new BeanDefinition(cbs, cpbs);
        BeanFactory beanFactory = new BeanFactory(beanDefinition);

        beanFactory.initialize();

        cpbs.getControllers().forEach(annotatedBean -> {
            // 1. @RequestMapping 붙은 method 만 필터.
            Object bean = beanFactory.getBean(annotatedBean.getClazz());
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
    }
}
