//Name: Anushree Badarinath Kasal
//Student ID: 1001624311
//Net ID: abk4311

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//https://docs.oracle.com/javase/tutorial/rmi/index.html 
//https://www.geeksforgeeks.org/interfaces-in-java/
//https://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html

public interface MsgInterface extends Remote{
//Function for communicating starting of student process.
	public void StudentStart()throws RemoteException;
//Function for communicating starting of advisor process.
	public void AdvisorStart()throws RemoteException;
//Function for communicating starting of notification process.
	public void NotifyStart()throws RemoteException;
//Function used for sending student request.
	public String StudentRequest(String sname, String cno)throws RemoteException;
//Function used by advisor to check if there are any requests.
	public ArrayList<String> AdvisorRequest()throws RemoteException;
//Function used by advisor to push the results to the server.
	public String AdvisorResult(ArrayList<String> res)throws RemoteException;
//Function used by notification process to display the pending results.
	public ArrayList<String> NotifyRequest()throws RemoteException;
//Function for communicating termination of student process.
	public void student_killed()throws RemoteException;
//Function for communicating termination of advisor process.
	public void advisor_killed()throws RemoteException;
//Function for communicating termination of notification process.
	public void notify_killed()throws RemoteException;
//Function to add student to active list if server dies and comes back up again.
	public void add_student()throws RemoteException;

}
