package service;

import db.DataBase;
import model.User;

public class UserService {

    private static UserService userService;

    private UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public void register(User user) {
        DataBase.findUserById(user.getUserId())
                .ifPresent(findUser -> {
                    throw new IllegalArgumentException("중복된 유저입니다.");
                });

        DataBase.addUser(user);
    }

    public User login(String userId, String password) {
        User user = DataBase.findUserById(userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("아이디를 다시 확인해주세요.");
                });

        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("패스워드를 다시 확인해주세요.");
        }
        return user;
    }
}
