package core.di.factory;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import core.nmvc.Bean;
import core.nmvc.BeanDefinition;
import core.nmvc.ClassPathBean;
import core.nmvc.ConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class BeanFactory { // 프레임워크의 bean 들을 설정해주는 클래스
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Bean> beanCandidates;
    private BeanDefinition beanDefinition;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
        this.beanCandidates = this.beanDefinition.getBeanCandidates();

    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) Optional.ofNullable(this.beans.get(requiredType)).orElse(null);
    }

    // 프레임워크가 시작되면서 프레임워크가 관리해야 할 인스턴스들(= bean)을 생성하는 부분
    public void initialize() {
        // DI 실행
        this.beanDefinition.getBeanCandidates().forEach(candidate -> {
            try {

                this.beans.put(candidate.getClazz(), setField(candidate));

            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        this.beans.forEach((clazz, object) -> logger.debug("beans : {}", clazz, object));
    }

    private Set<Class<?>> getClasses() {
        return this.beanCandidates.stream().map(Bean::getClazz).collect(Collectors.toSet());
    }

    private Bean find(Class<?> clazz) {
        return this.beanCandidates.stream().filter(b -> b.getClazz().equals(clazz)).findFirst().orElse(null);
    }

    private Object setField(Bean bean) throws IllegalAccessException, InstantiationException, InvocationTargetException {// 재귀로 구현
        if (bean instanceof ClassPathBean) {
            ClassPathBean classPathBean = (ClassPathBean) bean;
            return classPathBean.instantiate(getObjects(classPathBean.getParameters()));
        }
        ConfigurationBean configBean = (ConfigurationBean) bean;
        return configBean.instantiate(getObjects(configBean.getParameters()));
    }

    private List<Object> getObjects(List<Parameter> params) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> args = new ArrayList<>();
        for (Parameter p : params) {
            args.add(inject(p));
        }
        return args;
    }

    public Object inject(Parameter param) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (getClasses().contains(param.getType())) {
            // 인터페이스인데도 빈 후보로 등록된 경우
            if (param.getType().isInterface()) {
                ConfigurationBean configBean = (ConfigurationBean) find(param.getType());
                return configBean.getBeanMethod().invoke(beanDefinition.instantiateConfiguration());
            }
            return setField(find(param.getType()));
        }
        // 인터페이스라서 후보로 등록되어있지 않은경우
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(param.getType(), this.beanCandidates);
        return setField(find(concreteClass));
    }
}
