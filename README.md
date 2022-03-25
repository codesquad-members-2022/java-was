# java-was
Java Web Server Project for CodeSquad Members 2022

<br>

## 학습한 내용 또는 용어

+ 버퍼(buffer) : 데이터를 한 곳에서 다른 한 곳으로 전송하는 동안 일시적으로 그 데이터를 보관하는 임시 메모리 영역
              입 출력의 속도 향상을 위해 버퍼 사용 -> 키보드의 입력이 있을 때마다 한 문자씩 버퍼로 전송한다.
                                               이 때 버퍼가 가득 차거나, 개행 문자가 나타나면 버퍼의 내용을 한 번에 전송한다.

   * *** 하드디스크는 원래 속도가 엄청 느리므로 키보드 입력이 바로 전달되는 것보다 중간에 메모리 버퍼를 통해 입력을 모아서 전송하는 편이 훨씬 빠르다.

출처 : https://jhnyang.tistory.com/92

<br>

file 클래스의 생성자에 URL을 넣는다면 file url 경로에 대한 파일의 File 객체를 생성한다.
file 클래스의 메소드인 getPath()를 실행시키면 파일의 경로를 문자열의 형태로 리턴한다.

출처 : https://devbox.tistory.com/entry/Java-File-%ED%81%B4%EB%9E%98%EC%8A%A4

<br>

FileReader, FileWriter, FileInputStream, FileOutputStream 은 직접적으로 파일을 읽고 쓰는 클래스이나
문자나 바이트 형식의 문자만 읽거나 쓸 수 있다.

이에 반에, DataInputStream, DataOutputStream은 primitive type의 데이터를 읽고 쓸 수 있다.

출처 : https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=highkrs&logNo=220474124970

<br>

### 삽질을 통해 배운 점
겪은 문제 : 서버에 "POST" 요청을 했을 때 바로 response가 오지 않고, 다음 요청이 왔을 때 전에 "POST"의 response가 비어있는 상태로
software caused connection abort: socket write error 에러가 발생했습니다.

문제 원인 : BufferedReader의 readLine()를 통해 request body를 읽었는데 개행문자가 존재하지 않아서 readLine()하지 못해 blocking 당했고,
이 상태에서 다음 요청이 왔을 때 이전의 라인을 BufferedReader가 읽었지만 소켓이 close한 상태여서 resposse가 비어있는 상태로
software caused connection abort: socket write error 에러가 발생했습니다.

해결 방안 : readLine() 대신, 헤더의 Content-Length 값을 이용하여 글자수 만큼 읽어와 문제를 해결하였습니다.

```text
start-line
* ( header-field CRLF )
CRLF
[ message-body ]
```

새로 배운 점 
- Http message 구조상 body가 끝나고 개행문자가 없기 때문에 Content-Length의 값을 이용해야만 했고, 이를 통해 Content-Length의 존재 이유를 알게 되었습니다.
- 초기에는 log를 통해 에러가 난 부분을 찾고자 하였는데 산토리 선생님께서 디버거를 이용하셔서 문제를 해결하시는 모습을 보고 디버거를 사용해야겠다는 생각이 들었습니다.


## 2단계 - GET으로 회원가입 기능 구현

   - [x] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.

   - [x] 이 폼을 통해서 회원가입을 할 수 있다.
        ```text
        /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
        ```
       + (String 값) 추출 테스트 코드 작성 : 입력 값을 추출해서 파싱함
       + userId, password, name, email들 값들로 User 클래스 만들기
       + DB에 저장 
       * 한글이 정확하게 입력되고 있는지 확인하기

## 3단계 - POST로 회원가입

   기능요구사항
   - [x] http://localhost:8080/user/form.html 파일의 HTML form을 통해 회원가입을 할 수 있다.

   - [X] 가입 후 index.html 페이지로 이동한다.


   프로그래밍 요구사항
   - [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한다.

   - [X] POST로 회원가입 기능이 정상적으로 동작하도록 구현한다.

   - [X] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.


   TODO 리스트 
   - [x] Request Body의 값 추출하기
   - [X] redirect 구현 (HTTP 응답 해더의 status code를 200이 아닌 302로 한다.)
   
