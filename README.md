# java-was

Java Web Server Project for CodeSquad Members 2022

## 새롭게 알게 된 내용

- 클라이언트의 CSS, JavaScript 파일 요청 과정 프로그램 실행시 요청이 들어온 url을 모두 출력해보니 index.html 파일 응답 후, CSS, JavaScript파일명이 담긴 요청이 새로 들어오는
  것을 확인할 수 있었다. 이는 브라우저의 렌더링 엔진이 서버로부터 응답받은 HTML 파일을 렌더링하는 도중에 `<link>`, `<script>` 태그를 만나면 서버에 추가적으로 자원을 요청하기 때문이라고
  한다.  
  [참고 링크](https://nohack.tistory.com/36)


- `try-with-resource` 코드의 실행 위치가 try 문을 벗어나면 try-with-resources는 try(...) 안에서 선언된 객체의 close() 메소드들을 호출한다. 그래서 finally에서
  close()를 명시적으로 호출해줄 필요가 없다. try-with-resources에서 자동으로 close가 호출되는 것은 AutoCloseable을 구현한 객체에만 해당된다.  
  [참고링크](https://codechacha.com/ko/java-try-with-resources/)


- 자바의 File클래스는 절대경로와 상대경로를 둘 다 지원한다. 그런데 상대경로의 경우, 일반적으로 사용하는 것처럼 해당 파일이 있는 위치가 아니라 프로젝트 폴더의 위치를 기준으로 계산한다.  
  [참고링크](https://ohgyun.com/169)
