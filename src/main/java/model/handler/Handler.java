package model.handler;

import model.http.request.HttpServletRequest;
import model.http.response.HttpServletResponse;

import java.io.IOException;

public interface Handler {
    void service(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
