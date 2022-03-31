package util;

import java.util.List;

public class PrintUtils {

	private PrintUtils() {
	}

	public static void printRequestHeaders(List<Pair> headerPairs, String requestLine) {
		System.out.println(requestLine);
		for (Pair pair : headerPairs) {
			System.out.println(pair);
		}
	}
}
