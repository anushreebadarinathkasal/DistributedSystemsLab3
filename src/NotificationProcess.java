//Name: Anushree Badarinath Kasal
//Student ID: 1001624311
//Net ID: abk4311
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Notification process which polls the server for every seven seconds.

//https://docs.oracle.com/javase/tutorial/rmi/index.html 
//https://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html

public class NotificationProcess {
	static JFrame frame;
	static JTextArea NotifyText;
	static JPanel panel;
	 

	/**
	 * Launch the application.
	 */
	// main method with void output type and string[] input type
	public static void main(String[] args) {
	// This function creates graphical window for notification process and handles termination of process.
		// TODO Auto-generated method stub

		/**
		 * Initialize the contents of the frame.
		 */
		try {
			MsgInterface mintf_np= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
			mintf_np.NotifyStart();
		} catch (MalformedURLException | RemoteException | NotBoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
//To install swing extension into eclipse
//https://www.youtube.com/watch?v=oeswfZz4IW0
//Usage of Swing components
//https://docs.oracle.com/javase/tutorial/uiswing/components/
					frame = new JFrame();
					frame.setBounds(500, 500, 500,500 );
					frame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent windowevent) {
							MsgInterface mintf_4;
							try {
								mintf_4 = (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
								mintf_4.notify_killed();
								System.exit(0);
							} catch (MalformedURLException | RemoteException | NotBoundException e) {
								// TODO Auto-generated catch block
								System.out.println("exception in notification kill");
								e.printStackTrace();
								
							}
							
						}
					});
					//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//frame.getContentPane().setLayout(null);
					
					panel = new JPanel();
					panel.setBounds(12, 13, 408, 38);
					frame.getContentPane().add(panel);
					
					JLabel NotifyLog = new JLabel("Notification Logs: ");
					panel.add(NotifyLog);
					
					NotifyText = new JTextArea(20,20);
					panel.add(NotifyText);
					JScrollPane jp1 = new JScrollPane(NotifyText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel.add(jp1);
					frame.setVisible(true);
					NotifyText.setText("Notification process started" + "\n");
		//EventQueue.invokeLater(new Runnable() {
			//public void run() {
				try {
					
					//polls every seven seconds for the results that are to be displayed.
					while(true) {
						StudentResultsNotification();
						Thread.sleep(7000);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
		//});
	//}
	
	//gets results from server in an array form and prints them in the graphic console.
	// no inputs, output type is void.
	public static void StudentResultsNotification() throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
	
		MsgInterface mintf2= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
		ArrayList<String> res1 = mintf2.NotifyRequest();
		
		if(res1.size() !=0) {
			for(int i=0;i< res1.size();i++){
				
				String scno = res1.get(i);
				NotifyText.append("Decision "+scno + "\n");
				System.out.println(scno);
			
			}	
		}else {
			NotifyText.append("No resullts to display"+ "\n");
			System.out.println("No resullts to display");
			
		}
		
		
		
	}
	
}

