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

<br/>
<details>
<summary>👌 웹서버 3 단계 - GET으로 회원가입 기능 구현</summary>
<div markdown="1">
<br/>

### 기능요구사항

- [X] http://localhost:8080/user/form.html 파일의 HTML form을 통해 회원가입을 할 수 있다.
- [X] 가입 후 index.html 페이지로 이동한다.

### 프로그래밍 요구사항

- [X] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한다.
- [X] POST로 회원가입 기능이 정상적으로 동작하도록 구현한다.
- [X] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

<br/>
</div>
</details>

<br/>
<details>
<summary>🤟 웹서버 4 단계 - 쿠키를 이용한 로그인 구현</summary>
<div markdown="1">
<br/>

### 기능요구사항

- [X] 회원가입한 사용자로 로그인을 할 수 있어야 한다.
- [X] “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
- [X] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.

### 프로그래밍 요구사항

- [X] 정상적으로 로그인 되었는지 확인하려면 앞 단계에서 회원가입한 데이터를 유지해야 한다.
- [X] 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 메모리에 저장한다.
- [X] 필요에 따라 Database 클래스의 메소드나 멤버변수를 수정해서 사용한다.
- [X] 아이디와 비밀번호가 같은지를 확인해서 로그인이 성공하면 응답 header의 Set-Cookie 값을 sessionId=적당한값으로 설정한다.
- [X] Set-Cookie 설정시 모든 요청에 대해 Cookie 처리가 가능하도록 Path 설정 값을 /(Path=/)로 설정한다.
- [X] 응답 header에 Set-Cookie값을 설정한 후 요청 header에 Cookie이 전달되는지 확인한다.

<br/>
</div>
</details>

