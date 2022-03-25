# java-was
Java Web Server Project for CodeSquad Members 2022

<br>

### 학습한 내용 또는 용어

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

## 2단계 - GET으로 회원가입 기능 구현

   -[x] index.html의 “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.

   -[x] 이 폼을 통해서 회원가입을 할 수 있다.
        ```text
        /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
        ```
       + (String 값) 추출 테스트 코드 작성 : 입력 값을 추출해서 파싱함
       + userId, password, name, email들 값들로 User 클래스 만들기
       + DB에 저장 
       * 한글이 정확하게 입력되고 있는지 확인하기

