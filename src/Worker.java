
public class Worker implements Runnable {
	private Client[] clients;
	private int clientCounter = 0;
	
	public Worker(int amountOfClients) {
		this.clients = new Client[amountOfClients];
	}
	
	public void addClient(Client client) {
		clients[clientCounter++] = client;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null) {
				clients[i].read();
			}
		}
		long stopTime = System.currentTimeMillis();
		long time = stopTime - startTime;
		System.out.println("worker took " + time);
	}

}
