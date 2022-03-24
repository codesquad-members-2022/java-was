# java-was

Java Web Server Project for CodeSquad Members 2022

<br/><br/><br/>

## ✍🏻 요구사항

GET방식을 통해 회원가입을 진행할 수 있다.

<br/><br/><br/>

## To-do List

- [ ] index.html로 접속하면 회원가입 폼 파일을 내려준다.
- [ ] 이 폼을 통해 회원가입을 할 수 있다.
- [ ] 한글이 정확하게 입력되고 있는지 확인해야 한다.
- [ ] 가입한 회원 데이터는 리스트 형태로 저장한다
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

</details>
