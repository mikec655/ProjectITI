import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

class TestExtrapolate {

	@Test
	void test() {
		Deque<Double> tempQueue =new ArrayDeque<Double>();
		for(int x=1 ;x <= 30; x++) {	
			addValue(x, tempQueue);
		}
	
		Station test = new Station();
		double output = test.extrapolate(tempQueue);;
		assertEquals(31.0, output);
	}
	
	private void addValue(double value, Deque<Double> tempQueue) {
		tempQueue.add(value);
		if (tempQueue.size() > 30) {
			tempQueue.poll();
		}
}}
