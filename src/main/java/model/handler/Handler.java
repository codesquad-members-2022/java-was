package model.handler;

import model.request.HttpServletRequest;
import model.response.HttpServletResponse;

import java.io.IOException;

public interface Handler {
    void service(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
