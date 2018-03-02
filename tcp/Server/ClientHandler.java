import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable{

	private ServerSocket serverSocket;
	private Socket client;
	private DataInputStream input;
	private DataOutputStream output;
	private static final int iterations = 200;

	double byte1;
	double byte64;
	double byte1024;
	double kilo1;
	double kilo16;
	double kilo64;
	double kilo256;
	double kilo1024;
	double mega256x4k;
	double mega512x2k;
	double mega1024x1k;
	
	public ClientHandler(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
		initStreamsAndSockets();
	}

	private void initStreamsAndSockets(){
		try{		
			client = serverSocket.accept();
			output = new DataOutputStream(client.getOutputStream());
			input = new DataInputStream(client.getInputStream());	
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private long byteConversation(int messageSize, boolean kilo){
		int coefficient = 1;
		long startTime = -1;
		long elapsedTime = -1;
		if(kilo){
			coefficient = 1024;
		}	
		try{
			byte[] b = new byte[coefficient * messageSize];			
			for(int i = 0; i < b.length; i++){
				int j = i + 1;
				b[i] = (byte) j;
			}
			startTime = System.nanoTime();
			output.write(b, 0, coefficient * messageSize);
			byte[] b2 = new byte[coefficient * messageSize];
			input.readFully(b2, 0, coefficient * messageSize);
			elapsedTime = System.nanoTime() - startTime;
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		return elapsedTime;
	}

	private long messageConversation(int messageCount, int messageSize){
		long startTime = 0;
		long elapsedTime = 0;		
		try{		
			byte[] b = new byte[messageSize * 1024];
			for(int i = 0; i < b.length; i++){
				b[i] = (byte)i;
			}
			startTime = System.nanoTime();
			for(int i = 0; i < messageCount; i++){
				output.write(b, 0, messageSize * 1024);
				output.flush();
				byte ack = input.readByte();
			}
			elapsedTime = System.nanoTime() - startTime;
		}catch(IOException e){
			e.printStackTrace();
		}
		return elapsedTime;
	}
		
	private double takeMeasurements(int size, boolean kilo, boolean messages, int messageCount){
		long time = 0;
		for(int i = 0; i < iterations; i++){
			long measurement = 0;
			if(!messages){
				measurement = byteConversation(size, kilo);
			}else{
				measurement = messageConversation(messageCount, size);
			}
			if(i > 0){
				time += measurement;
			}
		}	
		double average = (double) time / (double) (iterations - 1);
		return average;
	}

	private void calculateAverages(){

		byte1 = takeMeasurements(1, false, false, 0);
		byte64 = takeMeasurements(64, false, false, 0);
		byte1024 = takeMeasurements(1024, false, false, 0);
		
		kilo1 = takeMeasurements(1, true, false, 0);
		kilo16 = takeMeasurements(16, true, false, 0);
		kilo64 = takeMeasurements(64, true, false, 0);
		kilo256 = takeMeasurements(256, true, false, 0);
		kilo1024 = takeMeasurements(1024, true, false, 0);

		mega256x4k = takeMeasurements(4, false, true, 256);
		mega512x2k = takeMeasurements(2, false, true, 512);
		mega1024x1k = takeMeasurements(1, false, true, 1024);

		System.out.println("Byte1: " + byte1);
		System.out.println("Byte64: " + byte64);
		System.out.println("Byte1024: " + byte1024);
		System.out.println("========================");
		System.out.println("Kilo1: " + kilo1);
		System.out.println("Kilo16: " + kilo16);
		System.out.println("Kilo64: " + kilo64);
		System.out.println("Kilo256: " + kilo256);
		System.out.println("Kilo1024: " + kilo1024);
		System.out.println("========================");
		System.out.println("Mega256x4k: " + mega256x4k);
		System.out.println("Mega512x2k: " + mega512x2k);
		System.out.println("Mega1024x1k: " + mega1024x1k);
	}
	
	public void run(){
		calculateAverages();
	}

}
