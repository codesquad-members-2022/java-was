# java-was project by Riako_Ader
Java Web Server Project for CodeSquad Members 2022

# 웹 서버 1단계 - HTTP GET 응답

## 프로젝트 구조
> ![1_tree](https://user-images.githubusercontent.com/29879110/159228887-f413bbd2-37a9-4311-8fed-386bc2d5588d.JPG)  

## 요구사항
  - `http://localhost:8080/index.html` 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

## 주요 코드 설명

```java
    private HttpClientRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        String requestLine = line;

        log.debug("request line : {}", requestLine);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = br.readLine();
            headers.add(HttpRequestUtils.parseHeader(line));

            log.debug("header : {}", line);
        }

        return new HttpClientRequest(requestLine, headers);
    }
```
- [x] 첫 번 째 라인은 Request Line 으로
- [x] 라인별로 http header 읽어 getKeyValue 메서드의 인자로 넘겨주어 Pair 를 생성한다.
- [x] 생성한 Pair 를 List 에 추가한다.
- [x] 빈 라인이 나올 때까지 라인을 읽어 Pair 리스트를 생성한다.

```java
    public class HttpClientRequest {
    
        private static final String REQUEST_LINE_DELIMITER = " ";
        private String method;
        private String requestURI;
        private List<HttpRequestUtils.Pair> headers;
    
        public HttpClientRequest(String requestLine, List<HttpRequestUtils.Pair> headers) {
            this.method = requestLine.split(REQUEST_LINE_DELIMITER)[0];
            this.requestURI = requestLine.split(REQUEST_LINE_DELIMITER)[1];
            this.headers = headers;
        }
    
        public String getRequestURI() {
            return requestURI;
        }
    }
```

- [x] 클라이언트 요청 정보를 담을 HttpClientRequest 클래스를 추가한다.
- [x] requestLine 를 인자로 받아 `method` 와 `requestURI` 값을 저장한다.
- [x] Pair 리스트를 인자로 받아 헤더 정보를 저장한다.

## HttpStatus 분석

```java

// 1xx Informational, 요청을 받아 작업을 진행 중이라는 의미
// 정보 전달: 요청을 받았고, 작업을 진행 중이라는 의미

CONTINUE(100,Series.INFORMATIONAL,"Continue"); // 클라이언트가 나머지 요청을 계속해야 하는 경우
SWITCHING_PROTOCOLS(101,Series.INFORMATIONAL,"Switching Protocols"); // *프로토콜 전환, 클라이언트가 서버에 프로토콜 전환을 요청하여 이를 승인하는 중일 경우

// 2xx Success
// 성공: 요청을 성공적으로 받았고, 이해했다는 의미

OK(200,Series.SUCCESSFUL,"OK"); // *서버가 요청을 성공적으로 처리한 경우, 가장 일반적으로 볼 수 있는 HTTP 상태 ex. 클라이언트가 요청한 페이지를 서버가 성공적으로 제공한 경우
CREATED(201,Series.SUCCESSFUL,"Created"); // 작성됨, 성공적으로 요청되었고 서버에 새 리소스가 작성된 경우
ACCEPTED(202,Series.SUCCESSFUL,"Accepted"); // 허용됨, 서버가 요청을 접수했지만 아직 처리되지 않은 경우
NON_AUTHORITATIVE_INFORMATION(203,Series.SUCCESSFUL,"Non-Authoritative Information"); // 신뢰할 수 없는 정보, 서버가 요청을 성공적으로 처리했지만 다른 소스에서 수신된 정보를 제공하고 있는 경우, 응답 받은 메타정보가 서버에 저장된 원본하고는 동일하지는 않지만 로컬이나 다른 복사본에서 수집 되었음을 알리는 응답코드, 이 경우에도 보통은 200 OK 응답코드가 203 Non-Authoritative Information 응답코드보다 우선적으로 응답한다.
NO_CONTENT(204,Series.SUCCESSFUL,"No Content"); // *콘텐츠 없음, 서버가 요청을 성공적으로 처리했지만 콘텐츠를 제공하지 않는 경우, 일반 클라이언트는 볼 일이 거의 없으며 처리 결과만 중요한 API 요청 등에서 주로 사용한다.
RESET_CONTENT(205,Series.SUCCESSFUL,"Reset Content"); // 콘텐츠 재설정, 서버가 요청을 성공적으로 처리했지만 콘텐츠를 표시하지 않는 경우, 클라이언트가 문서 보기를 재설정할 것을 요구 ex. 새 입력을 위한 양식 비우기
PARTIAL_CONTENT(206,Series.SUCCESSFUL,"Partial Content"); // *일부 콘텐츠, 컨텐츠의 일부만 제공하는 경우, 보통 클라이언트에서 시작 범위나 다운로드할 범위를 지정한 경우 자동으로 해당 부분만 제공할 때 사용하는 코드이다.

// 3xx Redirection
// 리다이렉션: 이 요청을 완료하기 위해서는 리다이렉션이 이루어져야 한다는 의미. 짧은 주소(단축 URL) 서비스의 경우 접속 시 `301`이나 `302` 코드를 보내고, `헤더의 location` 에 `리다이렉션할 실제 URL`을 적어 보낸다.        

MULTIPLE_CHOICES(300, Series.REDIRECTION, "Multiple Choices"); // 복수 응답, 서버가 요청에 따라 여러 조치를 선택할 수 있다. 서버가 사용자 에이전트에 따라 수행할 작업을 선택하거나, 요청자가 선택할 수 있는 작업 목록을 제공한다. 서버에서 여러 개의 응답이 있음을 알릴 때 사용할 의도로 만들어졌으나, 정작 응답을 선택하는 방법은 표준화되지 않아 사용되지 않는다.
MOVED_PERMANENTLY(301, Series.REDIRECTION, "Moved Permanently"); // 영구 이동, 영구적으로 컨텐츠가 이동했을 경우, GET 또는 HEAD 요청에 대한 응답으로 이 응답을 표시하면 요청자가 자동으로 새 위치로 전달된다.
FOUND(302, Series.REDIRECTION, "Found"); // 임시 이동, 일시적으로 컨텐츠가 이동했을 때 사용된다, 현재 서버가 다른 위치의 페이지로 요청에 응답하고 있지만 요청자는 향후 요청 시 원래 위치를 계속 사용해야 한다.
SEE_OTHER(303, Series.REDIRECTION, "See Other"); // 기타 위치 보기, 요청자가 다른 위치에 별도의 GET 요청을 하여 응답을 검색할 경우 서버는 이 코드를 표시한다. HEAD 요청 이외의 모든 요청을 다른 위치로 자동으로 전달한다. 서버가 사용자의 요청을 처리하여 다른 URL 에서 요청된 정보를 가져올 수 있도록 응답하는 코드
NOT_MODIFIED(304, Series.REDIRECTION, "Not Modified"); // **수정되지 않음, 200 다음으로 많이 볼 수 있는 HTTP 상태, 마지막 요청 이후 요청한 페이지는 수정되지 않았다는 의미, 이 경우 보통 브라우저에 캐시되어 있는 버전을 쓴다, 서버가 이 응답을 표시하면 페이지의 콘텐츠를 표시하지 않는다. 요청자가 마지막으로 페이지를 요청한 후 페이지가 변경되지 않으면 이 응답(If-Modified-Since HTTP 헤더라고 함)을 표시하도록 서버를 구성해야 한다.
TEMPORARY_REDIRECT(307, Series.REDIRECTION, "Temporary Redirect"); // 임시 리다이렉션, 302와 동일하게 일시적인 컨텐츠 이동을 나타낼 때 사용되나, HTTP 메서드의 변경을 허용하지 않는다, 현재 서버가 다른 위치의 페이지로 요청에 응답하고 있지만 요청자는 향후 요청 시 원래 위치를 계속 사용해야 한다.

// --- 4xx Client Error ---
// 클라이언트 오류: 이 요청은 올바르지 않다는 의미. 여기서부터 브라우저에 직접 표출된다.

BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"); // *잘못된 요청, 서버가 요청을 인식하지 못한 경우, 요청 자체가 잘못되었을 때 사용하는 코드
UNAUTHORIZED(401, Series.CLIENT_ERROR, "Unauthorized"); // *권한 없음 (인증 실패), 인증이 필요한 리소스에 인증 없이 접근할 경우 발생, 이 응답 코드를 사용할 때는 반드시 브라우저에 어느 인증 방식을 사용할 것인지 보내야 한다. 단순히 `권한`이 없는 경우 이 응답 코드 대신 403 Forbidden 을 사용해야 한다. Unauthenticated 의 의미
PAYMENT_REQUIRED(402, Series.CLIENT_ERROR, "Payment Required"); // *결제 필요, 결제가 필요한 리소스에 결제 없이 접근했을 경우 발생, HTTP/1.1에서 정의되었으나 구현하지는 않고, 향후에 사용하기 위해 예약해둔 코드이다. 현재 딱히 표준조차도 존재하지 않는다. 이런 상황에서는 보통 403 Forbidden 을 사용한다.
FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"); // *거부됨 (인가 실패), 서버가 요청을 거부할 때 발생한다, 관리자가 해당 사용자를 차단했거나 서버에 index.html 이 없는 경우에도 발생할 수 있다. 혹은 로그인 여부와는 무관하게 클라이언트가 리소스에 대한 접근 권한을 갖고 있지 않은 경우에 발생한다.
NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found"); // *찾을 수 없음, 서버가 요청한 리소스를 찾을 수 없는 경우, 서버에 존재하지 않는 페이지에 대한 요청인 경우
METHOD_NOT_ALLOWED(405, Series.CLIENT_ERROR, "Method Not Allowed"); // *허용되지 않는 방법, PUT 이나 DELETE 등 서버에서 허용되지 않은 메서드로 요청 시 사용하는 코드
NOT_ACCEPTABLE(406, Series.CLIENT_ERROR, "Not Acceptable"); // *받아들일 수 없음, 요청은 정상이나 서버에서 받아들일 수 없는 요청일 시 사용하는 코드, 보통 웹 방화벽에 걸리는 경우 이 코드가 반환된다. 요청한 페이지가 요청한 콘텐츠 특성으로 응답할 수 없는 경우
PROXY_AUTHENTICATION_REQUIRED(407, Series.CLIENT_ERROR, "Proxy Authentication Required"); // 프록시 인증 필요, 클라이언트가 프록시를 사용하여 인증해야하는 경우, 클라이언트가 사용할 프록시를 가리킨다
REQUEST_TIMEOUT(408, Series.CLIENT_ERROR, "Request Timeout"); // *요청시간 초과, 서버의 요청 대기 시간을 초과한 경우
CONFLICT(409, Series.CLIENT_ERROR, "Conflict"); // 충돌, 사용자의 요청이 서버의 상태와 충돌하여 응답하는 코드, 서버가 요청을 수행하는 도중 충돌이 발생한 경우, 이 때 서버는 충돌에 대한 정보를 포함하여 응답해야 한다, 서버는 PUT 요청과 충돌하는 PUT 요청에 대한 응답으로 이 코드를 요청 간 차이점 목록과 함께 표시해야 한다.
GONE(410, Series.CLIENT_ERROR, "Gone"); // 클라이언트가 요청한 리소스가 영구적으로 삭제되었을 경우, 이전에는 존재했지만 더 이상 존재하지 않는 리소스에 대해 404 대신 사용되기도 한다. 리소스가 영구적으로 이동된 경우 301 을 사용하여 리소스의 새 위치를 지정해야 한다.
LENGTH_REQUIRED(411, Series.CLIENT_ERROR, "Length Required"); // *길이 필요, 서버에 요청 시 Content-Length 값을 지정하지 않아 서버에서 응답을 거부할 때 쓰는 코드, 서버는 유효한 콘텐츠 길이 헤더 입력란 없이는 요청을 수락하지 않는다
PRECONDITION_FAILED(412, Series.CLIENT_ERROR, "Precondition Failed"); // 사전조건 실패, 클라이언트가 서버로 조건부 요청(Conditional Requests)을 보낼 때 서버의 전제조건과 클라이언트의 전제조건이 맞지 않아 서버에서 응답을 거부할 때 사용하는 코드
PAYLOAD_TOO_LARGE(413, Series.CLIENT_ERROR, "Payload Too Large"); // 요청이 너무 길어 서버가 처리할 수 없는 경우
URI_TOO_LONG(414, Series.CLIENT_ERROR, "URI Too Long"); // 요청 URI (URL) 가 너무 길어 서버가 처리할 수 없는 경우
UNSUPPORTED_MEDIA_TYPE(415, Series.CLIENT_ERROR, "Unsupported Media Type"); // *지원하지 않는 미디어 타입을 요청한 경우, 클라이언트 요청이 요청한 페이지에서 지원하지 않는 형식으로 되어 있는 경우, 클라이언트가 요청한 미디어타입이 서버에서 지원하지 않는 타입이라 응답을 거부할 때 사용되는 코드

// --- 5xx Server Error ---
// 서버오류 - 서버가 응답할 수 없다는 의미. 요청이 올바른지의 여부는 알 수 없다.

INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"); // *내부 서버 오류, 서버에 오류가 발생하여 요청을 수행할 수 없는 경우, 보통 설정이나 퍼미션 문제, 아니면 HTTP 요청을 통해 호출한 문서가 실제 HTML 문서가 아니라 JSP, PHP, 서블릿 등의 프로그램일 경우 그 프로그램이 동작하다 세미콜론을 빼먹는 등의 각종 에러로 비정상 종료를 하는 경우 이 응답코드를 보낸다.
NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"); // *구현되지 않은 기능, 서버가 요청을 수행할 수 있는 기능을 지원하지 않는 경우, ex. 드물지만 서버가 요청 메서드를 인식하지 못할 경우
BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"); // *불량 게이트 웨이, 서버가 게이트웨이나 프록시 역할을 하고 있거나 업스트림 서버에서 잘못된 응답을 받은 경우
SERVICE_UNAVAILABLE(503, Series.SERVER_ERROR, "Service Unavailable"); // 서비스를 사용할 수 없는 경우, 서버가 과부하로 다운 (오버로드) 되었거나 유지관리를 위해 다운되어 현재 사용할 수 없는 경우, 대개 일시적인 상태
GATEWAY_TIMEOUT(504, Series.SERVER_ERROR, "Gateway Timeout"); // 게이트웨이 시간 초과, 서버가 게이트웨이나 프록시 역할을 하고 있거나 업스트림 서버에서 제 때 요청을 받지 못한 경우 즉, 게이트웨이가 연결된 서버로부터 응답을 받을 수 없었을 때 사용된다.
HTTP_VERSION_NOT_SUPPORTED(505, Series.SERVER_ERROR, "HTTP Version not supported"); // 서버가 요청에 사용된 HTTP 프로토콜 버전을 지원하지 않는 경우, 브라우저는 서버가 처리 가능한 HTTP 버전을 자동으로 선택하므로 보기 드문 오류

```

---

# 웹 서버 2단계 - GET으로 회원가입 기능 구현

## 요구사항

- [x] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
- [x] 이 폼을 통해서 회원가입을 할 수 있다.

## 주요코드

```java
    /**
     * 1. `receiveRequest` 메서드를 사용하여 클라이언트가 보낸 데이터 스트림을 읽어 요청 객체로 변환한다.
     * 2. `HandlerMethodMapper` 로 요청 URI 값으로 매핑된 `HandlerMethod` 를 찾아 해당 요청을 수행하도록 한다.
     * 3. `HandlerMethod` 가 반환한 결과 값을 받아 클라이언트에게 응답한다.
     *
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = receiveRequest(in);

            HandlerMethod handlerMethod = HandlerMethodMapper.getHandlerMethod(request.getRequestURI());
            HttpResponse response = handlerMethod.service(request);

            sendResponse(out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 클라이언트가 보낸 데이터 스트림을 `RequestLine`, `RequestHeaders`, (+ RequestMessageBody) 로 구분 지어 읽어들인다.
     * 읽어들인 메시지들을 사용하여 HttpRequest 객체를 생성하고 이를 반환한다.
     *
     * @param in
     * @return `InputStream` 에서 읽어온 데이터로 HttpRequest 객체를 생성하여 반환한다.
     * @throws IOException
     */
    private HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        logger.debug("request line : {}", line);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.add(pair);

            logger.debug("header : {}", line);
        }

        return new HttpRequest(requestLine, headers);
    }

    /**
     * 매칭시킨 `HandlerMethod` 가 반환한 결과 값을 OutputStream 을 통해 클라이언트에게 응답한다.
     *
     * @param out
     * @param response
     */
    private void sendResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.write(response.toByteArray());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
```
---

# 웹 서버 3단계 - POST로 회원가입 기능 구현

## 요구사항

- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한다.
- [x] POST로 회원가입 기능이 정상적으로 동작하도록 구현한다.
- [x] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.
- [x] 같은 ID로 가입을 시도할 경우 가입되지 않고 가입 페이지로 이동한다.

## URI, URL

- URI 란 웹 서버가 리소스를 고유하게 식별할 수 있도록 자원의 위치를 알려주는 규약입니다.
- URI 에 속하는 개념으로 'URL' 과 'URN' 두 가지가 있는데 일반적으로 URL 을 많이 사용하여 URI = URL 으로 사용하기도 합니다.
- URL 은 통합 자원 식별자로 인터넷에 있는 리소스를 나타내는 유일한 주소입니다.
- URI 는 다음과 같은 구조로 되어있습니다.

> scheme:[//[user[:password]@]host[:port]][/path][?query][#fragment]

- URI 는 URL 을 내포하고 있습니다. 다만, 세부적으로 파고들면

>- URL(Uniform Resource Locator - 통합 자원 식별자) 은 자원의 실제 위치
>- URI(Uniform Resource Identifier)는 자원의 위치 + 리소스에 대한 고유 식별자 = 웹 주소

- URI 는 URL 에 리소스에 대한 고유 식별자를 추가한 개념입니다!
- 즉, URL 은 자원의 위치 를 가르키고 URI 는 자원을 식별 한다는 차이가 있습니다.

- 이와 관련하여 스프링 미션 때 MvcConfig 클래스에 작성하여 유용하게 사용했던 addViewControllers 와 연관지어 생각해볼 수 있었습니다.

```java
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

        registry.addViewController("/users/form").setViewName("user/form");
    }
```

- 위 메서드로 인해 https://naneun-spring-cafe.herokuapp.com/users/form 주소에서 users/form 은 자원의 실제 위치가 아니지만 static/user/form.html 에 접근할 수가 있었습니다.
- 따라서 https://naneun-spring-cafe.herokuapp.com/users/form 은 URL 은 아니지만 URI 입니다.

> scheme:[//[user[:password]@]host[:port]][/path][?query][#fragment]

- URI 에 path 뒤에 query 는 식별자의 역할도 수행합니다.
- 결론: `자원의 경로 + 식별자 = 주소`

# 웹 서버 4단계 - 쿠키를 이용한 로그인 구현

## 요구사항
- [x] 회원가입한 사용자로 로그인을 할 수 있어야 한다.
- [x] “로그인” 메뉴를 클릭하면 `http://localhost:8080/user/login.html` 으로 이동해 로그인할 수 있다.
- [x] 로그인이 성공하면 `index.html`로 이동하고, 로그인이 실패하면 `/user/login_failed.html`로 이동해야 한다.
- [x] 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 메모리에 저장한다.
- [x] 아이디와 비밀번호가 같은지를 확인해서 로그인이 성공하면 응답 header의 Set-Cookie 값을 sessionId=적당한값으로 설정한다.

## Cookie
- 웹 서버가 클라이언트에게 보내는 데이터 중 하나
- `Set-Cookie` : 서버에서 클라이언트로 쿠키를 전달(Response)
- `Cookie` : 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 서버로 다시 전달
  - 왜 다시 전달하는가?
    - HTTP는 `무상태(Stateless) 프로토콜`이기 때문에 쿠키를 저장하고 있을 수 없다. 따라서 매 요청시마다 서버와 클라이언트가 계속 쿠키를 주고 받게 된다.
- 쿠키는 네트워크 트래픽을 추가로 사용하기 때문에 꼭 필요한 정보만 사용한다
  - 보안에 민감한 데이터는 **절대절대절대** 저장하면 안된다!
- `Expires` : 쿠키의 만료일 지정
- `Max-Age` : 초단위이며 지정한 초가 모두 지나면 쿠키를 삭제한다. 음수나 0으로 지정시 바로 쿠키를 삭제함
- `Domain` : 쿠키를 전송할 도메인을 지정한다
  - Domain을 명시한 경우 : 명시한 도메인과 서브 도메인에도 쿠키를 전송한다
  - Domain을 생략한 경우 : 현재 문서 기준 도메인에만 전송
- `Path` : 설정한 경로를 포함한 하위 경로페이지에서만 쿠키에 접근이 가능하다
  - 일반적으로는 `Path=/` 로 지정한다
- `Secure` : 쿠키는 `http, https를 구분하지 않고 전송`한다. Secure를 적용하면 https의 경우에만 전송한다.

## 미션에서의 쿠키 적용
- Cookie라는 클래스를 만들어 쿠키의 속성을 관리할 수 있도록 했습니다.
- Cookies라는 일급컬렉션으로 Cookie를 관리했습니다.
- response 객체를 만들 때 아래와 같이 쿠키를 설정했습니다.

```java
    Cookie cookie = new Cookie("sessionId", session.getAttribute("sessionId"));
    cookie.setPath("/");

    response.addCookie(cookie); 
```
