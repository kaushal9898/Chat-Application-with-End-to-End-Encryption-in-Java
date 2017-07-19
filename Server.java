import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
	
	private int port;	
	private boolean running=false;
	private Thread run,clientThread;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	BufferedImage bImageFromConvert=null;
	String msg = null;
	String name;

	
	public static void main(String[] args) throws IOException{
		new Server();
	}
	public Server() throws IOException {
		port=8002;
		serverSocket = new ServerSocket(port);
		System.out.println("Server stared successfully at port no "+port);
		run=new Thread(this,"Server");
		run.start();	
	}
	
	public void run(){
		running=true;
		getclient();
	}
	
	private void getclient() {
		clientThread=new Thread("clientThread"){
			public void run(){
				 while (true) {
			            try {
			                socket = serverSocket.accept();
			                
			            } catch (IOException e) {
			                System.out.println("I/O error: " + e);
			            }
			            
			            // new thread for a client
			          
			            new ClientThread(socket).start();
			        }
			}
		};clientThread.start();
	}
}


