# java-was

Java Web Server Project for CodeSquad Members 2022

## 1단계 - GET HTTP 응답

### 기능요구사항

- [X] 정적인 html 파일 응답 : http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 프로그래밍 요구사항

- [X] JDK에서 지원해 주는 라이브러리를 이용해서 구현한다. 
- [ ] 초기 프로젝트 소스를 잘 분석하고 이를 개선한다.
- [X] 유지보수가 쉬운 코드가 될 수 있도록 고민해 본다.
- [X] 1단계에서는 text/html 만 응답해 주면 된다. 다른 종류의 포맷에 대해서는 추후에 고민하자.

### 새롭게 알게 된 내용

- 특정 파일(html, css, 이미지)들의 경우 Content-type을 정확히 전달하지 않으면 제대로 표시되지 않는다.
  - html 파일의 경우 text/html 외의 Content-type으로 응답받은 경우 브라우저에 소스가 그대로 표시된다.
  - 만일 content-type을 txt/html로 보내면 브라우저는 이를 html로 인식하지 않고 txt로 인식하며 브라우저 화면상에 출력되는 대신 다운로드가 시작된다.
  - text/css의 경우 text/html 타입으로 보내면 본문 html에 스타일이 적용되지 않는다.
  - image(favicon.ico)의 경우 text/html로 응답받은 경우 직접 열어보면 깨진 문자열이 출력된다. 단, 브라우저 상단의 아이콘은 정상적으로 표시되기도 한다. `*/*` 타입으로 보낼 경우에도 octet-stream 타입으로 인식되며 직접 열어보면 마찬가지로 깨진 문자열이 출력되며 image/x-icon로 응답받은 경우 직접 열었을 경우 정상 출력된다.
  - javascript의 경우 text/html로 보내도 script 타입으로 인식되며 스크립트도 정상 동작한다. (단, 권장 사항은 아니다.)
  - font의 경우 text/html, text/css 등의 타입에도 font 타입으로 인식되며 정상 동작한다.
- `Files::probeContentType`이나`URLConnection::guessContentTypeFromName` 등 Java API 중 확장자명에 따른 content-type을 반환해주는 메서드가 존재하나, 일부 확장자명의 경우 null을 반환하는 경우가 많아 신뢰성이 떨어진다.
  - 파일의 Content-type을 반환하는 외부 라이브러리 중에서는 Apache Tika 등이 유명하다.

## 2단계 - GET으로 회원가입 기능 구현

### 기능요구사항

- [x] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.

### 프로그래밍 요구사항

- [x] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
  - 예시: `/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net`
- [x] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [x] 한글이 정확하게 입력되고 있는지 확인해야 한다.

### 새롭게 알게 된 내용

- OS에 따라 파일 시스템에서 슬래시와 역슬래시의 용도가 다를 수 있다. 
  - 역슬래시(\)로 경로를 표시하던 관행은 IBM DOS에서 유래되었다. IBM 계열 시스템에서는 슬래시로 실행 스위치(옵션), 역슬래시로 경로를 표시했다. 
  - 반면 유닉스 계열은 슬래시로 경로를, 대시(-)로 스위치를 표시한다.
  - 정슬래시와 역슬래시를 자동으로 처리해 주는 경우도 있으나, 그래도 특정 상황 하에서는 코드 페이지 에러가 발생하거나 상대 경로를 인식하지 못하는 등 문제가 발생할 수도 있다. 
  - 윈도우에서 실행된 Java 런타임 환경에서는 자동으로 슬래시를 역슬래시로 변환해준다.
  - 참고: https://www.ibm.com/docs/en/zvse/6.2?topic=SSB27H_6.2.0/fa2ad_use_forward_or_backward_slashes_under_windows.html
- 웹 브라우저의 상대 경로
  - /user/create 요청 처리 후 response body로 /index.html 파일 내용을 전송하면, 위치가 /user/create인 상태로 index.html의 내용이 브라우저에 표시된다.  
  - 이 경우 브라우저에서 렌더링된 페이지에 있는 상대 경로들은 /user를 기준으로 하게 된다.
    - 예시1) href="css/bootstrap.min.css" 링크는 http://localhost:8080/user/css/bootstrap.min.css를 가리키게 된다. 
    - 예시2) href="user/form.html" 링크는 http://localhost:8080/user/user/form.html을 가리키게 된다.

