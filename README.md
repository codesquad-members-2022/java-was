### `WAS 구현하기 학습 READEME`

`DataOutputStream / PrintWriter 의 차이 ?`
- DataOutputStream : Byte Data 를 사용해 여러가지 형태로 사용할 수 있다.
- PrintWriter : 모든 데이터를 아스키 코드 형태로 변환한다. 
  - 바이트 코드를 읽어야할 때는 PrintWriter 를 사용할 수 없다. (이미지 등)
---
`InputStream ?`
- 이 추상 클래스는 바이트 입력 스트림을 나타내는 모든 클래스의 슈퍼 클래스이다. (JDK 11)
    1. `Stream 이란 이름은 왜 붙었을까 ?` 
    
       `위키 백과` 스트림이란 ?
       개별 바이트나 문자열인 데이터의 원천, 파일을 읽거나 쓸 때 네트워크 소켓을 거쳐 통신할 때 쓰이는
       추상적인 개념이다. 
       쉽게 말하면 `데이터가 전송되는 통로` 라고 이해하면 될 것 같다. 네트워크, 파일, 키보드 등 어디서
       오던간 데이터가 오고가는 통로가 스트림인 것이다.
    
    2. `바이트 입력 스트림 ?`

       바이트는 원래 알고 있는 것과 동일하다. 그냥 데이터라고 이해하자.
       1번과 종합하면 `InputStream` 이란 추상 클래스는 데이터가 들어오는 통로의 역할에 관해 규정
       하는 추상 클래스라는 것을 알 수 있다.
       `InputStream` 은 read() 를 통해 1byte 씩 값을 읽는다. 만약 읽을 값이 없다면 -1 을 반환한다.

    3. `종합`
       `InputStream`은 데이터를 byte 단위로 읽어들이는 통로이다. (읽어들인 데이터는 byte로 돌려줌)
        1. 데이터 읽기
        2. 특정 시점으로 되돌아가기
        3. 데이터가 얼마나 남았는지 보여주기
        4. 통로 끊기 등의 기능을 제공한다.

- InputStreamReader ?
    1. InputStream 과 다른 점

        ```java
        public static void main(String[] args) throws IOException {
                InputStream in = System.in;
        		
              이렇게 InputStreamReader 는 InputStream 객체를 입력으로 가지고 있어야 한다.
                InputStreamReader reader = new InputStreamReader(in);
        
                이렇게 char[] 형태로 받을 수 있지만 지정된 배열 인덱스 만큼만 입력받아야 한다.
                char[] ch = new char[3];
                reader.read(ch);
        
                System.out.println(ch);
        }
        ```

       `InputStreamReader` 는 byte 단위가 아닌 char 단위로 입력을 받을 수 있다.
       즉, 키보드로 입력하는 글자 한 개를 바로 입력받을 수 있도록 InputStream 을 개선한 것이다.
       하지만 한 글자가 아닌 줄 단위의 `문자열` 을 입력받으려면 마찬가지로 너무너무 불편하다.
       왜냐하면 배열 크기를 일일이 정해줘야 하기 때문이다. 얼마나 길게 입력할지 아무도 모르는데 무한
       으로 크게 배열을 잡아놓을 순 없다. 그래서 나온게 `BufferedReader` !

- BufferedReader ?

    ```java
    public static void main(String[] args) throws IOException {
            InputStream in = System.in;
            InputStreamReader reader = new InputStreamReader(in);	
            BufferedReader br = new BufferedReader(reader);
    	
            위 3줄의 로직을 합친게 원래 평소에 사용하는 형태
            이렇게 BufferedReader 는 InputStreamReader 객체를 입력으로 가지고 있어야 한다.
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
            String str = br.readLine();
            System.out.println(str);
    		
    }
    ```

  `BufferedReader` 는 버퍼에 문자열로 저장한다.  
  readLine() 메서드의 경우 개행 문자 기준으로 한 줄의 문자열을 읽는다.

3. `UTF-8` 인코딩을 사용하는 이유 ?
- 유니코드 한 문자를 나타내기 위해서는 1byte ~ 4byte 까지를 사용한다.
- 한글의 경우 3byte 를 사용하고, 모든 유니코드를 표현할 수 있는 인코딩 방식이기 때문에 대부분 `UTF-8` 을 사용하는 것이 권장된다.  


---
- 프로그램 작동 방식 간단 정리
    1. WebServer 클래스에서 ServerSocket 을 생성한 뒤 클라이언트 요청을 기다린다.
    2. 클라이언트 요청이 오면, 새로운 Socket 객체를 생성한 뒤 반환해준다.
    3. 해당 Socket 으로 RequestHandler 스레드를 실행시킨다.
    4. RequestHandler.run() 메서드에서 요청 헤더를 읽고 요청에 알맞게 ResponseHeader, ResponseBody 를 반환해 응답한다.
   
---

- 미션 3 까지의 작동 방식 간단 정리
  1. Socket의 InputStream을 통해서 클라이언트의 요청을 RequestReader를 통해서 Request 객체에 저장한다.
  2. 저장된 Request 객체를 통해서 FrontController로 RequestMapping한다.
  3. mapping된 컨트롤러가 로직을 처리하고 Response를 반환한다.
  4. ResponseWriter가 OutputStream과 Response를 통해서 클라이언트에게 응답을 보낸다.

---
