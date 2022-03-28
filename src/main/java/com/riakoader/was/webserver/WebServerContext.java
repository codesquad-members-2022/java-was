package com.riakoader.was.webserver;

import com.riakoader.was.config.HandlerMethodRegistry;
import com.riakoader.was.handler.HandlerMethodMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebServerContext {

    private static volatile WebServerContext webServerContext;

    private final HandlerMethodRegistry handlerMethodRegistry;

    private final HandlerMethodMapper handlerMethodMapper;

    private WebServerContext() {
        handlerMethodRegistry = HandlerMethodRegistry.getInstance();
        handlerMethodMapper = HandlerMethodMapper.getInstance();
    }

    public static WebServerContext getInstance() {
        if (webServerContext == null) {
            webServerContext = new WebServerContext();
        }
        return webServerContext;
    }

    public Object getBean(String beanName) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class<?> context = Class.forName(this.getClass().getTypeName());
        Field field = context.getDeclaredField(beanName);

        Class<?> clazz = Class.forName(field.getType().getTypeName());
        Method method = clazz.getDeclaredMethod("getInstance");

        return method.invoke(null);
    }
}
