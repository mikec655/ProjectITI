
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit; 
  
public class Server 
{ 
    private static final int PORT = 11000;
    private static final int AMOUNT_OF_CORES = 8;
    private static final int MAX_CLIENTS = 800;
	private static int clientCounter = 0;
  
    public static void main(String args[]) 
    { 
    	// Connectie met de database opstarten
    	Database.connect();
    	
    	// Aanmaken van een Threadpool met Scheduler
    	ScheduledExecutorService exec = Executors.newScheduledThreadPool(MAX_CLIENTS + 2);
		// JVM vragen voor Garbage Collection
    	exec.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					System.gc();
				}
		}
		, 0, 1, TimeUnit.SECONDS);
//    	exec.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				Database.executeQuery();
//			}
//		}
//		, 0, 1, TimeUnit.MILLISECONDS);

    	
        Socket connection; // Client socket
		try {
			// Server socket die luistert naar nieuwe clients
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server Started, Waiting for clients.");
			
			while (true) {
				// Accepteren van nieuwe clients
				connection = server.accept();		
				System.out.println("New client accepted.");
				// Client in Thread stoppen
				Thread client = new Thread(new Client(connection));
				clientCounter++;
				//Thread toevoegen aan Threadpool
				exec.scheduleAtFixedRate(client, 1000 / (MAX_CLIENTS / AMOUNT_OF_CORES) * clientCounter, 1000, TimeUnit.MILLISECONDS);
			}
		}

		catch (java.io.IOException e) { 
			System.out.println(e);
		}        
    } 
} 