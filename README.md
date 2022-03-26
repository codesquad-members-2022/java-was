# java-was
Java Web Server Project for CodeSquad Members 2022

<br/>
<details>
<summary>👆 웹 서버 1단계 - HTTP GET 응답</summary>
<div markdown="1">
<br/>

### 기능 요구 사항

- [X] 정적인 html 파일 응답
  - http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 프로그래밍 요구사항

- [X] JDK에서 지원해 주는 라이브러리를 이용해서 구현한다.
- [X] 초기 프로젝트 소스를 잘 분석하고 이를 개선한다.
- [X] 유지보수가 쉬운 코드가 될 수 있도록 고민해 본다.
- [X] 1단계에서는 text/html 만 응답해 주면 된다. 다른 종류의 포맷에 대해서는 추후에 고민하자.

<br/>
</div>
</details>

<br/>
<details>
<summary>✌ 웹서버 2 단계 - GET으로 회원가입 기능 구현</summary>
<div markdown="1">
<br/>


### 기능요구사항

- [X] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다. 
  - 이 폼을 통해서 회원가입을 할 수 있다.

### 프로그래밍 요구사항

- [X] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다. 
  ```
  /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
  ```
- [X] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [X] 한글이 정확하게 입력되고 있는지 확인해야 한다.

<br/>
</div>
</details>
