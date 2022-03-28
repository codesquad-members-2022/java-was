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
