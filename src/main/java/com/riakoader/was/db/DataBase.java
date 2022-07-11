package com.riakoader.was.db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import com.riakoader.was.model.User;

public class DataBase {

    private static final Map<String, User> users = Maps.newHashMap();

    static {
        addUser(new User("ader", "1234", "앗어", "ader@gmail.com"));
        addUser(new User("naneun", "1234", "리악고", "naneun@gmail.com"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
