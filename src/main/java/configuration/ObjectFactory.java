package configuration;

import model.handler.controller.HomeController;

public class ObjectFactory {
    public static final HomeController homeController = HomeController.getInstance();
}
