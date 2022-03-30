# java-was

---

- Pair : [`나단`](https://github.com/nathan29849), [`제리`](https://github.com/jeremy0405)

---
## Step1 : HTTP GET 응답

### Accept Header, Content-Type

- `Accept Header` : Client가 Request시 받을 수 있는 `MIME types`를 의미(서버와의 컨텐츠 협상)
- `Content-Type` :  Server가 Response시 Client 컨텐츠 협상을 통해 내려 준 `MIME type`

<br>

### DataOutputStream

사용 이유
- `OutputStream` : `write()` 시 인자로 `byte[]`를 받음
- `DataOutputStream` : `write()` 시 인자로 자바의 기본 타입을 받음

따라서 `OutputStream`을 사용하기 위해서는 `out.write("test".getByte(StandardCharset.UTF-8))`을 사용해야 하는 반면 `DataOutputStream`은 `out.write("test")`로 사용이 가능해서 편리하다.

<br>

### java.io.File 

- `new File()` 에서 인자값 상대경로, 절대경로 둘 다 지원한다. (단 상대경로는 프로젝트 디렉토리 기준)
- `file.toPath()`가 무엇인가?
  - file의 path를 Path 클래스로 바꾸어주는 메소드 
- java.io, java.nio 차이
    - blocking / nonblocking
    - Stream / Channel

---

## Step2 : GET으로 회원가입 기능 구현

### URLDecoder
- StandardCharsets.UTF_8는 Java 7부터 지원
- InputStreamReader에 StandardCharsets.UTF_8를 굳이 넣어줄 필요가 없었다.
  - 이유 : InputStream으로 받아올 때 한 글자씩 받아오게 되는데, 한글은 여러 문자의 조합으로 이루어지므로, 제대로 디코딩이 되지 않는다.
  - 해결 : URLDecoder.decode(string, StandardCharsets.UTF_8)를 통하여 String을 decode 해준다.
    - 이 때 decdode는 Java 8까지는 인자로 String 2개를 받았었는데, Java 11 이후로는 String과 Charset을 받을 수 있게 추가가 되어 이용이 가능했던 것이다.

<br>

### Stream vs Buffer
- Stream은 한 글자씩 받아오는 반면, Buffer는 일정한 공간만큼 모아서 오는 차이가 있다.
  - node.js에서 Stream은 버퍼들을 포함하는 개념 (Java에서는 위의 개념으로 쓰임)
- 참고 : https://stackoverflow.com/questions/43147069/how-do-an-inputstream-inputstreamreader-and-bufferedreader-work-together-in-jav
---

## Step3 : POST로 회원가입

### try-with-resources
- `try-with-resources`는 AutoCloseable 인터페이스를 구현한 리소스에 대하여 finally 직전에, close()를 호출한다.
- Closeable vs AutoCloseable ?
  - Closeable (extends AutoCloseable)
    - JDK 5 ~
    - IOException
  - AutoCloseable
    - JDK 7+ ~
    - Exception
- `try-with-resources` 블록 내에서의 리소스 반환 순서 : 괄호 안에 생성된 역순으로 close() 호출 

### Optional OrElse, OrElseGet
- orElse
  - 반환 값을 그대로 받는다.
  - 무조건 뒤의 로직이 실행된다.

- orElseGet
  - Supplier로 wrapping한 값을 받는다.
  - **`null`일 경우에만** 뒤의 로직이 실행된다.(lazy)
