package com.riakoader.was.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanFactory {

    public Object getBean(String beanName) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class<?> context = Class.forName(this.getClass().getTypeName());
        Field field = context.getDeclaredField(beanName);

        Class<?> clazz = Class.forName(field.getType().getTypeName());
        Method method = clazz.getDeclaredMethod("getInstance");

        return method.invoke(null);
    }
}
