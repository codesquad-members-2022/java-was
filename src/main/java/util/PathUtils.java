package util;

public class PathUtils {

    private PathUtils(){};

    public static String getPath(String... args) {
        return String.join("", args);
    }

}
