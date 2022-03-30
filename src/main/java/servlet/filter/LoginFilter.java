package servlet.filter;

import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
    private List<String> redLabel = new ArrayList<>();

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        // 해당 url인지 검사
        if (isLoginCheckPath(request.getPath())) { // 로그인 체크 해야 하는곳
            String sessionId = getSessionId(request);
            if (sessionId != null) {
                if (Session.isLoginUser(sessionId)) {
                    log.debug("[LoginFilter] {}", sessionId);
                    return true; // 로그인 이미 완료
                }
            }
            // 로그인 화면으로 이동
            response.forward("/user/login.html");
            return false;
        }
        return true;
    }

    private String getSessionId(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        String values = headers.get("Cookie");
        String[] splits = values.split("; ");
        if (splits.length > 1) {
            return splits[1].substring(10);
        }
        return null;
    }

    public void addUrl(String url) {
        this.redLabel.add(url);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return redLabel.stream()
                .filter(url -> url.equals(requestURI))
                .findFirst()
                .isPresent();
    }
}
