
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*; 
  
public class Server 
{ 
    private ServerSocket server   = null;
    private int threadCounter = 0;
  
    // constructor met port 
    public Server(int port) 
    { 
        // start de server en wacht voor clients
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
            System.out.println("Waiting for a clients ..."); 
            
            // wacht op clients om een verbinding maken
            while (true){
	            Socket socket = server.accept(); 
	            threadCounter++;
	            Thread client = new Thread(new Client(socket, threadCounter));
				client.start();
				System.out.println("New client accepted, client count: " + threadCounter);
            }
        }
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) 
    { 
        // verbinding met database
    	Database db = new Database();
        db.executeTestQuery("");
        
        // start van de server
    	Server server = new Server(11000); 
    } 
} 