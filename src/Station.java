import java.util.ArrayDeque;
import java.util.Deque;


public class Station {
	private Deque<Float> tempQueue = new ArrayDeque<Float>();
	private Deque<Float> dewpQueue = new ArrayDeque<Float>();
	private Deque<Float> stpQueue = new ArrayDeque<Float>();
	private Deque<Float> slpQueue = new ArrayDeque<Float>();
	private float visib;
	private Deque<Float> wdspQueue = new ArrayDeque<Float>();
	private float prcp;
	private float sndp;
	private String frshtt;
	private float cldc;
	private int wnddir;

	// Setters and Adders
	private void addValue(float value, Deque<Float> queue) {
		queue.add(value);
		if (queue.size() > 30) {
			queue.poll();
		}
	}
	
	public void addTemp(float temp) {
		addValue(temp, tempQueue);
	}
	
	public void addDewp(float dewp) {
		addValue(dewp, dewpQueue);
	}
	
	public void addStp(float stp) {
		addValue(stp, stpQueue);
	}
	
	public void addSlp(float slp) {
		addValue(slp, slpQueue);
	}
	
	public void setVisib(float visib) {
		this.visib = visib;
	}
	
	public void addWdsp(float wdsp) {
		addValue(wdsp, wdspQueue);
	}
	
	public void setPrcp(float prcp) {
		this.prcp = prcp;
	}

	public void setSndp(float sndp) {
		this.sndp = sndp;
	}

	public void setFrshtt(String frshtt) {
		this.frshtt = frshtt;
	}

	public void setCldc(float cldc) {
		this.cldc = cldc;
	}

	public void setWnddir(int wnddir) {
		this.wnddir = wnddir;
	}

	// Getters and Extrapolation
	private float extrapolate(Deque<Float> queue) {
		float average = 0;
		float sum = 0;
		for(float y: queue){
			sum += y; 
		}
		average = sum / queue.size();
		return average;
	}

	public float extrapolateTemp() {
		return extrapolate(tempQueue);
	}
	
	public float extrapolateDewp() {
		return extrapolate(dewpQueue);
	}
	
	public float extrapolateStp() {
		return extrapolate(stpQueue);
	}
	
	public float extrapolateSlp() {
		return extrapolate(slpQueue);
	}
	
	public float getVisib() {
		return visib;
	}
	
	public float extrapolateWdsp() {
		return extrapolate(wdspQueue);
	}

	public float getPrcp() {
		return prcp;
	}

	public float getSndp() {
		return sndp;
	}

	public String getFrshtt() {
		return frshtt;
	}

	public float getCldc() {
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