## 미션 중 고민거리

### index.html 호출 시 스레드가 여러개 생성되는 이유?

![image](https://user-images.githubusercontent.com/92678400/159219578-972a6b49-7273-4cdd-b577-6ee5091239d3.png)

- css 파일 등을 읽어 오기 위한 것으로 보인다

![image](https://user-images.githubusercontent.com/92678400/159255864-0c8fa6c1-e642-465c-839a-349c9e3d8e64.png)

### css 파일을 못 읽어오는 이유

`dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");`

여기에서 Content-Type을 text/html로 지정했기 때문

-> 호출 url에 따라 MIME 타입을 적용해주는 MimeUtils 클래스를 만들어 적용하였다.
