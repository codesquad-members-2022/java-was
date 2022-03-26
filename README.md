# java-was
Java Web Server Project for CodeSquad Members 2022



#### GET 파일 요청을 받아 파일 전달
- http://localhost:8080/index.html 로 URL 요청시 
  - 브라우저에 의해 변환된 Request Header 중 첫번째 라인
    - GET /index.html HTTP/1.1
      - GET + 1칸 + '/' 이후의 경로를 추출
        - GET / HTTP/1.1 : 아무 파일요청이 없을때 '/' 뒤는 공백
      - webapp 하위 디렉토리를 읽는다.
- http://localhost:8080/user/create?userId=test&password=asdf&name=test&email=test2%40email.com
