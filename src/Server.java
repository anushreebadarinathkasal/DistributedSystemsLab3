//Name: Anushree Badarinath Kasal
//Student ID: 1001624311
//Net ID: abk4311

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


//Server class is implementing message interface where the request from the students,decisions from advisor
//is stored and results are notified.

//https://docs.oracle.com/javase/tutorial/rmi/index.html 
//https://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html
//https://www.geeksforgeeks.org/interfaces-in-java/
//https://github.com/dsapru/advising-simulation-rpc-mq
//https://beginnersbook.com/2013/12/how-to-loop-arraylist-in-java/

public class Server extends UnicastRemoteObject implements MsgInterface{

	static JTextArea ServerText;
	static JTextArea ActiveText;
	static JLabel ServerLog;
	static JLabel ActiveLog;
	static JPanel panel;
	static JFrame frame;

//Main memory structure to store all the student requests and advisor decisions.
	static ArrayList<String> storage_mem = new ArrayList<String>();
	static CopyOnWriteArrayList<String> active_list = new CopyOnWriteArrayList<String>();
	
	
	public Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) {
		try {
			//https://docs.oracle.com/javase/7/docs/api/java/rmi/registry/LocateRegistry.html 
			Registry registry = LocateRegistry.createRegistry(8818);
			Server ser = new Server();
			ser.graphic();
			registry.rebind("mint", ser);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

//Server window is created once server process is started then  requests from student or results from 
//advisor are added to storage memory.
	public static void graphic() throws IOException {
		// TODO Auto-generated method stub 
//To install swing extension into eclipse
//https://www.youtube.com/watch?v=oeswfZz4IW0
//Usage of Swing components
//https://docs.oracle.com/javase/tutorial/uiswing/components/
		frame = new JFrame();
		frame.setBounds(500, 500, 750,750 );

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(12, 13, 408, 38);
		frame.getContentPane().add(panel);
		
		ActiveLog = new JLabel("Active List: ");
		panel.add(ActiveLog);
		
		ActiveText = new JTextArea(20,20);
		panel.add(ActiveText);
		JScrollPane jp2 = new JScrollPane(ActiveText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(jp2);
		
		ServerLog = new JLabel("Logs: ");
		panel.add(ServerLog);
		
		ServerText = new JTextArea(20,20);
		panel.add(ServerText);
		JScrollPane jp1 = new JScrollPane(ServerText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(jp1);
		frame.setVisible(true);
		ServerText.setText("Server started" + "\n");
//Once server is started if their were pending requests add to storage memory.
		File sreq_1 = new File("fromstudent.txt");
		String line_1="";
		try {
		
			FileReader fr_1 = new FileReader(sreq_1);
			if(sreq_1.exists()) {
				BufferedReader br_1 = new BufferedReader(fr_1);
				while((line_1=br_1.readLine())!=null) {
					storage_mem.add(line_1);
					}
				fr_1.close();
				br_1.close();
				
			}else {
				System.out.println("********************");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("File is not present");
		}
		File sreq_2 = new File("fromadvisor.txt");
		String line_2="";
		try {
		
			FileReader fr_2 = new FileReader(sreq_2);
			if(sreq_2.exists()) {
				BufferedReader br_2 = new BufferedReader(fr_2);
				while((line_2=br_2.readLine())!=null) {
					storage_mem.add(line_2);
					}
				fr_2.close();
				br_2.close();
				
			}else {
				System.out.println("********************");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("File is not present");
		}
	}

//Any request from student process is stored in the storage memory and also creates from student.txt file and stores in it.
//Student request input is two string elements.
//output is a string.
	@Override
	public String StudentRequest(String sname, String cno) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			if(sname.isEmpty() && cno.isEmpty()) {
				return "fail";
			}else {
				FileWriter sdata = new FileWriter("fromstudent.txt",true);
				BufferedWriter bw = new BufferedWriter(sdata);
				bw.write(sname+ ":" + cno);
				storage_mem.add(sname+ ":" + cno);
				bw.newLine();
				bw.close();
				ServerText.append("Request received from:"+sname+ ":"+cno+"\n");
				return "SUCCESS";
			}
		}catch(Exception e1) {
				System.out.println(e1.getMessage());
				return "fail";
		}
		
	}
//This method is called when an advisor is requesting to see if there are any student request pending.
//It reads from the fromstudent.txt file and returns an array list of pending requests.
//It also deletes students requests from storage memory.
//output of this function is array list.
	
	@Override
	public ArrayList AdvisorRequest() throws RemoteException {
		ArrayList<String> req = new ArrayList<String>();
		String line="";
		// TODO Auto-generated method stub
		try {
			File sreq = new File("fromstudent.txt");
			FileReader fr = new FileReader(sreq);
			if(!sreq.exists()) {
				return req;
			}else {
				BufferedReader br = new BufferedReader(fr);
				while((line=br.readLine())!=null) {
					req.add(line);
					}
					fr.close();
					sreq.delete();
					int j = 0;
					for(String sdelete : storage_mem ) {
						//In a for loop get elements from storage memory and if the last element of the string 
						//is not APPROVED or DISAPPROVED then delete requests from storage memory.
						String[] s_ele = sdelete.split(":");
						String last_stringelement =s_ele[s_ele.length-1];
						if( ! ((last_stringelement.equals("APPROVED")) || (last_stringelement.equals("DISAPPROVED"))) ){
							storage_mem.remove(j);
							}
						j++;
					}
					return req;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return req;
		}
	
	}

//This method is used by notification process to get advisors decision.
//This function returns an array list of advisors decisions, and also deletes from 
//the storage memory of all decision elements that are made.
//output is an array list of type string.
	@Override
	public ArrayList NotifyRequest() throws RemoteException {
		ArrayList<String> res = new ArrayList<String>();
		String line1="";
		// TODO Auto-generated method stub
		try {
			File sres = new File("fromadvisor.txt");
			FileReader fr1 = new FileReader(sres);
			if(!sres.exists()) {
				return res;
			}else {
				BufferedReader br1 = new BufferedReader(fr1);
				while((line1=br1.readLine())!=null) {
					res.add(line1);
					}
					fr1.close();
					sres.delete();
					int j = 0;
					for(String sdelete : storage_mem ) {
						//In a for loop get elements from storage memory and if the last element of the string 
						//is APPROVED or DISAPPROVED then delete from storage memory as requests are processed.
						
						String[] s_ele = sdelete.split(":");
						String last_stringelement =s_ele[s_ele.length-1];
						if(  (last_stringelement.equals("APPROVED")) || (last_stringelement.equals("DISAPPROVED")) ){
							storage_mem.remove(j);
							}
						j++;
					}
					return res;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return res;
		}
	
	}
	
//This method is used to store the decisions processed by advisor and added to storage memory.
//output is string.
	@Override
	public String AdvisorResult(ArrayList<String> res) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			
			FileWriter adv = new FileWriter("fromadvisor.txt",true);
			BufferedWriter advbw = new BufferedWriter(adv);
			if(res.size()==0) { //if the array returned from advisor is empty return failure.
				return "fail";
			}else {
				for(int i=0;i<res.size();i++) { 
					//get the result from res array and add it to storage memory in a for loop.
					String advno = res.get(i);
					advbw.write(advno);
					advbw.newLine();
					storage_mem.add(advno);
					
				}
				advbw.close();
				return "SUCCESS";
			}
			
		}catch(Exception e){
			return "fail";
			
		}
	}

//Method to print in real time if a student process is killed.
// no input output is void
	@Override
	public void student_killed() {
		// TODO Auto-generated method stub
		int i = 0;
		String found_stu = "FALSE";
		int entry =0;
		for(String sdelete_list : active_list ) {
			String[] s_ele1 = sdelete_list.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Student")){
				found_stu = "TRUE";
				entry = i;
				break;
			}
			i++;
			
		}
		if(found_stu.equals("TRUE")) {
			active_list.remove(entry);
		}
		int k = 0;
		ActiveText.append("New active list:"+"\n");
		for(String sactive : active_list ) {
			ActiveText.append(sactive +"\n");			
				k++;			
		}
		ServerText.append("Student Process Killed"+"\n");
	}

//Method to print in real time if a advisor process is killed.
// no input output is void
	@Override
	public void advisor_killed() throws RemoteException {
		// TODO Auto-generated method stub
		int i = 0;
		String found_adv = "FALSE";
		int entry =0;
		for(String sdelete_list : active_list ) {
			String[] s_ele1 = sdelete_list.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Advisor")){
				found_adv = "TRUE";
				entry = i;
				break;
			}
			i++;
			
		}
		if(found_adv.equals("TRUE")) {
			active_list.remove(entry);
		}
			
		int k = 0;
		ActiveText.append("New active list:"+"\n");
		for(String sactive : active_list ) {
			
			ActiveText.append(sactive+"\n");			
				k++;			
		}
		ServerText.append("Advisor Process Killed"+"\n");
		
	}

//Method to print in real time if a notification process is killed.
// no input output is void
	@Override
	public void notify_killed() throws RemoteException {
		// TODO Auto-generated method stub
		int i = 0;
		String found_not = "FALSE";
		int entry =0;
		for(String sdelete_list : active_list ) {
			String[] s_ele1 = sdelete_list.split(" ");
			//ActiveText.append("sele is  "+s_ele1[0] + i + "\n");
			if(s_ele1[0].equalsIgnoreCase("Notify")){
				found_not = "TRUE";
				entry = i;
				break;
			}
			i++;
			
		}
		if(found_not.equals("TRUE")) {
			active_list.remove(entry);
		}
		int k = 0;
		int size=active_list.size();
		//ActiveText.append("size is "+size + "\n");
		ActiveText.append("New active list:"+"\n");
		for(String sactive : active_list ) {
			ActiveText.append(sactive+"\n");			
				k++;			
		}
		ServerText.append("Notification Process Killed"+"\n");
	}
//Method to print in real time if a student process is started.
	@Override
	public void StudentStart() throws RemoteException {
		// TODO Auto-generated method stub
		active_list.add("Student process active");
		int i = 0;
		for(String sactive : active_list ) {
			String[] s_ele1 = sactive.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Student")) {
			ActiveText.append(sactive + "\n");	}		
			i++;			
		}
		
		ServerText.append("Student Process Started"+"\n");
		
	}
//Method to print in real time if a advisor process is started.
	@Override
	public void AdvisorStart() throws RemoteException {
		// TODO Auto-generated method stub
		active_list.add("Advisor process active");
		int i = 0;
		for(String sactive : active_list ) {
			String[] s_ele1 = sactive.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Advisor")) {
			ActiveText.append(sactive + "\n");	}		
			i++;			
		}
		ServerText.append("Advisor Process Started"+"\n");
	}
//Method to print in real time if a notification process is started.
	@Override
	public void NotifyStart() throws RemoteException {
		// TODO Auto-generated method stub
		active_list.add("Notify process active");
		int i = 0;
		for(String sactive : active_list ) {
			String[] s_ele1 = sactive.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Notify")) {
			ActiveText.append(sactive + "\n");	}		
			i++;			
		}
		ServerText.append("Notify Process Started"+"\n");
	}

//Add student function to active list if server crashes and comes back again.
//no input and void output.
	@Override
	public void add_student() throws RemoteException {
		// TODO Auto-generated method stub
		int i = 0;
		String found = "FALSE";
		for(String sactive_list : active_list ) {
			String[] s_ele1 = sactive_list.split(" ");
			if(s_ele1[0].equalsIgnoreCase("Student")){
				found ="TRUE";
				break;
			}
			i++;
			
		}
		if(!found.equalsIgnoreCase("TRUE")) {
			active_list.add("Student process active");
			int k = 0;
			for(String sactive : active_list ) {
				String[] s_ele2 = sactive.split(" ");
				if(s_ele2[0].equalsIgnoreCase("Student")) {
					ActiveText.append(sactive + "\n");	}
						
					k++;			
			}
		}
		
		
	}
}