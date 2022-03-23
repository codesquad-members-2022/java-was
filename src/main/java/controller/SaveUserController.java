package controller;

import java.util.Map;

import db.DataBase;
import model.User;

public class SaveUserController implements MyController {

    @Override
    public String process(Map<String, String> paramMap) {
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");
        String name = paramMap.get("name");
        String email = paramMap.get("email");
        User user = new User(userId, password, name, email);

        DataBase.addUser(user);
        return "index.html";
    }
}
