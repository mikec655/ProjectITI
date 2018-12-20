import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

class TestExtrapolate {

	@Test
	void testOne() {
		Deque<Double> queue =new ArrayDeque<Double>();
		for(int x=1 ;x <= 30; x++) {	
			queue.add((double) x);
		}
	
		Station test = new Station();
		/*double output = test.extrapolate(queue);;
		assertEquals(31.0, output);*/
	}
	
	@Test
	void testTwo() {
		Deque<Double> queue =new ArrayDeque<Double>();
		queue.add(2.0);
		queue.add(-2.0);
		queue.add(2.0);
		queue.add(-2.0);
		queue.add(2.0);
		queue.add(-2.0);
	
		Station test = new Station();
//		double output = test.extrapolate(queue);;
//		assertEquals(0.0, output);
	}
}
