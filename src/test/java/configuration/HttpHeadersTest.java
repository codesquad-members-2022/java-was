package configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpHeadersTest {

    private HttpHeaders httpHeaders;

    /**
     * Content-Type, Content-Length,
     * Request Method, Request URL,
     * Cache-Control, Port,
     * Connection, sec-ch-ua-platform,
     * Sec-Fetch-Mode, sec-ch-ua-mobile,
     * Sec-Fetch-User, sec-ch-ua,
     * User-Agent, Purpose,
     *
     *
     * // TODO.
     * Sec-Fetch-Dest, Host,
     * Accept, Sec-Fetch-Site,
     * Accept-Encoding, Upgrade-Insecure-Requests,
     * Cookie, Accept-Language,
     * Referer, Origin;
     */

    @BeforeEach
    void init() {
        httpHeaders = ObjectFactory.httpHeaders;
    }

    @Test
    @DisplayName("기본으로 등록한 HeaderType을 넣으면 해당 값이 나온다.")
    void findHeaderType() {
        String expectedContentType = httpHeaders.getHeaderType("Content-Type");
        String expectedContentLength = httpHeaders.getHeaderType("Content-Length");
        String expectedRequestMethod = httpHeaders.getHeaderType("Request Method");
        String expectedRequestURL = httpHeaders.getHeaderType("Request URL");
        String expectedCacheControl = httpHeaders.getHeaderType("Cache-Control");
        String expectedPort = httpHeaders.getHeaderType("Port");
        String expectedConnection = httpHeaders.getHeaderType("Connection");
        String expectedSecChUaPlatform = httpHeaders.getHeaderType("sec-ch-ua-platform");
        String expectedSecFetchMode = httpHeaders.getHeaderType("Sec-Fetch-Mode");
        String expectedsecChUaMobile = httpHeaders.getHeaderType("sec-ch-ua-mobile");
        String expectedsecSecFetchUser = httpHeaders.getHeaderType("Sec-Fetch-User");
        String expectedsecsecChUa = httpHeaders.getHeaderType("sec-ch-ua");
        String expectedUserAgent = httpHeaders.getHeaderType("User-Agent");
        String expectedPurpose = httpHeaders.getHeaderType("Purpose");

        assertThat("Content-Type").isEqualTo(expectedContentType);
        assertThat("Content-Length").isEqualTo(expectedContentLength);
        assertThat("Request Method").isEqualTo(expectedRequestMethod);
        assertThat("Request URL").isEqualTo(expectedRequestURL);
        assertThat("Cache-Control").isEqualTo(expectedCacheControl);
        assertThat("Port").isEqualTo(expectedPort);
        assertThat("Connection").isEqualTo(expectedConnection);
        assertThat("sec-ch-ua-platform").isEqualTo(expectedSecChUaPlatform);
        assertThat("Sec-Fetch-Mode").isEqualTo(expectedSecFetchMode);
        assertThat("sec-ch-ua-mobile").isEqualTo(expectedsecChUaMobile);
        assertThat("Sec-Fetch-User").isEqualTo(expectedsecSecFetchUser);
        assertThat("sec-ch-ua").isEqualTo(expectedsecsecChUa);
        assertThat("User-Agent").isEqualTo(expectedUserAgent);
        assertThat("Purpose").isEqualTo(expectedPurpose);

    }
}
