//Name: Anushree Badarinath Kasal
//Student ID: 1001624311
//Net ID: abk4311
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

//Student process is used by student to send his requests for course that he/she is interested.


//https://docs.oracle.com/javase/tutorial/rmi/index.html 
//https://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html

public class StudentProcess {
//To install swing extension into eclipse
//https://www.youtube.com/watch?v=oeswfZz4IW0
//Usage of Swing components
//https://docs.oracle.com/javase/tutorial/uiswing/components/
	private JFrame frame;


	/**
	 * Launch the application.
	 */
	//Initialize the student process and set up the student console.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentProcess window = new StudentProcess();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StudentProcess() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	//Initialize the frame contents 
	private void initialize() {
		
		try {
			MsgInterface mintf_sp= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
			mintf_sp.StudentStart();
		} catch (MalformedURLException | RemoteException | NotBoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		frame = new JFrame();
		frame.setBounds(500, 100, 800,800 );
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				MsgInterface mintf_sk;
				try {
					mintf_sk = (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
					mintf_sk.student_killed();
					System.exit(0);
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 13, 408, 38);
		
		frame.getContentPane().add(panel);
		
		JLabel lblStudentName = new JLabel("Student Name : ");
		panel.add(lblStudentName);
		
		JTextField StudentName = new JTextField();
		panel.add(StudentName);
		StudentName.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 52, 408, 41);
		frame.getContentPane().add(panel_1);
		
		JLabel lblCourse = new JLabel("Course :");
		panel_1.add(lblCourse);
		
		JTextField Courseno = new JTextField();
		panel_1.add(Courseno);
		Courseno.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(12, 101, 408, 99);
		frame.getContentPane().add(panel_2);
		
		JTextArea StudentLog = new JTextArea(5,30);
		panel_2.add(StudentLog);
		JScrollPane jp1 = new JScrollPane(StudentLog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel_2.add(jp1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(12, 52, 100, 100);
		frame.getContentPane().add(panel_3);
		
		//handing student process termination 
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MsgInterface mintf_2= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
					mintf_2.student_killed();
					System.exit(0);
				}catch(Exception e2) {
					System.out.println("exception here in catch");
					System.out.println(e2.getMessage());
				}
				
			}
		});
		panel_3.add(btnExit);
		
		
		//send student name and course number as a request.
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				MsgInterface mintf= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
				mintf.add_student();
				String Student_Name = StudentName.getText();
				String Course_no = Courseno.getText();
				if(!Student_Name.isEmpty() && !Course_no.isEmpty()) {
					String success;
					//Student request method takes two string elements as input.
					success = mintf.StudentRequest(Student_Name, Course_no);
					if(success.equals("SUCCESS")) {
					StudentLog.append("Request Sent From:" + Student_Name + ":"+ Course_no+ "successfully" + "\n");
					} else {
					StudentLog.append("Request failed"+"\n");	
					}
						
				}else {
					StudentLog.append("Invalid Request" + "\n");
			
				}
				}catch(Exception e1) {
					System.out.println(e1.getMessage());
				}
				
			}
		});
		panel_3.add(btnSend);
	}
}
