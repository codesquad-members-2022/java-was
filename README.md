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
