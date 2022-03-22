package util;

public class RequestLineUtil {

    public static final int FIRST = 1;

    public static String from(String firstLine) {
        String[] strList = firstLine.split(" ");
        return strList[FIRST];
    }

}
