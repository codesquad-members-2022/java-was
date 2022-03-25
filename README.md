# java-was

Java Web Server Project for CodeSquad Members 2022

## 1단계

<details>
<summary>1단계 요구사항</summary>

- [x] http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
  - [x] http request header 로부터 path를 읽는 작업
  - [x] index.html 파일을 읽어서 쓰는 작업
- [ ] JDK에서 지원해 주는 라이브러리를 이용해서 구현한다.
- [ ] 초기 프로젝트 소스를 잘 분석하고 이를 개선한다.
- [ ] 유지보수가 쉬운 코드가 될 수 있도록 고민해 본다.
</details>

## 2단계

<details>
<summary>2단계 요구사항</summary>

- [x] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
- [x] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [x] 한글이 정확하게 입력되고 있는지 확인해야 한다.
</details>

## 3단계

<details>
<summary>3단계 요구사항</summary>

- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한다.
- [x] POST로 회원가입 기능이 정상적으로 동작하도록 구현한다.
- [x] 중복아이디를 처리하기 위해서 Map<Id, User> 로 회원목록을 관리한다.
- [x] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.
</details>
