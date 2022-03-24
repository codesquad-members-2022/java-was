package webserver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


class RequestHandlerTest {

    @Mock
    private Socket socket;

    @Mock
    private RequestHandler requestHandler;

    @InjectMocks
    private BufferedReader bufferedReader;

    @Mock
    private OutputStream outputStream;

    @Mock
    private InputStream inputStream;

    @Captor
    private ArgumentCaptor<byte[]> valueCapture;

    @BeforeAll
    void init(){

    }

    @Test
    @DisplayName("")
    public void test01() throws IOException {
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));


        Mockito.verify(outputStream).write(valueCapture.capture());
        byte[] writtenData = valueCapture.getValue();
    }
}
