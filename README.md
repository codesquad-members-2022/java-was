## Step04 TODO list

- [x] Servlet interface 만들기
- [x] CreateUserServlet (내부로 createUser 메서드 이동시키기)
- [x] 로그인 기능 구현 (http://localhost:8080/user/login.html)
  - [x] 성공시 index.html로 이동
  - [x] 실패시 /user/login_failed.html로 이동

## Step03 리뷰 반영

- [ ] 학습한 내용 정리
- [x] Database 유저 중복 관련해서 IllegalArgumentException 아닌 다른 Exception으로 변경
- [x] Database.addUser 리턴 제거하기
- [x] HttpRequest의 header 명칭 변경
- [x] HttpResponse 이름 고민해보기 -> 향후에 복잡해지면 변경
- [x] createRequest 이름 변경하기 -> parse
- [x] requestLineTokens 객체 만들기
- [x] RequestHandler Parser 스태틱으로 만들기
- [x] RequestHandler.createUser 위치 고민 해보기 -> 이후 생성될 createServlet 내부로 이동예정
- [x] RequestParser에서 String body="" 삭제하기
- [x] RequestHandler에서 메서드 분리하지 말기 (buildRequest, buildResponse)

## Step03 TODO list

- [x] Reqeust 객체 만들기
  - [x] 메서드 종류
  - [x] 리소스 위치
  - [x] 스키마(protocol)
  - [x] Header는 map으로 저장
  - [x] 본문은 String으로

- [x] RequestParser 만들기

- [x] Response 객체 만들기
- [x] response에 값 setting 해주기
- [x] ResponseBuilder 메서드 분리하기
- [x] 회원 저장 하기 (Map형태)
- [x] 중복 회원 방지

## Step01 TODO List

- [x] 기본 소스코드 함께 읽고 분석하기 (요청시 -> mian -> start -> handler.run() 순서로 실행)
- [x] input header를 분석하기 -> requeatLine 분석 -> 데이터 위치(URI) 확인
- [x] 읽은 requestLine를 통해 읽은 자원을 File 객체로 만든다.
- [x] File 객체를 output으로 보낸다.
