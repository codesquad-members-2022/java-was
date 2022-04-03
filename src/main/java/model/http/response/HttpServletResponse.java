package model.http.response;

import model.http.session.Cookie;

import java.io.DataOutputStream;

public interface HttpServletResponse {
    DataOutputStream getDataOutputStream();

    void responseHeader(int length, String type);

    void responseBody(byte[] body);

    void responseHeaderRedirection(int length, String type, String location);

    void responseHeaderLoginRedirection(int lengthOfBodyContent, String type, String location, Cookie cookie);
}
