import java.net.Socket;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

public class TestThread implements Runnable{
	private Socket client;
	private PrintWriter sockWrite;
	private BufferedReader sockRead;
	private Scanner in;
	
	public TestThread(Socket client){
		this.client = client;
		this.in = new Scanner(System.in);
	}
	
	public void openStreams() throws	IOException,
										InterruptedException{
		this.sockWrite = new PrintWriter(this.client.getOutputStream(), true);
		
		InputStreamReader irs = 
			new InputStreamReader(this.client.getInputStream());
		this.sockRead = new BufferedReader(irs);
	}
	
	public void run(){
		try{
			this.openStreams();
			while(true){
				String message = this.sockRead.readLine();
				this.sockWrite.println(message);
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
}	
