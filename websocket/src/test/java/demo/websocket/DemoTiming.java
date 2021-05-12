package demo.websocket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class DemoTiming {
	public static void main(String args[]) throws InterruptedException {

		long start = System.currentTimeMillis();
		long end = start + 6 * 1000;
		int win = 4;

		do {
			Thread.sleep(1000);
			System.out.println(System.currentTimeMillis() > end);
			System.out.println(System.currentTimeMillis() - end);
			if (System.currentTimeMillis() > end) {
				break;
			}
		} while (win > 1);
	}
}
