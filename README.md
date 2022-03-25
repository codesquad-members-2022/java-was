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
- [ ] 중복 회원 방지

## Step01 TODO List

- [x] 기본 소스코드 함께 읽고 분석하기 (요청시 -> mian -> start -> handler.run() 순서로 실행)
- [x] input header를 분석하기 -> requeatLine 분석 -> 데이터 위치(URI) 확인
- [x] 읽은 requestLine를 통해 읽은 자원을 File 객체로 만든다.
- [x] File 객체를 output으로 보낸다.
