package util;

import java.util.List;
import util.HttpRequestUtils.Pair;

public class PrintUtils {

	private PrintUtils() {
	}

	public static void printRequestHeaders(List<Pair> headerPairs, String line) {
		System.out.println(line);
		for (Pair pair : headerPairs) {
			System.out.println(pair);
		}
	}


}
