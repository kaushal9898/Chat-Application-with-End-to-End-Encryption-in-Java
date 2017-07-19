

import java.io.Serializable;
import java.net.Socket;


public class Packet implements Serializable {
	private String msg = null;
	private int id;
	private String rname=null;
	private String sname=null;
	private String imgname=null;
	byte[] img=null;
	private String imgtype=null;
	boolean remove;
	boolean samename = false;
	Packet(byte[] img,String imgname,String imgtype,String rname,String sname){
		this.img = new byte[10000];
		this.img = img;
		this.imgname = imgname;
		this.imgtype=imgtype;
		this.rname=rname;
		this.sname=sname;
	}
	Packet(boolean samename){
		this.samename = samename;
	}
	Packet(int id,String name,boolean remove){
		this.id=id;
		this.rname=name;
		this.remove = remove;
	}
	
	public String getImgtype() {
		return imgtype;
	}
	
	Packet(String msg){
		this.msg = msg;
	}
	
	private static final long serialVersionUID = 1L;
	
	public String getMsg() {
		return msg;
	}
	
	public String getImgname() {
		return imgname;
	}
	
	public byte[] getImg() {
		return img;
	}
	
	public String getname(){
		return rname;
	}
	public int getid(){
		return id;
	}
	public String getsname(){
		return sname;
	}
	
}
