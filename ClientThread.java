


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ClientThread extends Thread {
	public static List<ClientList> clients=new ArrayList<ClientList>();
	
	protected Socket socket;
	BufferedImage buffimg;
	public ClientThread(Socket socket){
		this.socket=socket;
	}
	public void run(){
		for(int i=0;i<clients.size();i++){
			System.out.println(clients.get(i).name);
		}
		while(true){
		ObjectInputStream is=null;
		try {
			is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		Packet obj = null;
	    try {
	    	obj = (Packet) is.readObject();
		} catch ( Exception e) {
		
		}
	    try{
	    if(obj.getMsg()!=null){
	    	
	    	if(obj.getMsg().equals("EXIT")){
	    		is.close();
	    		int k=0;
	    		
	    		for(int i=0;clients.get(i).socket!=socket;i++){
	    			k++;
	    		}
	    		String name = clients.get(k).name;
	    		int id = clients.get(k).getid();
	    		System.out.println(clients.get(k).name+" is disconnected....");
	    		clients.remove(k);
	    		for(int j=0;j<clients.size();j++){
		    		Socket skt = clients.get(j).socket;
			    		ObjectOutputStream out=null;
						try {
							out = new ObjectOutputStream(skt.getOutputStream());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Packet obj1 = new Packet(id,name,true);
						try {
							out.writeObject (obj1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	}
	    		Thread.currentThread().stop();;
	    		socket.close();
	    	}
	    	else{
	    	System.out.println(obj.getMsg());
	    	String reciever="";
	    	for(int i=obj.getMsg().length()-1;obj.getMsg().charAt(i)!=':';i--){
	    		reciever=obj.getMsg().charAt(i)+reciever;
	    	}
	    	
	    	Socket socket1=null;
	    	int k=0;
	    	for(int i=0;!clients.get(i).name.equals(reciever);i++){
	    		k++;
	    	}
	    	System.out.println(clients.get(k).name);
	    	socket1=clients.get(k).getsocket();
	    	
	    	ObjectOutputStream out=null;
			try {
				out = new ObjectOutputStream(socket1.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int p=0;
			String newmsg="";
			for(int i=0;p!=2;i++){
				newmsg+=obj.getMsg().charAt(i);
				if(obj.getMsg().charAt(i)==':'){
					p++;
				}
			}
			
			newmsg=newmsg.substring(0,newmsg.length()-1);
			System.out.println(newmsg);
			Packet obj1 = new Packet(newmsg);
			try {
				out.writeObject (obj1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    			
	    	}
	    }
	    if(obj.getImg()==null && obj.getMsg()==null && obj.getid()!=-1 && obj.getname()!=null && !socket.isClosed()){
	    	int f=1;
	    	for(int i=0;i<clients.size();i++){
	    		if(obj.getname().equals(clients.get(i).name)){
	    			ObjectOutputStream out=null;
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Packet obj1 = new Packet(true);
					try {
						out.writeObject (obj1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					f=0;
					break;
	    		}
	    	}
	    	if(f==1){
		    	clients.add(new ClientList(obj.getname(),socket.getPort(),socket.getInetAddress(),obj.getid(),socket));
		    	System.out.println(obj.getname()+" is connected....");
		    	System.out.println(clients.size());
		    	for(int j=0;j<clients.size()-1;j++){
		    		ObjectOutputStream out=null;
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Packet obj1 = new Packet(clients.get(j).getid(),clients.get(j).name,false);
					try {
						out.writeObject (obj1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    	for(int j=0;j<clients.size()-1;j++){
		    		Socket skt = clients.get(j).socket;
			    		ObjectOutputStream out=null;
						try {
							out = new ObjectOutputStream(skt.getOutputStream());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Packet obj1 = new Packet(obj.getid(),obj.getname(),false);
						try {
							out.writeObject (obj1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	}
	    	}
	    }
	    if(obj.getImg()!=null){
	    	System.out.println("Image is tranfered");
	 
	    	byte[] img = obj.getImg();
	    	InputStream in = new ByteArrayInputStream(img);
	    	String name=obj.getname();
	    	try {
				buffimg = ImageIO.read(in);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	  
	    	
	    	Socket socket1=null;
	    	int k=0;
	    	for(int i=0;!clients.get(i).name.equals(name);i++){
	    		k++;
	    	}
	    	System.out.println(clients.get(k).name);
	    	socket1=clients.get(k).getsocket();
	    	
	    	ObjectOutputStream out=null;
			try {
				out = new ObjectOutputStream(socket1.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Packet obj1=new Packet(img,obj.getImgname(),obj.getImgtype(),obj.getname(),obj.getsname());
			
			try {
				out.writeObject (obj1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	    }
	    catch(Exception e){
	    	
	    }
	    	
		}
	}
}
