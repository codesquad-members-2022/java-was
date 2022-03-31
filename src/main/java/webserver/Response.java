package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;

public class Response {

	private Logger log = LoggerFactory.getLogger(Response.class);
	private DataOutputStream dos;

	public Response(OutputStream out) {
		this.dos = new DataOutputStream(out);
	}

	public void write(byte[] body, HttpStatus httpStatus) {
		try {
			dos.writeBytes("HTTP/1.1 " + httpStatus.getMessage() + "\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("\r\n");
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void write(HttpStatus httpStatus, List<Pair> pairs) {
		try {
			dos.writeBytes("HTTP/1.1 " + httpStatus.getMessage() + "\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			for (Pair pair : pairs) {
				dos.writeBytes(pair.toString() + "\r\n");
			}
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
