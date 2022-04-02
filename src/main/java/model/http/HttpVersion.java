package model.http;

import java.util.Arrays;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1"),
    HTTP_1_2("HTTP/1.2");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public static HttpVersion of(String version) {
        return Arrays.stream(values())
                .filter(httpVersion -> httpVersion.equalTo(httpVersion.version, version))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException("해당 버전이 존재하지 않습니다."));
    }

    private boolean equalTo(String version, String inputVersion) {
        return version.equals(inputVersion);
    }

    public String getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return getVersion();
    }
}
