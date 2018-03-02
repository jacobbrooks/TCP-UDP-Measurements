import java.net.*;
import java.io.*;

public class Server{
	private DatagramSocket socket;
	private static final int iterations = 200;

	public Server(){
		try{
			socket = new DatagramSocket(6145);
			socket.setSendBufferSize(8192);
			socket.setReceiveBufferSize(8192);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void byteServe(int messageSize){
		try{
			byte[] b = new byte[messageSize];
			DatagramPacket packet = new DatagramPacket(b, messageSize);
			socket.receive(packet);

			InetAddress address = packet.getAddress();
        	int port = packet.getPort();
			byte[] b2 = packet.getData();

       	 	packet = new DatagramPacket(b2, messageSize, address, port);
        	socket.send(packet);	
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void messageServe(int messageSize, int messageCount){
		try{
			for(int i = 0; i < messageCount; i++){
				byte[] b = new byte[messageSize * 1024];
				DatagramPacket packet = new DatagramPacket(b, messageSize * 1024);
				socket.setSoTimeout(3000);
				try{
					socket.receive(packet);
				}catch(SocketTimeoutException e){
					System.out.println("Server message receipt timed out!");
				}				
				InetAddress address = packet.getAddress();
        		int port = packet.getPort();
				b = packet.getData();
				
				byte[] ack = new byte[1];
				ack[0] = (byte) 5;
				packet = new DatagramPacket(ack, 1, address, port);
        		socket.send(packet);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loopServe(){
		/*for(int i = 0; i < iterations; i++){
			byteServe(1);
		}
		for(int i = 0; i < iterations; i++){
			byteServe(64);
		}
		for(int i = 0; i < iterations; i++){
			byteServe(1024);
		}*/
		 
		for(int i = 0; i < iterations; i++){
			messageServe(4, 256);
		}
		for(int i = 0; i < iterations; i++){
			messageServe(2, 512);
		}
		for(int i = 0; i < iterations; i++){
			messageServe(1, 1024);
		}
		socket.close();
	}

}
