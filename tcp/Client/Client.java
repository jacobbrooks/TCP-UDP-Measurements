import java.io.*;
import java.net.*;

public class Client{
	private Socket serverConnection;
	private DataInputStream input;
	private DataOutputStream output;
	private String addrToConnect;
	private static final int iterations = 200;

	public Client(String addrToConnect){
		this.addrToConnect = addrToConnect;		
		serverConnection = null;
		input = null;
		output = null;
		initStreamsAndSockets();
	}

	private void initStreamsAndSockets(){
		try{
			serverConnection = new Socket(addrToConnect, 6145);
			output = new DataOutputStream(serverConnection.getOutputStream());
			input = new DataInputStream(serverConnection.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void byteConversation(int messageSize, boolean kilo){
		int coefficient = 1;
		if(kilo){
			coefficient = 1024;
		}		
		try{	
			byte[] b = new byte[messageSize * coefficient];
			input.readFully(b, 0, messageSize * coefficient);
			output.write(b, 0, messageSize * coefficient);
			output.flush();	
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void messageConversation(int messageSize, int messageCount){
		try{
			byte[] b = new byte[messageSize * 1024];
			for(int i = 0; i < messageCount; i++){
				input.readFully(b, 0, messageSize * 1024);
				output.writeByte(1);
				output.flush();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void loopConversation(){
		for(int i = 0; i < iterations; i++){
			byteConversation(1, false);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(64, false);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(1024, false);
		}
		/* */
		for(int i = 0; i < iterations; i++){
			byteConversation(1, true);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(16, true);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(64, true);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(256, true);
		}
		for(int i = 0; i < iterations; i++){
			byteConversation(1024, true);
		}
		/* */
		for(int i = 0; i < iterations; i++){
			messageConversation(4, 256);
		}
		for(int i = 0; i < iterations; i++){
			messageConversation(2, 512);
		}
		for(int i = 0; i < iterations; i++){
			messageConversation(1, 1024);
		}
	}

}
