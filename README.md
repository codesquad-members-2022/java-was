# java-was
Java Web Server Project for CodeSquad Members 2022

## 1단계. HTTP GET 응답

### 기능요구사항
- [x] `http://localhost:8080/index.html` 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답
### 프로그래밍 요구사항
- [x] JDK 에서 지원해 주는 라이브러리를 이용해서 구현
- [x] 초기 프로젝트 소스를 잘 분석하고 이를 개선
- [x] 유지보수가 쉬운 코드가 될 수 있도록 고민

### 정리하기
- HTTP GET 응답을 어떤 흐름으로 하게 되는지 알게 되었습니다.
- try-with-resource 키워드 관련하여 알게 되었습니다.
- InputStream, OutputStream 키워드 관련하여 알게 되었습니다.
- `java.net.ServerSocket`, `java.net.Socket` 키워드 관련하여 알게 되었습니다.

## 2단계. GET 으로 회원가입 기능 구현
### 기능요구사항
- [x] “회원가입” 메뉴를 클릭하면 회원가입 폼을 표시
- [x] 폼을 통해 회원가입

### 프로그래밍 요구사항
- [x] HTML 과 URL 을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장
- [x] 한글이 정확하게 입력되고 있는지 확인

## 3단계. POST 로 회원가입
### 기능요구사항
- [x] `http://localhost:8080/user/form.html` 파일의 HTML form 을 통해 회원가입을 할 수 있다.
- [x] 가입 후 index.html 페이지로 이동한다.
- [x] 같은 ID로 가입을 시도할 경우 가입되지 않고 가입 페이지로 이동

### 프로그래밍 요구사항
- [x] `http://localhost:8080/user/form.html` 파일의 form 태그 method 를 get 에서 post 로 수정
- [x] POST 로 회원가입 기능이 정상적으로 동작하도록 구현
- [x] 중복아이디를 처리하기 위해서 `Map<Id, User>` 로 회원목록을 관리
- [x] 가입 후 페이지 이동을 위해 redirection 기능을 구현

## 4단계. 쿠키를 이용한 로그인 구현
### 기능요구사항
- [x] 회원가입한 사용자로 로그인을 할 수 있어야 한다.
- [x] “로그인” 메뉴를 클릭하면 `http://localhost:8080/user/login.html` 으로 이동해 로그인할 수 있다.
- [x] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.

### 프로그래밍 요구사항
- [x] 정상적으로 로그인 되었는지 확인하려면 앞 단계에서 회원가입한 데이터를 유지해야 한다.
- [x] 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 메모리에 저장한다.
- [x] 필요에 따라 Database 클래스의 메소드나 멤버변수를 수정해서 사용한다.
- [x] 아이디와 비밀번호가 같은지를 확인해서 로그인이 성공하면 응답 header의 Set-Cookie 값을 sessionId=적당한값으로 설정한다.
- [x] Set-Cookie 설정시 모든 요청에 대해 Cookie 처리가 가능하도록 Path 설정 값을 /(Path=/)로 설정한다.
- [x] 응답 header에 Set-Cookie값을 설정한 후 요청 header에 Cookie이 전달되는지 확인한다.
