# java-was
Java Web Server Project for CodeSquad Members 2022

### Step 01

#### 새로 알게 된 내용
- 브라우저가 css 및 js 파일을 알아서 요청하는 것
- 서버소켓이 클라이언트가 요청할때마다 새로운 소켓을 생성하는 것


### Step 02

#### 새로 알게 된 내용
- css 및 js가 적용되지 않는 이유 : server 측에서 content-type 을 text/html 로만 보냈기 때문이다

#### 궁금한 점
- 요청이 오는 url을 담당해서 해당 로직을 수행하는 컨트롤러가 필요할 것 같은데 어떤 식으로 구현해야 할지 의문

### Step 03

#### 요청이 들어오는 과정 
- Request/Response 객체로 관리 -> url, route, parameter, body, header
- GET/POST 요청이 들어온다 -> GET/POST
- RequestController 인터페이스를 통해 method에 따라 controller를 분리한다. (GetController / PostController)
- 각 컨트롤러 안에서 URL에 따른 처리를 수행한다. -> 응답 내용 반환

#### 리팩토링 과정
- RequestHandler가 method, route(+queryString), header, body -> HttpRequest 객체 생성(new HttpRequest(method, route, header, body))
- 얘를 `UrlMapper`에 넘기면 url에 따라서 컨트롤러를 호출해서 응답결과인 response를 받는다.
- controller 내부에서는 http method에 따라 그 내부에서 어떻게 동작할 지 구현
- Http request는 route를 분리해서 queryString을 추출

#### 웹 서버와 was 역할
웹 서버는 요청이 들어오면, 정적 파일만 리턴하는 서버인데
was들은 웹 서버가 요청이 들어오면, was랑 통신을 해서 어떤 로직을 수행한 다음 그 결과를 받아서 리턴(동적 서버)
was는 보통 앞단에 웹 서버를 두고, 요청 -> (웹 서버 -> was -> DB) 

#### 새로 알게 된 내용
- Http request body를 읽을 때에는 `BufferedReader`의 readline을 사용하면 body에 개행문자가 없을 때 blocking이 발생한다는 것을 알게 되었습니다.
그래서 항상 Content-length 만큼을 읽어들이는 방식을 채택한 것이 아닌가 생각하게 되었습니다.
- Redirection을 구현할 때, Http response header에 `Location`을 명시해주면 브라우저가 스스로 redirect하는 것을 알게 되었습니다.
- 스프링은 위대하다

#### 궁금한 점
- HttpStatus를 enum으로 관리하는 방법을 채택해봤습니다. 각 상태코드와 응답 메시지를 관리하는 용도로 사용하였는데 적절한 방법일까요? 
