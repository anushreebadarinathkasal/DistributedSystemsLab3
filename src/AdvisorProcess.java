//Name: Anushree Badarinath Kasal
//Student ID: 1001624311
//Net ID: abk4311
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Random;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Advisor process that checks for requests every three seconds,process the requests if they are received and pushes
//the results.

//https://docs.oracle.com/javase/tutorial/rmi/index.html 
//https://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html
public class AdvisorProcess {
	
	static JFrame frame;
	static JTextArea AdvisorText;
	static JPanel panel;
	

	/**
	 * Launch the application.
	 */
//Start of advisor process,creates a graphical window and handles any unexpected termination.
	public static void main(String[] args) {

		// TODO Auto-generated method stub

		
		/**
		 * Initialize the contents of the frame.
		 */
		try {
			MsgInterface mintf_ap= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
			mintf_ap.AdvisorStart();
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
							MsgInterface mintf_3;
							try {
								mintf_3 = (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
								mintf_3.advisor_killed();
								System.exit(0);
							} catch (MalformedURLException | RemoteException | NotBoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//frame.getContentPane().setLayout(null);
					
					panel = new JPanel();
					panel.setBounds(12, 13, 408, 38);
					frame.getContentPane().add(panel);
					
					JLabel AdvisorLog = new JLabel("Advisor Logs: ");
					panel.add(AdvisorLog);
					
					AdvisorText = new JTextArea(20,20);
					panel.add(AdvisorText);
					JScrollPane jp1 = new JScrollPane(AdvisorText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel.add(jp1);
					frame.setVisible(true);
					AdvisorText.setText("advisor process activated"+"\n");
					
//Sleep for three seconds and pull requests.
				try {
					while(true) {
						AdvisorPull();
						Thread.sleep(3000);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	
//Advisor process pulls requests then approves/disapproves them based on two randomly generated numbers.
	// No input and output is void
	public static void AdvisorPull() throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		MsgInterface mintf1= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
		//Requesting server for student requests in form of array list.
		ArrayList<String> req1 = mintf1.AdvisorRequest();
		ArrayList<String> result = new ArrayList<String>();
		if(req1.size()==0) {
			AdvisorText.append("No request present"+ "\n");
			System.out.println("No request");
			
		}else {
			for(int i=0;i< req1.size();i++){				
				int num = nextrandomnumber();
				int probablity_r = nextprobaliblitynumber();
			   //decision making for approval disapproval based on random numbers.
				if(num >= probablity_r) {
					String scno = req1.get(i);
					result.add(scno + ":APPROVED");
					System.out.println(scno + ":APPROVED");
					AdvisorText.append("Request processed as "+scno + ":APPROVED"+ "\n");
				}else {
					String scno = req1.get(i);
					result.add(scno + ":DISAPPROVED");
					System.out.println(scno + ":DISAPPROVED");
					AdvisorText.append("Request processed as "+scno + ":DISAPPROVED" + "\n");
				}
			}
		//method call to push all the decisions.	
		AdvisorResultPush(result);
			
		}
		
		
	}
	//https://dzone.com/articles/random-number-generation-in-java 
	//function to generate random number upper range 80.
	// no input output is integer type
	public static int nextprobaliblitynumber() {
		// TODO Auto-generated method stub
		Random rand1 =  new Random();
		return rand1.nextInt(80);
	}

	//https://dzone.com/articles/random-number-generation-in-java 
	//function to generate random number upper range 100.
	// no input output is integer type
	public static int nextrandomnumber() {
		// TODO Auto-generated method stub
		Random rand =  new Random();
		return rand.nextInt(100);
	}


//function to communicate all the advisor decisions.
//input is an array list of type string and return method is void.
	public static void AdvisorResultPush(ArrayList<String> adv_res)throws MalformedURLException, RemoteException, NotBoundException {
		MsgInterface mintf_ap= (MsgInterface) Naming.lookup("rmi://localhost:8818/mint");
		if(adv_res.size()!=0) {
			String advres1 = mintf_ap.AdvisorResult(adv_res);
			if(advres1.equals("SUCCESS")) {
				AdvisorText.append("All the decisions are pushed"+"\n");
				
			}
		}
	}

}


