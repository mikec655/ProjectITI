import java.util.ArrayDeque;
import java.util.Deque;


public class Station{
	private Deque<Double> tempQueue =new ArrayDeque<Double>();
	
	public void addTemp(double temp) {
		addValue(temp, tempQueue);
	}
	
	public double extrapolateTemp() {
		return extrapolate(tempQueue);
	}
	
	private void addValue(double value, Deque<Double> tempQueue) {
		tempQueue.add(value);
		if (tempQueue.size() > 30) {
			tempQueue.poll();
		}
	}
	
	private double extrapolate(Deque<Double> queue) {
		double somy = 0;
		double somxy = 0;
		double x = 1;
		double somx = 0;
		double somx2 = 0;
		int n = queue.size();
		for(double y: tempQueue) {
			double product = x * y;
			somxy += product;
			somy += y;
			somx += x;
			somx2 += Math.pow(x, 2);
			x++;
		}
		
		double gemx = somx / n;
		double gemy = somy / n;
		
		/*
		System.out.println("somx:"+somx +"somx2:"+somx2+"gemx:"+gemx);
		System.out.println("somy:"+somy+"gemy:"+gemy + "somxy"+ somxy);
		*/
		double rc = ((n * somxy) - (somx * somy)) / ((n * somx2) - (Math.pow(somx, 2)));
		double constant = gemy - rc*gemx;
		double newValue = constant + rc*(n+1); 
		System.out.println("RC:"+ rc +" Constante:"+ constant);
		System.out.println("Volgendewaarde:"+ newValue);
		
		return newValue;
	}

}