public class Main{
	public static void main(String[] args){
		String[] command = {"ssh", "129.3.218.83", "java -classpath Desktop/csc445a1/tcp/Client/ Main"};
		Thread t = new Thread(new Server(command));
		t.start();
		try{
			t.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
