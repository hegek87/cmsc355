import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

/********************************************************************
*                          Translation Module
* Repeatedly prompts the user for input, translating the words to a
* foreign language.
*********************************************************************
* Input: String fileName
* Output: Writes a file containing all english words entered by the
* user and the translated words.
*********************************************************************
* Maintenance Log
*********************************************************************
* FIX001 01/24/14 Kenny Hegeland
* Allowing an optional dictionary file to be supplied
* FIX002 01/24/14 Kenny Hegeland
* Restructured the Translate class to not contain all logic in main
********************************************************************/

/*******************************************************************
*                            Translate Class
* Gets user input and passes it to the Dictionary module to translate
* it to a foreign word.
*********************************************************************
* Input: English word
* Output: Foreign word
********************************************************************/
public class Translate{
	private Scanner in;
	private PrintWriter writer;
	private BufferedReader socketRead;
	private PrintWriter socketWrite;
	
	public static final String host = "127.0.0.1";
	public static final int port = 8080;
	
	public void closeStreams(){
		try{
			writer.close();
			socketRead.close();
			socketWrite.close();
			in.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
		
	public Translate(){
		// Open scanner to read user input
		this.in = new Scanner(System.in);
	}
	
	public void openStreams() throws 	UnknownHostException,
							IOException{
		// Open writer for output file
		FileWriter fw = new FileWriter("output.txt", true);
		this.writer = new PrintWriter(fw,true);
		
		// Open a socket to the dictionary
		Socket clientSock = new Socket(host, port);
			
		// Open streams to the socket for reading and writing
		InputStreamReader is = 
			new InputStreamReader(clientSock.getInputStream());
		this.socketRead = new BufferedReader(is);
		
		this.socketWrite = 
			new PrintWriter(clientSock.getOutputStream(),true);
	}
	
	public void callDictionary(String en) throws IOException{
		/*********************************************************
		* Write the english word to the socket for the dictionary.
		*********************************************************/
		socketWrite.println(en);
		String foreign;
		
		// Read translated word from the socket
		foreign = socketRead.readLine();
		writer.print("English: " + en);
		writer.println(", Foreign: " + foreign);
		System.out.println(foreign);
	}
	
	public Scanner getIn(){ return this.in; }
	
	public static void run(){
		Translate tran = new Translate();
		try{	
			tran.openStreams();
			/**************************************************
			* Continually get user input until user enters q.
			**************************************************/
			String en;
			System.out.print("> ");
			while((en = tran.getIn().nextLine()) != null){
				if(en.equals("q")){ return; }
				tran.callDictionary(en);
				System.out.print("> ");
			} 
		} catch(UnknownHostException uhe){
			uhe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		} finally{
			tran.closeStreams();
		}
	}
	
	public static void main(String[] args){
		// Start up
		run();
	}	
}			
