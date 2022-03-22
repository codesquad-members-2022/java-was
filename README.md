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

- [ ] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.

### 프로그래밍 요구사항

- [ ] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
  - 예시: `/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net`
- [ ] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [ ] 한글이 정확하게 입력되고 있는지 확인해야 한다.

