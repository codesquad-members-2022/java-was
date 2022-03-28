package com.riakoader.was.util;

import com.riakoader.was.httpmessage.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    /**
     * 매칭시킨 'HandlerMethod' 가 반환한 결과 값을 OutputStream 을 통해 클라이언트에게 응답한다.
     *
     * @param out
     * @param response
     */
    public static void sendResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.write(response.toByteArray());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
