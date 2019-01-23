import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;


public class Station {
	private byte[][] records = new byte[60][43];
	private int recordIndex = 0;
	private int stn;
	private String date;
	private Deque<Float> tempQueue = new ArrayDeque<Float>();
	private Deque<Float> dewpQueue = new ArrayDeque<Float>();
	private Deque<Float> stpQueue = new ArrayDeque<Float>();
	private Deque<Float> slpQueue = new ArrayDeque<Float>();
	private float visib;
	private Deque<Float> wdspQueue = new ArrayDeque<Float>();
	private float prcp;
	private float sndp;
	private int frshtt;
	private float cldc;
	private int wnddir;
	
	// bytes
	public void addRecord(byte[] record) {
		records[recordIndex] = record;
		recordIndex++;
		if (recordIndex == 60){
			recordIndex = 0;
			writeRecords();
		}
	}
	
	private void writeRecords() {
		long startTime = System.currentTimeMillis();
		try {
			File f = new File("Public/data/" + date);
			f.mkdirs();
			f = new File("Public/data/" + date + "/"+ stn + ".dat");
			f.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(f, true);
			for (int i = 0; i < 60; i++) {
				fos.write(records[i]);
			}
			fos.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		long stopTime = System.currentTimeMillis();
		long speed = stopTime - startTime;
		System.out.println("Writing XML took " + speed + "ms");
	}

	// Setters and Adders
	private void addValue(float value, Deque<Float> queue) {
		queue.add(value);
		if (queue.size() > 30) {
			queue.poll();
		}
	}
	
	public void setStn(int stn) {
		this.stn = stn;
	}
	
	public void setDate(String date) {
		this.date = date;
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

	public void setFrshtt(int frshtt) {
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
		return Math.round(average * 10) / 10;
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

	public int getFrshtt() {
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