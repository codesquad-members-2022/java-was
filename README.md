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

### 학습 메모

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
