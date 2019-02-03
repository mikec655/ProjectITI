import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;


public class Station {
	private byte[] records = new byte[60 * 43];
	private int recordIndex = 0;
	private int stn;
	private String date;
	private Deque<Float> tempQueue = new ArrayDeque<Float>();
	private float tempSum;
	private Deque<Float> dewpQueue = new ArrayDeque<Float>();
	private float dewpSum;
	private Deque<Float> stpQueue = new ArrayDeque<Float>();
	private float stpSum;
	private Deque<Float> slpQueue = new ArrayDeque<Float>();
	private float slpSum;
	private float visib;
	private Deque<Float> wdspQueue = new ArrayDeque<Float>();
	private float wdspSum;
	private float prcp;
	private float sndp;
	private int frshtt;
	private float cldc;
	private short wnddir;
	
	// Add bytes to byte array
	public void addRecord(byte[] record) {
		for(int i = 0; i < 43; i++) {
			this.records[recordIndex * 43 + i] = record[i];
		}
		recordIndex++;
		if (recordIndex == 60){
			recordIndex = 0;
			writeRecords();
		}
	}
	
	// Write bytes to file
	private void writeRecords() {
		try {
			File f = new File("data/" + date);
			f.mkdirs();
			f = new File("data/" + date + "/" + stn + ".dat");
			f.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(records);
			fos.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// Setters and Adders
	private void addValue(float value, float sum, Deque<Float> queue) {
		queue.add(value);
		sum += value;
		if (queue.size() > 30) {
			sum -= queue.poll();;
		}
	}
	
	public void setStn(int stn) {
		this.stn = stn;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void addTemp(float temp) {
		addValue(temp, tempSum, tempQueue);
	}
	
	public void addDewp(float dewp) {
		addValue(dewp, dewpSum, dewpQueue);
	}
	
	public void addStp(float stp) {
		addValue(stp, stpSum, stpQueue);
	}
	
	public void addSlp(float slp) {
		addValue(slp, slpSum, slpQueue);
	}
	
	public void setVisib(float visib) {
		this.visib = visib;
	}
	
	public void addWdsp(float wdsp) {
		addValue(wdsp, wdspSum, wdspQueue);
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

	public void setWnddir(short wnddir) {
		this.wnddir = wnddir;
	}

	// Getters and Extrapolation
	private float extrapolate(Deque<Float> queue, float sum) {
		float average = sum / queue.size();
		return Math.round(average * 10) / 10;
	}
	
	public String getDate() {
		return date;
	}

	public float extrapolateTemp() {
		return extrapolate(tempQueue, tempSum);
	}
	
	public float extrapolateDewp() {
		return extrapolate(dewpQueue, dewpSum);
	}
	
	public float extrapolateStp() {
		return extrapolate(stpQueue, stpSum);
	}
	
	public float extrapolateSlp() {
		return extrapolate(slpQueue, slpSum);
	}
	
	public float getVisib() {
		return visib;
	}
	
	public float extrapolateWdsp() {
		return extrapolate(wdspQueue, wdspSum);
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

	public short getWnddir() {
		return wnddir;
	}
	
	// Sizes of queues
	public int sizeOfTemp() {
		return tempQueue.size();
	}
	
}