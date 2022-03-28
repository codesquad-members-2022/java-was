package servlet;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Map;

import static webserver.RequestHandler.USER_REGISTRY_FORM;

public class CreateUserServlet extends BaseServlet {
    private static final Logger log = LoggerFactory.getLogger(CreateUserServlet.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String resourcePath = request.getPath();

        if (resourcePath.startsWith("/user/create")) {
            User user = createUser(request.getBody());
            String nextPath = "/index.html";

            try {
                DataBase.addUser(user);
                log.debug("[User] : {}", user);
            } catch (RuntimeException e) {
                nextPath = USER_REGISTRY_FORM;
            }

            response.redirection(nextPath);
            return;
        }

        response.forward(resourcePath);
    }

    private User createUser(String resourcePath) {
        String queryString = HttpRequestUtils.getQueryString(resourcePath);
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        return new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
    }
}
