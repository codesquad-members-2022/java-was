package configuration;

import model.handler.HandlerFactory;
import model.handler.RequestMapping;
import model.handler.controller.HomeController;

public class ObjectFactory {
    public static final RequestMapping requestMapping = RequestMapping.getInstance();
    public static final HomeController homeController = HomeController.getInstance();
    public static final HandlerFactory handlerFactory = HandlerFactory.getInstance();
}
