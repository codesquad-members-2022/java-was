package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MyHttpRequest;
import webserver.RequestHandler;

import java.util.Map;

public class SignUpController implements MyController{

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public String process(MyHttpRequest request) {
        Map<String, String> paramMap = request.getParamMap();

        if (DataBase.findUserById(paramMap.get("userId")) != null) {
            return "user/signup_failed";
        }

        User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
        DataBase.addUser(user);
        log.info("user={}",user);

        return "redirect:/";
    }
}