## 3단계 - GET으로 회원가입 기능 구현

### 기능요구사항

- [x] http://localhost:8080/user/form.html 파일의 HTML form을 통해 회원가입을 할 수 있다.
- [x] 가입 후 index.html 페이지로 이동한다.
- [x] 같은 ID로 가입을 시도할 경우 가입되지 않고 가입 페이지로 이동한다.

### 프로그래밍 요구사항

- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한다.
- [x] POST로 회원가입 기능이 정상적으로 동작하도록 구현한다.
- [x] 중복아이디를 처리하기 위해서 Map<Id, User> 로 회원목록을 관리한다.
- [x] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

### 새롭게 알게 된 내용

- 리다이렉이션을 할 때는 상태코드를 30x로 하고 Location 응답 헤더에 리다이렉션할 URL 리소스를 입력한다.
- 클라이언트에 응답을 줄 때는 상태 코드가 있으면 상태메시지(reason phrase : FOUND 등)를 생략해도 동작한다.
- 상태코드를 생략해서 응답하면 200으로 응답된다.(웹 브라우저에서 확인했을 경우)
  - (예시)
  - HTTP/1.1 -> HTTP/1.1 200 OK로 들어옴
  - HTTP/1.1(공백) -> HTTP/1.1 200으로 들어옴
- 상태코드가 40x일 경우라도 html 등 리소스를 응답하면 웹 브라우저 단에서 웹 페이지는 정상적으로 출력된다.
- form 태그로 POST 요청 시 default로 Content-Type Header가 application/x-www-form-urlencoded로 설정되어 쿼리 스트링이 바디에 입력되어 전송된다.
  - 따라서 쿼리 스트링이 URL에 노출되지 않는다.

### 고민했던 부분

- 기존에 주어진 DataBase의 경우 static Map을 가진 Util 클래스인데, 이번 단계에서는 이를 그대로 사용하였습니다. 
  - 다만, DataBase를 static 필드와 static 메서드로 관리함에 있어서 어떤 문제가 발생할 수 있을지에 대해 좀 더 공부가 필요할 것 같습니다.

## 4단계 - 쿠키를 이용한 로그인 구현

### 기능요구사항

- [x] 회원가입한 사용자로 로그인을 할 수 있어야 한다.
  - [x] “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
  - [x] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.

### 프로그래밍 요구사항

- [x] 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 메모리에 저장한다.
  - 필요에 따라 Database 클래스의 메소드나 멤버변수를 수정해서 사용한다.
- [x] 아이디와 비밀번호가 같은지를 확인해서 로그인이 성공하면 응답 header의 Set-Cookie 값을 sessionId=적당한값으로 설정한다.
- [x] Set-Cookie 설정시 모든 요청에 대해 Cookie 처리가 가능하도록 Path 설정 값을 /(Path=/)로 설정한다.
- [x] 응답 header에 Set-Cookie값을 설정한 후 요청 header에 Cookie이 전달되는지 확인한다.

### 추가 요구 사항

- [x] (선택) 로그아웃을 구현한다.

### 학습 메모

- 쿠키는 name만으로 특정할 수 없다. 
  - domain이나 path 속성이 다르면 별개의 쿠키로 취급된다. 
  - http 응답을 통해 쿠키를 만료시킬 때에도 대상 쿠키와 동일한 path를 명시해 주어야 정상적으로 만료시킬 수 있다.

### TODO

- Response Header를 전송하는 메서드들의 중복과 파편화가 심하다. HttpResponse 클래스를 만들어 HTTP 응답에 관련된 내용을 객체로 묶도록 해야겠다. 

## 5단계 - 동적 HTML

### 기능요구사항

- [x] 접근하고 있는 사용자가 “로그인” 상태일 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
- [x] 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

### 프로그래밍 요구사항

- [x] StringBuilder를 활용해 사용자 목록을 출력하는 html 을 동적으로 생성한 후 응답으로 보낸다.

### 학습 메모

- 이번 미션의 경우 최소한의 코드로 요구사항을 구현했으나, 추후 템플릿 엔진(머스태치 등)과 같은 유사한 방식으로 태그를 인식하여 렌더링하는 코드도 구현해볼 수 있을 것 같습니다. 

