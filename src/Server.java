
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit; 
  
public abstract class Server 
{ 
    private static final int PORT = 11000;
    private static final int MAX_CLIENTS = 800;
    private static final int THREADS = 4;
	private static int clientCounter = 0;
	private static ScheduledExecutorService executor;
	private static Worker[] workers;
	private static ServerSocket server;
  
    public static void main(String args[]) 
    { 
    	new File("data").mkdir();
    	
    	// Aanmaken van een Threadpool met Scheduler
    	executor = Executors.newScheduledThreadPool(THREADS);
    	
    	workers = new Worker[THREADS];
    	for (int i = 0; i < THREADS; i++) {
    		workers[i] = new Worker(MAX_CLIENTS / THREADS);
    		Thread thread = new Thread(workers[i]);
    		thread.setPriority(Thread.MAX_PRIORITY);
    		executor.scheduleAtFixedRate(thread, 1000, 1000, TimeUnit.MILLISECONDS);
    	}
    	

        Socket conn; // Client socket
		try {
			// Server socket die luistert naar nieuwe clients
			server = new ServerSocket(PORT);
			System.out.println("Server Started, Waiting for clients.");
			
			while (true) {
				// Accepteren van nieuwe clients
				if (clientCounter <= 8000) {
					conn = server.accept();		
					// Client in Thread stoppen
					Client client = new Client(conn);
					workers[clientCounter % THREADS].addClient(client);
					clientCounter++;
					System.out.println("New client accepted. client count: " + clientCounter);
				} else {
					System.out.println("Client not accepted. client count is already " + MAX_CLIENTS);
				}
			}
		}

		catch (java.io.IOException e) { 
			System.out.println(e);
		}        
    } 
} 