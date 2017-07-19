import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtIp;
	private String name,ip;
	private final int port=8002;
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the frame.
	 */
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(48, 66, 197, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 11));
		lblNewLabel.setBounds(124, 41, 46, 14);
		contentPane.add(lblNewLabel);
		
		txtIp = new JTextField();
		txtIp.setBounds(48, 174, 197, 20);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setFont(new Font("Times New Roman", Font.BOLD, 11));
		lblIpAddress.setBounds(102, 134, 74, 14);
		contentPane.add(lblIpAddress);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 name = txtName.getText();
				 ip=txtIp.getText();
				 
				
				login();
				
			}
		});
		btnLogin.setBounds(102, 248, 89, 23);
		contentPane.add(btnLogin);
	}
	public void login(){
		dispose();
		try {
			new Client(name,ip,port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
