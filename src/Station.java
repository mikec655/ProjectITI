import java.util.ArrayDeque;
import java.util.Deque;


public class Station {
	private Deque<Double> tempQueue = new ArrayDeque<Double>();
	private Deque<Double> dewpQueue = new ArrayDeque<Double>();
	private Deque<Double> stpQueue = new ArrayDeque<Double>();
	private Deque<Double> slpQueue = new ArrayDeque<Double>();
	private double visib;
	private Deque<Double> wdspQueue = new ArrayDeque<Double>();
	private double prcp;
	private double sndp;
	private String frshtt;
	private double cldc;
	private int wnddir;
	
	// Setters and Adders
	private void addValue(double value, Deque<Double> queue) {
		queue.add(value);
		if (queue.size() > 30) {
			queue.poll();
		}
	}
	
	public void addTemp(double temp) {
		addValue(temp, tempQueue);
	}
	
	public void addDewp(double dewp) {
		addValue(dewp, dewpQueue);
	}
	
	public void addStp(double stp) {
		addValue(stp, stpQueue);
	}
	
	public void addSlp(double slp) {
		addValue(slp, slpQueue);
	}
	
	public void setVisib(double visib) {
		this.visib = visib;
	}
	
	public void addWdsp(double wdsp) {
		addValue(wdsp, wdspQueue);
	}
	
	public void setPrcp(double prcp) {
		this.prcp = prcp;
	}

	public void setSndp(double sndp) {
		this.sndp = sndp;
	}

	public void setFrshtt(String frshtt) {
		this.frshtt = frshtt;
	}

	public void setCldc(double cldc) {
		this.cldc = cldc;
	}

	public void setWnddir(int wnddir) {
		this.wnddir = wnddir;
	}

	// Getters and Extrapolations
	private double extrapolate(Deque<Double> queue) {
		float f = 0;
		double somy = 0;
		double somxy = 0;
		double x = 1;
		double somx = 0;
		double somx2 = 0;
		int n = queue.size();
		for(double y: queue) {
			double product = x * y;
			somxy += product;
			somy += y;
			somx += x;
			somx2 += Math.pow(x, 2);
			x++;
		}
		double gemx = somx / n;
		double gemy = somy / n;
		double rc = ((n * somxy) - (somx * somy)) / ((n * somx2) - (Math.pow(somx, 2)));
		double constant = gemy - rc*gemx;
		double newValue = constant + rc*(n+1); 
		return newValue;
	}

	public double extrapolateTemp() {
		return extrapolate(tempQueue);
	}
	
	public double extrapolateDewp() {
		return extrapolate(dewpQueue);
	}
	
	public double extrapolateStp() {
		return extrapolate(stpQueue);
	}
	
	public double extrapolateSlp() {
		return extrapolate(slpQueue);
	}
	
	public double getVisib() {
		return visib;
	}
	
	public double extrapolateWdsp() {
		return extrapolate(wdspQueue);
	}

	public double getPrcp() {
		return prcp;
	}

	public double getSndp() {
		return sndp;
	}

	public String getFrshtt() {
		return frshtt;
	}

	public double getCldc() {
		return cldc;
	}

	public int getWnddir() {
		return wnddir;
	}
	
	// Sizes of queues
	public int sizeOfTemp() {
		return tempQueue.size();
	}
	
}