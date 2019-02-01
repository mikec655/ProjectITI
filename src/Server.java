
import java.net.ServerSocket;
import java.net.Socket;
  
public abstract class Server 
{ 
    private static final int PORT = 11000;
    private static final int MAX_CLIENTS = 800;
	private static int clientCounter = 0;
	private static ServerSocket server;
  
    public static void main(String args[]) 
    { 

        Socket conn; // Client socket
		try {
			// Server socket die luistert naar nieuwe clients
			server = new ServerSocket(PORT);
			System.out.println("Server Started, Waiting for clients.");
			
			while (true) {
				// Accepteren van nieuwe clients
				if (clientCounter <= MAX_CLIENTS) {
					conn = server.accept();		
					// Client in Thread stoppen
					Client client = new Client(conn);
					Thread thread = new Thread(client);
					thread.start();
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