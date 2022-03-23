package util;

public class RequestLineUtil {

    public static final int URL_INDEX = 1;

    public static String getURL(String requestLine) {
        String[] strList = requestLine.split(" ");
        return strList[URL_INDEX];
    }

}
