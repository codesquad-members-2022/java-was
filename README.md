# java-was
Java Web Server Project for CodeSquad Members 2022

---

## STEP1 - HTTP GET 응답

### 기능 요구 사항 
- [x] 모든 Request Header 출력하기
- [x] RequestLine에서 path 분리하기
- [x] path에 해당하는 파일 읽어 응답하기

### 추가 요구 사항에 대한 구현
- RequestLine에서 URL를 분리하는 별도의 유틸 메서드를 만들고 단위 테스트를 만들어 진행하였다.

---

## STEP2 - GET으로 회원가입 기능 구현

### 기능 요구 사항
- [x] "/user/form.html" 으로 이동하면서 회원가입 폼을 표시한다.
- [x] 폼의 GET 요청이 들어왔을 때, User 회원가입(객체 생성)을 하도록 하기

### 별도로 한 작업
- 요청에 대한 해석을 담당하는 MyHttpRequest 클래스 정의
  - 내부적으로 요청의 InputStream을 기반으로 분석하여 초기화

### 고민사항
- 저희는 현재 당장 주어진 요구사항이 '회원가입'이였으므로 이것을 if문으로 처리했습니다.
- 앞으로 여러가지 URL 요청에 대한 분기 처리가 필요한데 이들 각각에 대해 if문으로 분기하는 것은 비효율적인 것 같습니다. 그래서 다음 단계 이후 이에 대한 요구사항이 더 주어진다면 이를 다른 방식으로 처리할 수 있도록 본격적으로 고민해봐야겠습니다. 

### 학습해볼 키워드
- 전반적인 Http 메시지 사양 및 기본 용어
  - requestLine : HTTP 메서드, Path & Query, Http 버전
  - Header : Content-Length(Body에 내용이 있을 때), Accept(클라이언트가 선호하는 미디어 타입),  ...
  - Body

---

## STEP3 - POST로 회원 가입

### 기능 요구 사항
- [x] POST 방식으로 HTML form 데이터가 body에 넘어왔을때 회원가입할 수 있다.
- [x] 회원가입을 마치면 "/index.html"로 redirect 한다.

### 별도로 한 작업
- RequestLine 분리
- MyHttpResponse 분리

### 고민사항
- 메서드는 하나의 기능을 담당해야 하므로 initPathAndQueryParameters를 분리하고 싶었지만 하나의 url로부터 두가지 요소를 분리하는 작업이 밀접하게 얽혀있어서, 두 가지 메서드로 분리하기 어려웠습니다.

### 학습해볼 키워드
- http 상태 코드
  - 2xx : 성공
  - 3xx : redirection
  - 4xx : 클라이언트 오류 (요청 오류)
  - 5xx : 서버 오류 (서버에서 요청을 수행하지 못함)
- redirect 302 응답
  ```
    HTTP/1.1 302 Found
    Location: /index.html
  ```
  - 이와 같은 응답을 보낼 경우, 클라이언트는 첫 줄의 상태 코드를 읽고 Location의 path를 읽어 서버에 재요청을 보낸다. 

---
