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
