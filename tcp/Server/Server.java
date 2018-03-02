import java.io.*;
import java.net.*;

public class Server implements Runnable{
	
	private ServerSocket serverSocket;
	private String[] clientCommand;

	public Server(String[] clientCommand){
		this.clientCommand = clientCommand;
		try{
			serverSocket = new ServerSocket(6145); 
		}catch(IOException e){
			e.printStackTrace();
		}		
	}

	private void serve(){
		Process p;
		try{
			p = Runtime.getRuntime().exec(clientCommand);
		}catch(IOException e){	
			e.printStackTrace();
		}	
		Thread t = new Thread(new ClientHandler(serverSocket));
		t.start();
		try{
			t.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void run(){
		serve();
	}

}
