# java-was

Java Web Server Project for CodeSquad Members 2022

<br>

## 학습한 내용 또는 용어

+ 버퍼(buffer) : 데이터를 한 곳에서 다른 한 곳으로 전송하는 동안 일시적으로 그 데이터를 보관하는 임시 메모리 영역 입 출력의 속도 향상을 위해 버퍼 사용 -> 키보드의 입력이 있을 때마다 한 문자씩
  버퍼로 전송한다. 이 때 버퍼가 가득 차거나, 개행 문자가 나타나면 버퍼의 내용을 한 번에 전송한다.

    * *** 하드디스크는 원래 속도가 엄청 느리므로 키보드 입력이 바로 전달되는 것보다 중간에 메모리 버퍼를 통해 입력을 모아서 전송하는 편이 훨씬 빠르다.

[참고자료](https://jhnyang.tistory.com/92)

<br>

file 클래스의 생성자에 URL을 넣는다면 file url 경로에 대한 파일의 File 객체를 생성한다. file 클래스의 메소드인 getPath()를 실행시키면 파일의 경로를 문자열의 형태로 리턴한다.

[참고자료](https://devbox.tistory.com/entry/Java-File-%ED%81%B4%EB%9E%98%EC%8A%A4)

<br>

FileReader, FileWriter, FileInputStream, FileOutputStream 은 직접적으로 파일을 읽고 쓰는 클래스이나 문자나 바이트 형식의 문자만 읽거나 쓸 수 있다.

이에 반에, DataInputStream, DataOutputStream은 primitive type의 데이터를 읽고 쓸 수 있다.

[참고자료](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=highkrs&logNo=220474124970)
<br>

인코딩 형식

1. ASCII

- 최초의 문자열 인코딩
- 영어를 위한 문자

2. UTF-8

- 유니코드를 위한 가변 길이 문자 인코딩(멀티바이트) 방식 중 하나이다
- ANSI의 단점을 보완하기 위해 만들어짐
- 영어 : 1byte
- 중동지역 언어 또는 많은 유럽 언어 : 2byte
- 한국,중국,일본 등 동아시아권 언어 : 3byte

3. UTF-16

- 16bit 기반으로 저장하는 UTF-8의 변형
- 한글의 경우, 저장하면 2bytes면 되어 용량의 이점이 있지만 경우에 따라서는 2bytes 이상을 사용할 경우가 있어 용량의 이점이 크다고 보긴 어렵고, 엔디안 처리를 고려함에 따른 복잡성 증대나 ANSI와
  호환이 안되는 단점이 있다.

4. UTF-32

- 모든 문자를 4bytes로 인코딩한다.
- 문자 변환 알고리즘이나 가변길이 인코딩 방식에 대한 고민을 하고 싶지 않을 때 유용하지만 매우 비효율적으로 메모리를 사용하므로 자주 사용되지는 않는다.

5. EUC-KR

- EUC-KR은 한글 지원을 위해 유닉스 계열에서 나온 완성형 코드 조합
- 완성형 코드 : 완성 된 문자 하나하나마다 코드 번호를 부여한 것
- <-> 조합형 코드 : 글의 자음과 모음 각각에 코드 번호를 부여한 후 초성, 중성, 종성을 조합하여 하나의 문자를 나타내는 방식
- EUC-KR은 ANSI를 한국에서 확장한 것으로 외국에서는 지원이 안 될 가능성이 높다.

[참고자료](https://onlywis.tistory.com/2)

<br>

Http 상태 코드

1. 400 Bad Request

- 이 응답은 잘못된 문법으로 인하여 서버가 요청을 이해할 수 없음을 의미합니다.

2. 403 Forbidden

- 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다. 예를들어 그들은 미승인이어서 서버는 거절을 위한 적절한 응답을 보냅니다. 401과 다른 점은 서버가 클라이언트가 누구인지 알고 있습니다.

3. 409 Conflict

- 이 응답은 요청이 현재 서버의 상태와 충돌될 때 보냅니다.

고민 사항

+ 아이디가 중복되어 회원가입이 실패했을 때 어떤 상태 코드를 사용해야 할 지 고민했습니다.

결론

+ 아이디 중복을 확인하는 것이 미리 저장되어있던 유저와 새로 생성하려는 유저의 충돌로 판단했기에 409 상태코드가 더 적절하다고 생각했습니다.
+ 이번 미션의 경우 같은 ID로 가입을 시도할 경우 가입되지 않고 가입 페이지로 가도록 처리해야 되기 때문에 302 상태코드를 사용했습니다.

[참고자료](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)

<br>

URI(Uniform Resource Identifier)

- Uniform : 리소스를 식별하는 통일된 방식
- Resource : 자원, URI로 식별할 수 있는 모든 것(제한 없음)
- Identifier: 다른 항복과 구분하는데 필요한 정보
- URI는 로케이터(Locator), 이름(Name) 또는 둘 다 추가로 분류될 수 있다.
    - URL(Uniform Resource Loator) : 리소스가 있는 위치를 지정
        - `foo://example.com:8042/over/there?name=ferret#nose`
        - foo - scheme
        - example.com:8042 - authority
        - over/there - path
        - name=ferret - query
        - nose - fragment
    - URN(Uniform Resource Name) : 리소스에 이름을 부여
        - `urn:example:animal:ferret:nose`
        - urn - scheme
        - example:animal:ferret:nose - path
    - 위치는 변할 수 있지만, 이름은 변하지 않는다.
    - URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않았으며, 거의 URL만 사용

URL 전체 문법

- scheme://[userinfo@]host[:port][/path][?query][#fragment]
    - ex) https://www.google.com:443/search?q=hello&hl=ko
    - 프로토콜 : https
    - 호스트명 : www.google.com
    - 포트번호 : 443
    - 경로 : /search
    - 쿼리 파라미터 : q=hello&hl=ko
- scheme : 주로 프로토콜(어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙) 사용
    - ex) http, https, ftp 등등
    - http - 80, https - 443 포트를 주로 사용(생략 가능)
    - https는 http에 보안 추가(HTTP Secure)
- userinfo : URL에 사용자정보를 포함해서 인증. 거의 사용하지 않음
- host : 호스트명. 도메인명이나 IP 주소를 직접 사용 가능
- port : 접속 포트. 일반적으로 생략
- path : 리소스 경로. 계층적 구조
- query : key=value 형태. ?로 시작, &로 추가 가능
- fragment : html 내부 북마크 등에 사용하며 서버에 전송되는 정보는 아님

### 삽질을 통해 배운 점

겪은 문제 : 서버에 "POST" 요청을 했을 때 바로 response가 오지 않고, 다음 요청이 왔을 때 전에 "POST"의 response가 비어있는 상태로 software caused connection
abort: socket write error 에러가 발생했습니다.

문제 원인 : BufferedReader의 readLine()를 통해 request body를 읽었는데 개행문자가 존재하지 않아서 readLine()하지 못해 blocking 당했고, 이 상태에서 다음 요청이 왔을
때 이전의 라인을 BufferedReader가 읽었지만 소켓이 close한 상태여서 resposse가 비어있는 상태로 software caused connection abort: socket write error
에러가 발생했습니다.

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

리팩토링해야 할 부분

- [x] Run를 여러 메소드로 쪼갠다.

## 리뷰 받은 내용
- [x] 마크다운 문법에 맞게 README를 작성하기
- [x] HttpRequestUtils에서 getQueryString() 메소드 수정
- [x] 인코딩 형식들 README에 작성하기
- [x] URL에 대한 내용 찾아보기

## 4단계 - 쿠키를 이용한 로그인 구현
 - [ ] 회원가입한 사용자로 로그인을 할 수 있어야 한다.
 - [ ] “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
 - [ ] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
 - [ ] 정상적으로 로그인 되었는지 확인하려면 앞 단계에서 회원가입한 데이터를 유지해야 한다.
 - [ ] 앞 단계에서 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 메모리에 저장한다.
 - [ ] 필요에 따라 Database 클래스의 메소드나 멤버변수를 수정해서 사용한다.
 - [ ] 아이디와 비밀번호가 같은지를 확인해서 로그인이 성공하면 응답 header의 Set-Cookie 값을 sessionId=적당한값으로 설정한다.
 - [ ] Set-Cookie 설정시 모든 요청에 대해 Cookie 처리가 가능하도록 Path 설정 값을 /(Path=/)로 설정한다.
 - [ ] 응답 header에 Set-Cookie값을 설정한 후 요청 header에 Cookie이 전달되는지 확인한다.
