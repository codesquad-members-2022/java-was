package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSession {

	// UUID, userId
	private static Map<String, String> session = new HashMap<>();

	private static Logger log = LoggerFactory.getLogger(HttpSession.class);

	public static String makeUUID(String userId) {
		String uuid = UUID.randomUUID().toString();
		session.put(uuid, userId);
		log.debug("uuid, userId: {}, {}", uuid, userId);
		return uuid;
	}

	public static String checkUser(String uuid) {
		return session.get(uuid);
	}

	public static void logout(String uuid) {
		session.remove(uuid);
	}

}
