# java-was

Java Web Server Project for CodeSquad Members 2022

<br/><br/><br/>

## ✍🏻 요구사항

POST방식을 통해 회원가입을 진행

<br/><br/><br/>

## To-do List

- [ ] HTML form을 통해 회원가입
- [ ] 가입 후 index.html 페이지로 이동
- [ ] 중복 ID로 가입을 시도할 경우 가입이 되지않고 가입 페이지로 이동

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
- 데이터가 변경되지 않는 상황에서 굳이 같은 데이터를 내려줄 필요가 없기 때문에 다양한 캐싱 전략을 사용한다. 캐싱을 위해서는 cache-control과 last-modified-since와 같은 값들을 사용할 수 있다.

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

스프링에서 어떻게 이를 사용할 수 있을까 하고 찾아보던 중 아래와 같은 방식을 찾았지만 아직까지 정확하게 어떻게 사용해야 할지는 감이 잘 잡히지 않습니다.
3, 4단계 미션을 수행해 가면서 방법을 찾아보겠습니다.

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
