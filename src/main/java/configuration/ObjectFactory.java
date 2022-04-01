package configuration;

import model.handler.HandlerFactory;
import model.handler.RequestMapping;
import model.handler.controller.HomeController;
import model.handler.controller.UserCreateController;
import model.handler.controller.UserLoginController;
import webserver.DispatcherServlet;

public class ObjectFactory {
    public static final RequestMapping requestMapping = RequestMapping.getInstance();
    public static final UserLoginController userLoginController = UserLoginController.getInstance();
    public static final UserCreateController userCreateController = UserCreateController.getInstance();
    public static final HomeController homeController = HomeController.getInstance();
    public static final DispatcherServlet dispatcherServlet = DispatcherServlet.getInstance();
    public static final HandlerFactory handlerFactory = HandlerFactory.getInstance();
}
