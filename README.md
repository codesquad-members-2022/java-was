# java-was

Java Web Server Project for CodeSquad Members 2022

<br/><br/><br/>

## ✍🏻 요구사항

회원가입한 사용자로 로그인을 할 수 있어야 한다. 이때 Cookie를 활용해 회원가입한 데이터를 유지해야 한다.

<br/><br/><br/>

## To-do List

- [x] 회원가입한 사용자로 로그인을 할 수 있다.
- [x] 로그인 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
- [x] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동한다.
- [x] Set-Cookie 설정시 모든 요청에 대해 Cookie가 처리 가능하도록 Path 설정 값을 `Path=/`로 설정한다.
- [x] 쿠키 값이 header에 함께 전달되는지 확인한다.

<br/><br/><br/>

## 학습 중점사항

if문으로 분기하는 것의 한계를 많이 느껴 전체적인 구조를 많이 개선했습니다. 또한 실제 스프링의 구조를 참조하며 HTTP 헤더와 바디를 학습하면서 어떻게 비슷하게 만들까에 중점을 두고 학습했습니다.

<br/><br/><br/>

## 구현 중 학습한 내용

<details>
<summary>📝 1단계 학습내용</summary>
<br/>

- HTTP 동작방식에 대해 생각을 정리하는 시간을 가졌습니다. [그림 출처](https://pearlluck.tistory.com/117)
    1. 사용자가 브라우저에 URL 주소 입력<br/><br/>
    2. DNS서버에 의해 IP주소 찾음
        - `IP주소`: 컴퓨터 네트워크에서 장치들이 서로 인식하기 위한 특수번호
        - `MAC주소`: 네트워크 인터페이스에 할당된 고유 식별자(변경불가)
        - `ARP(Address Resolution Protocol`: 주소 결정 프로토콜(목적지 IP주소값을 가지고 상대방의 MAC주소를 찾음)<br/><br/><br/>
    3. 웹서버와 TCP 연결시도
        - 3-way-handshaking: 클라이언트와 서버간에 신뢰성 있는 연결을 위한 3번의 패킷쿄환 과정
            1. Client -> Server: 처음으로 패킷을 보낸다(SYN)
            2. Server -> Client: Client가 보낸거 잘받았고(ACK), 내가 처음으로 패킷을 보낸다(SYN)
            3. Client -> Server: Server가 보낸거 잘받았고(ACK) 이제 서로 데이터(`HTTP메세지`)를 보낼 준비 완료
               ![3way-handshake](https://t1.daumcdn.net/cfile/tistory/225A964D52F1BB6917)
               <br/><br/><br/>
    5. 웹서버와 http메세지 주고받음
        - HTTP : Hyper Text Transfer Protocol, Hyper Text를 전송하기 위한 프로토콜, Request&Response 주고받는 역할 클라이언트가 서버에 요청을 보내면, 그에
          맞는 응답 결과를 돌려주고, 클라이언트는 사용자에게 서버로부터 응답받은 결과를 보여주는 것
          ![http통신](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbMcIdM%2FbtqDcdvjYXA%2FVML0aacRlfdAw41dD3f1LK%2Fimg.png)
          ​
        - 4-1. 클라이언트 -> 서버 : request 전송
            - 요청메세지 : 메소드/ 요청UTL / HTTP버전
        - 4-2. 서버 -> 클라이언트 : response 받음
            - 응답메세지 : HTTP버전/상태코드/사유구절
              <br/><br/><br/>
    6. 웹서버와 TCP연결 해제
        - 4-way-handshaking : 클라이언트와 서버간에 양쪽다 연결을 종료시킨다
            1. Client->Server : 처음으로 종료한다는 패킷을 보낸다(FIN)
            2. Sever ->Client : Client가 보낸거 잘받았고(ACK)
            3. Sever ->Client : Server가 처음으로 종료한다는 패킷을 보낸다(FIN)
            4. Client-> Server : Server가 보낸거 잘받았고(ACK) 이제 서로연결 종료한다.
               ![4way-handshake](https://t1.daumcdn.net/cfile/tistory/2152353F52F1C02835)                
               ​ 이 과정에서 생긴 궁금점은 현재 로컬(내 PC)에서 웹서버를 실행하여 웹브라우저로 접근을 했을 때 내 PC에서 동작중인 웹서버에 접속을 하는 데에도 TCP 커넥션을 맺는지
               궁금했습니다.</br>
               와이어샤크로 확인해본 결과 로컬 내에서 접속을 해도 커넥션을 맺는 것을 확인할 수 있었습니다. ​
               ![와이어샤크 확인 결과](https://user-images.githubusercontent.com/86910955/159453454-adc91804-434e-4e1e-aa8d-0e30b8b4c991.png)
               ​ ​
               <br/><br/>

</details>

<details>
<summary>📝 2단계 학습내용</summary>
<br/>

### URL파싱과 데이터 전송까지만 구현했기 때문에 url이 리다이렉션 되지는 않습니다.

- GET 방식 데이터 전송
    - URL 뒤에 ? 마크를 통해 데이터를 전송한다.
    - 2개 이상의 key, value 쌍 데이터를 보낼 때는 & 마크로 구분한다.
    - GET 방식의 요청은 캐싱 방식을 사용하기 때문에 다른 데이터 전송보다 속도가 빠르다.
      <br/><br/><br/>

`학습하면서 GET 방식이 왜 더 빠를까` 를 고민하다 이것저것 실험해 보았습니다. 아직 구현하지 못했지만 학습한 내용은 다음과 같습니다.

- 데이터가 변경되지 않는 상황에서 굳이 같은 데이터를 내려줄 필요가 없기 때문에 다양한 캐싱 전략을 사용한다. 캐싱을 위해서는 cache-control과 last-modified-since와 같은 값들을 사용할 수
  있다.

`웹 브라우저에는 캐시를 저장하는 저장소가 있는데 이 경우 두 번쨰 요청시 저장소를 통해 네트워크 통신을 하지 않아도 되며, 하드디스크를 통해 바로 데이터를 가져올 수 있다.
`
<br/><br/><br/>

- cache-control, 을 통해 캐시가 유효한 시간을 명시한다.
- Last modified-since를 통해 데이터가 변경된 지 알 수 있다.

![http이미지](https://user-images.githubusercontent.com/86910955/160075123-4d0d2342-dc94-4703-9f6a-ba5496a007ae.png)

참고자료

- https://itstory.tk/entry/Spring-MVC-LastModified-IfModifiedSince-%EC%BA%90%EC%8B%9C-%EC%84%A4%EC%A0%95
- https://stackoverflow.com/questions/10498135/last-modified-header-in-mvc

<br/><br/><br/><br/>

스프링에서 어떻게 이를 사용할 수 있을까 하고 찾아보던 중 아래와 같은 방식을 찾았지만 아직까지 정확하게 어떻게 사용해야 할지는 감이 잘 잡히지 않습니다. 3, 4단계 미션을 수행해 가면서 방법을 찾아보겠습니다.

</details>

<details>
<summary>📝 3단계 학습내용</summary>
<br/>

Post 방식을 통해 데이터를 전송할 수 있다.

- GET방식은 URL에 데이터를 함께 전송하지만 Post는 Form 형식으로 전송한다.
- POST방식은 GET에 비해 보안성이 높으며 길이 제한이 없다.

- name: 스크립트나 서버에서 폼을 식별하기 위한 폼의 이름
- action: 폼 데이터가 전송되는 주소의 url
- method: http메서드 전송 방식

</details>

<details>
<summary>📝 4단계 학습내용</summary>
<br/>

HTTP body와 표현 헤더, 쿠키 헤더 부분의 지식이 부족해서 이 부분을 중점적으로 공부해보았습니다.

## 1. HTTP BODY

- 메시지 본문을 통해 표현 데이터 전달
- 메시지 본문 = 페이로드(payload)
- 표현은 요청이나 응답에서 전달할 실제 데이터
- 표현 헤더는 표현 데이터를 해석할 수 있는 정보 제공

## 2. 표현 헤더

- Content-Type : 표현 데이터의 형식 설명
    - 미디어 타입, 문자 인코딩
- Content-Encoding : 표현 데이터 인코딩
    - 표현 데이터를 압축하기 위해 사용
    - 데이터를 전달하는 곳에서 압축 후 인코딩 헤더 추가
    - 읽은 쪽에서 인코딩 헤더의 정보로 압축 해제
    - ex) gzip(압축), deflate, identity(압축안함)
- Content-Language : 표현 데이터의 자연 언어 표현
    - ex) ko, en, en-US
- Content-Length : 표현 데이터의 길이
    - 바이트 단위
    - Transfer-Encoding(전송 코딩)을 사용시 사용 불가

## 3. 쿠키 헤더

- Set-Cookie : 서버에서 클라이언트로 쿠키 전달(응답)
- Cookie : 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 서버로 전달
  <br/>
  HTTP는 무상태(Stateless) 프로토콜이므로 요청과 응답을 주고 받으면 연결이 끊어진다. 만약 로그인하고 진행해야하는 상황이라면 모든 요청에 로그인 정보를 넘겨야 하는 것이다. 아래 그림과 같이 쿠키는
  모든 요청에 자동으로 포함되기 때문에 이런 문제를 해결할 수 있다.

![쿠키_로그인](https://user-images.githubusercontent.com/86910955/161429075-4379176a-5731-4ed2-9bb8-c7484daafbb8.png)
![로그인이후_페이지접근](https://user-images.githubusercontent.com/86910955/161429077-882340f3-c7f5-4165-8b64-272e7db788b7.png)

- 사용처
    - 사용자 로그인 세션 관리
    - 광고 정보 트래킹
- 쿠키 정보는 항상 서버에 전송
    - 네트워크 트래픽 추가 유발하므로 최소한의 정보만 사용(세션 id, 인증 토큰 등)
    - 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리지 참고
- 생명주기
    - 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
    - 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
        - Set-Cookie: expires=Sat, 26-Dec-2020 04:39:21 GMT
        - 만료일이 되면 쿠키 삭제
        - Set-Cookie: max-age=3600
        - 초단위 만료기간 지정, 0이나 음수를 지정하면 쿠키 삭제
- 도메인
    - 명시: 명시한 문서 기준 도메인 + 서브 도메인을 포함하여 쿠키를 전송
    - ex) domain=example.org 지정해서 쿠키 생성하면 dev.example.org도 쿠키 전송
    - 생략 : 현재 문서 기준 도메인만 적용
    - ex) example.org에서 쿠키 생성하고 domain 지정 생략하면 example.org 에서만 쿠키 전송
- 경로
    - 도메인으로 한 번 필터해주고 경로로 추가로 필터링
    - 이 경로를 포함한 하위 경로 페이지만 쿠키 전송
    - 일반적으로 path=/ 루트로 지정한다. 한 도메인 안에서 보통 쿠키를 다 전송하기를 원하기 때문
    - ex) path=/home 지정하면 /home , /home/level1 쿠키전달 가능, /hello 전달 불가능
- 보안
    - Secure : 쿠키는 http, https 구분하지 않고 전송하는데 secure 적용시 https 경우에만 쿠키 전송
    - HttpOnly : 자바스크립트에서 쿠키 접근 불가능하게 하는 방법, HTTP 전송에만 사용
    - SamSite : 요청 도메인과 쿠키에 설정된 도메인이 같은 경우만 쿠키 전송

</details>
