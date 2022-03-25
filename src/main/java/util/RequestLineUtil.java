package util;

public class RequestLineUtil {

    public static final int METHOD_INDEX = 0;
    public static final int URL_INDEX = 1;

    public static String getURL(String requestLine) {
        String[] strList = requestLine.split(" ");
        return strList[URL_INDEX];
    }

    public static String getMethodType(String requestLine) {
        String[] strList = requestLine.split(" ");
        return strList[METHOD_INDEX];
    }

}
