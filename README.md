# java-was

### step2 학습내용

##Louie
- 브라우저가 html 파일을 불러오기 위해 필요한 파일들(파비콘, css, js 등)을 알아서 요청한다.
<details>
    <summary>ServerSocket.accept()</summary>

- `accept()`가 호출되면 클라이언트가 서버 소켓에게 요청이 올 때까지 대기한다.
- 요청이 오면 새로운 클라이언트 소켓을 생성하는데 이때 포트 번호가 자동으로 할당된다.
- 현재 코드처럼 요청마다 Thread 생성해서 실행한다면 멀티 스레드 프로그래밍이 가능해진다.
</details>

<details>
    <summary>로그(Log)</summary>

## 로그 생성 방법

```java
private final Logger log = LoggerFactory.getLogger(getClass());
```

## 로그 레벨

```java
log.trace("trace log={}", name);
log.debug("debug log={}", name);
log.info(" info log={}", name);
log.warn(" warn log={}", name);
log.error("error log={}", name);
```

위에 보이는 5개의 로그 레벨을 많이 사용한다.

## 로그 레벨 설정

`application.properties`에서 설정한다.

```basic
// 전체 로그 레벨 설정(기본 info)
logging.level.root=info

// hello.springmvc 패키지와 그 하위 패키지의 로그 레벨 설정
logging.level.hello.springmvc=debug
```

## 올바른 로그 사용법

```java
log.debug("data="+data)
log.debug("data={}", data)
```

- 첫번째 줄은 로그가 출력되지 않아도 파라미터의 문자가 더해져서 쓸데없는 연산이 발생한다.
- 그래서 두번째 줄처럼 로그를 출력 해야한다.

## 로그의 장점
- 쓰레드, 클래스 이름 같은 정보를 볼 수 있다.
- 코드를 수정하지 않고 애플리케이션 설정만으로 원하는 로그만 출력할 수 있다.
- 로그의 내용을 다른 파일에 저장할 수 있다.
- 내부 버퍼링, 멀티 쓰레드 같은 기능을 통해 성능도 System.out 보다 더 좋다.
</details>


##Lee
## 한 페이지를 요청할 때 브라우저의 멀티 스레딩
로그를 찍어 보니 한 페이지를 로드할 때 1)정적 html 파일 2)파비콘 3)CSS 4)자바스크립트 요청이 각각 서로 다른 스레드를 통해서 순차적으로 들어오는 것을 확인할 수 있었습니다.
처음에는 단일 스레드로 요청되지 않는 것이 의아했는데, 팀원과 이야기를 나누다 보니 사실 일단 정적 html 파일만 로드되면 이후 요소들은 순차적으로 로드하고, 모종의 문제로 해당 자원이 로드되지 못할 때는 우선 로드된 내용들만 보여주는 방식이 더 효율적이겠다는 생각이 들었습니다.

## URL 한글 인코딩 / 디코딩 문제
URL 쿼리스트링 등에 한글이 있을 때 인코딩/디코딩 문제를 해결하기 위해 URLDecoder 등을 사용했습니다. 자세한 내용은 추후 정리해 볼 예정입니다.

## Git rebase 정리
merge 후 rebase 과정에서, 항상 "어느 브랜치에서 리베이스 명령어를 써야 할까?"가 헷갈렸는데요. 이번 단계를 진행하면서 브랜치를 정리하는 과정에서 명확히 이해하게 되었습니다. experiment 브랜치에서 따로 작업한 내용을 main 브랜치의 HEAD 뒤로 옮겨와 병합하기 위해서는 'experiment 브랜치에서 "git rebase main"을 실행'해야 합니다.
이후 rebase 과정에서 merge와 마찬가지로 변경사항 충돌이 일어날 수 있는데요. merge와 rebase의 충돌이 다른 점은, merge는 하나의 머지 커밋을 만들면서 충돌사항이 한 커밋에 모이기 때문에 충돌을 해결하고 그대로 commit하면 되지만, rebase는 병합되어 들어오는 브랜치의 커밋을 하나하나 차례로 병합해 나가는 방식이기 때문에, 커밋 단위로 충돌이 생겨서 이를 수정하고 그 이후 rebase를 계속하는 방식입니다. merge와 비슷하다고 생각해 충돌 수정 후 commit을 진행하는 등의 실수가 있었는데요. 수정 이후 git rebase --continue를 사용해야 해당 커밋의 리베이스가 완료되고 이후 리베이스 과정이 계속됩니다. 이후 커밋에서 충돌이 또 발생하면 같은 방식으로 진행하면 됩니다.


