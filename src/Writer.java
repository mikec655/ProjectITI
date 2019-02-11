import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Writer implements Runnable {
	
	private String date;
	private int time;
	private byte[] data;
	
	public Writer(String date, int time, byte[] data) {
		this.date = date;
		this.time = time;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			long startTime = System.currentTimeMillis();
			File f = new File("data/" + date); 
			f.mkdirs();
			f = new File("data/" + date + "/" + time + ".dat");
			f.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(data);
			fos.close();
			long stopTime = System.currentTimeMillis();
			long writeTime = stopTime - startTime;
			System.out.println("writing file " + time + " took: " + writeTime + "ms");
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
