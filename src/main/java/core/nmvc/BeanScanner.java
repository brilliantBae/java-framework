package core.nmvc;

import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BeanScanner {
    private Object[] basePackage;

    BeanScanner(Object... basePackage){
        this.basePackage = basePackage;
    }
    Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Set<Class<?>> getServices() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Service.class);
    }

    private Set<Class<?>> getRepositories() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Repository.class);
    }

    Set<Class<?>> getBeans() {
        return Stream.concat(
                Stream.concat(getControllers().stream(), getServices().stream()),
                getRepositories().stream()).collect(Collectors.toSet());
    }
}
