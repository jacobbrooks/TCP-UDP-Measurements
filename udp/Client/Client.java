import java.net.*;
import java.io.*;

public class Client{
	
	private DatagramSocket socket;
	private static final int iterations = 200;
	private double byte1, byte64, byte1024;
	private double mega256x4k, mega512x2k, mega1024x1k;	

	public Client(){
		try{
			socket = new DatagramSocket();
			socket.setSendBufferSize(8192);
			socket.setReceiveBufferSize(8192);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private long sendBytes(int messageSize){
		long startTime = 0;
		long elapsedTime = 0;
		try{
			
			byte[] b = new byte[messageSize];
			for(int i = 0; i < b.length; i++){
				b[i] = (byte) i;
			}
			
			startTime = System.nanoTime();

			InetAddress address = InetAddress.getByName("altair.cs.oswego.edu");
			DatagramPacket packet = new DatagramPacket(b, messageSize, address, 6145);
			socket.send(packet);

			packet = new DatagramPacket(b, messageSize);
			socket.receive(packet);

			elapsedTime = System.nanoTime() - startTime;

			byte[] b1 = packet.getData();
				
		}catch(IOException e){
			e.printStackTrace();
		}
		return elapsedTime;
	}

	private long sendMessages(int messageSize, int messageCount){
		long startTime = 0;
		long elapsedTime = 0;
		try{
			byte[] b = new byte[messageSize * 1024];
			for(int i = 0; i < b.length; i++){
				b[i] = (byte) i;
			}
			
			InetAddress address = InetAddress.getByName("altair.cs.oswego.edu");
			byte[] ack = new byte[1];

			startTime = System.nanoTime();
			for(int i = 0; i < messageCount; i++){
				DatagramPacket packet = new DatagramPacket(b, messageSize * 1024, address, 6145);
				socket.send(packet);
				packet = new DatagramPacket(ack, 1);
				socket.setSoTimeout(3000);
				try{
					socket.receive(packet);
				}catch(SocketTimeoutException e){
					System.out.println("Client ack receipt timed out");
				}				
				ack = packet.getData();
			}
			elapsedTime = System.nanoTime() - startTime;

		}catch(Exception e){
			e.printStackTrace();
		}
		return elapsedTime;
	}

	private double takeMeasurements(int messageSize, boolean messages, int messageCount){
		long measurement = 0;
		double time = 0;
		for(int i = 0; i < iterations; i++){
			if(!messages){
				measurement += sendBytes(messageSize);
			}else{
				measurement += sendMessages(messageSize, messageCount);
			}			
			if(i > 0){
				time += (double) measurement;
			}
		}
		double average = (double) time / (double) (iterations - 1);
		return average;
	}

	public void calculateAverages(){
		/*byte1 = takeMeasurements(1, false, 0);
		byte64 = takeMeasurements(64, false, 0);
		byte1024 = takeMeasurements(1024, false, 0); */

		mega256x4k = takeMeasurements(4, true, 256);
		mega512x2k = takeMeasurements(2, true, 512);
		mega1024x1k = takeMeasurements(1, true, 1024);

		/*System.out.println("Byte 1: " + byte1);
		System.out.println("Byte 64: " + byte64);
		System.out.println("Byte 1024: " + byte1024);*/
		
		System.out.println("Mega256x4k: " + mega256x4k);
		System.out.println("Mega512x2k: " + mega512x2k);
		System.out.println("Mega1024x1k: " + mega1024x1k);

		socket.close();
	}
	
}
