package configuration;

import db.DataBase;
import model.handler.controller.*;
import util.RandomUtil;
import webserver.servlet.ConnectionPool;
import webserver.servlet.DispatcherServlet;

public class ObjectFactory {
    public static final HttpHeaders httpHeaders = HttpHeaders.getInstance();
    public static final RandomUtil randomUtil = RandomUtil.getInstance();
    public static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    public static final RequestMapping requestMapping = RequestMapping.getInstance();
    public static final UserLoginController userLoginController = UserLoginController.getInstance();
    public static final UserCreateController userCreateController = UserCreateController.getInstance();
    public static final HomeController homeController = HomeController.getInstance();
    public static final DispatcherServlet dispatcherServlet = DispatcherServlet.getInstance();
    public static final HandlerFactory handlerFactory = HandlerFactory.getInstance();
    public static final DataBase dataBase = DataBase.getInstance();
}
