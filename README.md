# java-was

Java Web Server Project for CodeSquad Members 2022

## 알게된 내용

### 검봉

#### redirect 작동 원리(미션 기준)

1. POST 요청

```java
POST/user/create HTTP/1.1
...
userId=geombong&password=password&name=검봉&email=email@com
```

- 회원 가입 FROM에 회원가입 정보를 입력하고 요청을 보내면 POST http body에 FROM에서 입력한 데이터를 담아 POST요청을 보낸다.

2. 302 응답

```java
HTTP/1.1 302 Found
Location: localhost:8080/index.html
```

- 여기서 가장 크게 헤깔렸던 문제가 Redirect 하려는 URL을 웹에서 만들어서 다시 HttpConnection을 사용해서 연결을 해야하는 방식으로 하는줄 알았으나, </br> 정말 간단하게 response header에 302코드와 Location 주소를 담아 응답하면 브라우저가 자동으로 리다이렉트를 해준다는걸 뒤늦게 알았다.

3. 자동 리다이렉트

- 브라우저가 자동으로 응답 코드와 Location을 보고 리다이렉트를 해준다.

4. GET 요청

- 브라우저가 자동으로 리다이렉트를 하면서 Location에 있는 URL로 GET 요청을 보낸다.

5. 200 응답

- 요청한 URL 결과에 따라 200 OK를 반환한다.

### 반스

- 일시적인 리다이렉션 302의 작동 방식에 대해 배웠습니다.
- 처음에 리다이렉션 302 의 작동방식 모두를 Java로 구현하려고 했으나, 브라우저에게 Code와 Location 정보만 주면 알아서 처리해주는걸 보고 웹 브라우저 응답방식에 대해 배울 수 있었습니다.
- 회원가입 중복확인이 클라이언트측 오류라 생각하여 4xx 코드를 구현해야 하나 라는 생각으로 4xx 코드에 종류에 대해서도 학습할 수 있었습니다.
- Handler의 run()메소드 분리에 대해 고민해보면서 Servlet이란걸 공부하게 되었고, Spring Web MVC에서 Servlet이 어떻게 작동하고 DispatcherServlet의 역할까지 학습할 수 있었습니다. 
