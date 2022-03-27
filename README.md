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
  - form 태그 통한 회원가입을 GET 요청으로 받았을 때, 쿼리파라미터들이 있다.
  - `?` 이전 경로는 url 별 맵핑 클래스를 두어 해당 url 경로를 처리하는 메서드에서 파라미터 처리등을 진행 할 것이다.
  - 즉, /user/create는 먼저 처리되어 진행되어왔다는 가정하에, `?` 뒤부터의 처리 로직으로 사용자 입력한 값을 저장하게 하고자 한다.


<br>

- 중간정리, 위 2가지 상황예시로 3가지의 경우가 발생 할 수 있다. => 모두 GET 요청시 가능하다
  - 도메인과 포트만을 통한 URL 요청시 : http://localhost:8080/ -> "/"
  - index.html 파일 요청 : http://localhost:8080/index.html -> "/index.html"
  - 쿼리파라미터 전달 : localhost:8080/user/create?userId=test&password=asdf -> ? + userId=test&password=asdf





---

<br>

## refactoring

- HttpMessage
  - URL에 요청으로 브라우저에 의해 생성되어진 Request Header 내용들을 받는다


<br>


---


<br>

## STUDY

#### try-with-resources
  - JDK 7
  - try에 자원객체 전달시, try 코드 블록 종료시 자동으로 자원 종료 해주는 기능
    - 별도의 catch 블록, finally 블록에 close()의 종료 확인 및 종료 처리를 해주지 않아도 된다.
    - 이때, 자동 종료 위해 try에 전달할 수 있는 객체는 AutoCloseable 인터페이스의 구현체로 한정된다.
    - 실습 : HttpMessage 클래스의 write() 에 적용

    ``` java
      public interface Closeable extends AutoCloseable { }
      public abstract class Reader implements Readable, Closeable { }
      public class BufferedReader extends Reader {}
    ```
