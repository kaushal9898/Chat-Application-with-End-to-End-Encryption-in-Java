

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JList;
import java.awt.Color;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField txtMessage;
	private static  String name="";
	private String ip;
	private int port;
	private JTextPane textArea; 
	private Thread recieve;
	public List<OnlineUsers> users=new ArrayList<OnlineUsers>();
	private JPanel panel;
	private JList<String> online_users;
	public DefaultListModel<String> model;
	private Socket socket = null;
	String msg = null;
	String imgname = "";
	String imgtype="";
	BufferedImage buffimg;
	ObjectInputStream is=null;
	int id;
	boolean f = true;
	public Client(String name,String ip,int port) throws Exception {
		setTitle(name);
		this.name=name;
		this.ip=ip;
		this.port=port;
		
		boolean c = connection();
		if(!c){
			consol("Connection failed");
			System.out.println("Connection failed");
		}
		
		Create();
		recieveThread();
	}
	
	private boolean connection(){
		try {
			socket = new Socket(ip, port);
			id=UniqueID.getid();
			System.out.println(name+" Connected");
			
			
			ObjectOutputStream ous=new ObjectOutputStream(socket.getOutputStream());
			Packet pckt=new Packet(id,name,false);
			ous.writeObject(pckt);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public static String encryptMsg(String msg) throws Exception{
		
		String key = "Ba212345Ba212345";
        SecretKeySpec key_AES = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key_AES);
        byte[] encrypted = cipher.doFinal(msg.getBytes());

        StringBuilder strb = new StringBuilder();
        for (byte b: encrypted) {
            strb.append((char)b);
        }
        String emsg = strb.toString();
	   
		return emsg;
	}
	public static String decryptMsg(String msg) throws Exception {
		String key = "Ba212345Ba212345"; 
        SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cphr = Cipher.getInstance("AES");
		byte[] bb = new byte[msg.length()];
        for (int i=0; i<msg.length(); i++) {
            bb[i] = (byte) msg.charAt(i);
        }
        cphr.init(Cipher.DECRYPT_MODE, aesKey);
        String dmsg = new String(cphr.doFinal(bb));
	    return dmsg;
	}
	
	private void recieveThread(){
		
		recieve=new Thread("Recieve"){
			public void run(){
				
				while(true){
					
					
					is=null;
					try {
						is = new ObjectInputStream(socket.getInputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Packet obj = null;
				    try {
				    	obj = (Packet) is.readObject();
					} catch ( Exception e) {
						
					}
				    if(obj.samename){
				    	dispose();
				    	Login frame = new Login();
						frame.setVisible(true);
				    }
				    //new user..
				    if(obj.getImg()==null && obj.getMsg()==null && obj.getid()!=-1 && obj.getname()!=null && obj.remove == false){
				    	users.add(new OnlineUsers(obj.getname(),obj.getid()));  
				    	model.addElement(obj.getname());						
				    }
				    //remove user
				    if(obj.getImg()==null && obj.getMsg()==null && obj.getid()!=-1 && obj.getname()!=null && obj.remove == true){
				    	for(int i=0;i<users.size();i++){
				    		if(users.get(i).getId() == obj.getid()){
				    			users.remove(i);
				    		}	
						}
				    	for(int i=0;i<model.size();i++){
				    		if(model.get(i).equals(obj.getname())){	
				    			model.remove(i);
				    		}
				    	}
				    }
				    //recieve message	
				    
					if(obj.getMsg()!=null){
						
						String rec = "";
						String rmsg = obj.getMsg();
						int i=0;
						for(i=0;rmsg.charAt(i)!=':';i++){
							rec+=rmsg.charAt(i);
						}
						rmsg = rmsg.substring(i+2);
						String dmsg = null;
						try {
							dmsg = decryptMsg(rmsg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	consol1(rec+": "+dmsg);
					}
					
					//recieve image
					else if(obj.getImg()!=null){
				    	byte[] img = obj.getImg();
				    	InputStream in = new ByteArrayInputStream(img);
				    	try {
							buffimg = ImageIO.read(in);
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
				    	System.out.println(obj.getImgtype());
				    	try {
							ImageIO.write(buffimg,obj.getImgtype(), new File(
									"src/"+obj.getImgname()));
						} catch (IOException e) {
							
							e.printStackTrace();
						}
				    	BufferedImage newimg =Client.scaleImage(buffimg,  BufferedImage.TYPE_INT_RGB, 450, 350);
				    	consol1("Image recieved from "+ obj.getsname());
				    	textArea.insertIcon(new ImageIcon(newimg));
				    }
					
				}
			   
			}
		};
		recieve.start();
	}
	
	private void Create() throws Exception{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	    
	  
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(683,500);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		panel = new JPanel();
		panel.setBounds(496, 54, 169, 366);
		contentPane.add(panel);
		panel.setLayout(null);
		model = new DefaultListModel<>();
		online_users = new JList<String>();
		online_users.setForeground(Color.BLACK);
		online_users.setFont(new Font("Verdana", Font.PLAIN, 14));
		online_users.setBounds(0, 0, 169, 366);
		panel.add(online_users);
		online_users.setModel(model);
		txtMessage = new JTextField();
		txtMessage.setBounds(10, 431, 308, 29);
				
		txtMessage.addKeyListener(new KeyAdapter() {
			//sending the message
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(online_users!=null && online_users.getSelectedValue()!=null){						
						msg = txtMessage.getText();
						String emsg = null;
						
						try {
							emsg = encryptMsg(msg);
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						if(!msg.equals("")){
							ObjectOutputStream out=null;
							try {
								out = new ObjectOutputStream(socket.getOutputStream());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Packet obj = new Packet(name+" : "+emsg+":"+online_users.getSelectedValue());
							try {
								out.writeObject (obj);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
							consol(msg);
							txtMessage.setText("");
						}
					}
				}
			}
			
		});
		contentPane.setLayout(null);
		contentPane.add(txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBounds(328, 434, 61, 23);
		
		//sending the message
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(online_users!=null && online_users.getSelectedValue()!=null){						
					msg = txtMessage.getText();
					String emsg = null;
					
					try {
						emsg = encryptMsg(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					if(!msg.equals("")){
					ObjectOutputStream out=null;
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Packet obj = new Packet(name+" : "+emsg+":"+online_users.getSelectedValue());
					try {
						out.writeObject (obj);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					txtMessage.setText("");					
					consol(msg);
				}
			}
			}
		});
		contentPane.add(btnNewButton);
		textArea = new JTextPane();
		textArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.BLACK);
		textArea.setEditable(false);
		
		JScrollPane scroll=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(10, 11, 474, 409);
		
		contentPane.add(scroll);
		
		//sending image
		JButton send_file = new JButton("Image");
		send_file.setBounds(394, 434, 90, 23);
		send_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!online_users.getSelectedValue().equals("")){
				JFileChooser f=new JFileChooser();
				f.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result=f.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File s=f.getSelectedFile();
					String path=s.getAbsolutePath();
					
					for(int i=s.getName().length()-1;s.getName().charAt(i)!='.';i--){
						imgtype = s.getName().charAt(i)+imgtype;
					}
					System.out.println(imgtype); 
					try {
						buffimg =ImageIO.read(new File(path));
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					try {
						ImageIO.write(buffimg, "jpg", bout );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						bout.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] img = bout.toByteArray();
					
					try {
						bout.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ObjectOutputStream out=null;
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					imgname = s.getName();
					
					Packet obj = new Packet(img,imgname,imgtype,online_users.getSelectedValue(),name);
					try {
						out.writeObject (obj);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					BufferedImage newimg = Client.scaleImage(buffimg, BufferedImage.TYPE_INT_RGB, 450, 350);
					textArea.insertIcon(new ImageIcon(newimg));
				}
			}
			}
		});
		contentPane.add(send_file);
		
		//closing the window
		addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent e){
			try {	
					
				
					ObjectOutputStream out=null;
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Packet obj = new Packet("EXIT");
					try {
						out.writeObject (obj);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					recieve.stop();
					socket.close();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
			}
				
			}
		});
		
		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setBounds(530, 4, 104, 37);
		lblOnlineUsers.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(lblOnlineUsers);
		txtMessage.requestFocusInWindow();
	}
	
	public static String getname() {
		return name;		
	}
	
	//printing the message
	public void consol(String massege){
		textArea.setText(textArea.getText()+name+" : "+massege+"\n");
	}
	public void consol1(String massege){
		textArea.setText(textArea.getText()+massege+"\n");
	}
	
	public static BufferedImage scaleImage(BufferedImage image, int imageType,
            int newWidth, int newHeight) {
			// Make sure the aspect ratio is maintained, so the image is not distorted
			double thumbRatio = (double) newWidth / (double) newHeight;
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			double aspectRatio = (double) imageWidth / (double) imageHeight;

			if (thumbRatio < aspectRatio) {
				newHeight = (int) (newWidth / aspectRatio);
			} else {
				newWidth = (int) (newHeight * aspectRatio);
			}

			// Draw the scaled image
			BufferedImage newImage = new BufferedImage(newWidth, newHeight,imageType);
			Graphics2D graphics2D = newImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

			return newImage;
}
}
