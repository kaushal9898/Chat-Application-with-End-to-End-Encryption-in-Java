


import java.net.InetAddress;
import java.net.Socket;

public class ClientList {
	
	public String name;
	public int port;
	public InetAddress add;
	private final int id;
	public int attemp=0;
	public Socket socket;
	public ClientList(String name,int port,InetAddress add,final int id,Socket socket){
		this.name=name;
		this.port=port;
		this.add=add;
		this.id=id;
		this.socket=socket;
	}
	public int getid(){
		return id;
	}
	public Socket getsocket(){
		return socket;
	}
}

