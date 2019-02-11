
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

public class WriteBuffer implements Runnable{
	
	private HashMap<Integer, ByteBuffer> data = new HashMap<Integer, ByteBuffer>(); 
	private Semaphore semaphore = new Semaphore(1);
	private String date = new String();
	
	public void addRecord(int time, byte[] record) {
		ByteBuffer data = this.data.get(time);
		if (data == null) {
			data = ByteBuffer.allocate(43 * 8000);
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			this.data.put(time, data);
			semaphore.release();
		}
		data.put(record);
	}
	
	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public void run() {
		while (true) {
			int currentTime = (int) (System.currentTimeMillis() % (24 * 60 * 60 * 1000));
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Iterator<Entry<Integer, ByteBuffer>> it = data.entrySet().iterator();
			while (it.hasNext()) {
				int time = it.next().getKey();
				if (time < currentTime + 5000) {
					System.out.println("New writer created at " + currentTime);
					ByteBuffer data = this.data.get(time);
					Writer writer = new Writer(this.date, time, data.array());
					Thread thread = new Thread(writer);
					thread.start();
					it.remove();
				}
			}
			semaphore.release();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e3) {
				e3.printStackTrace();
			}
		}
		
	}
	
	

}
