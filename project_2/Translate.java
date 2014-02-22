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
* Input: None
* Output: Writes a file containing all english words entered by the
* user and the translated words. Also displays the translated word to
* the user.
*********************************************************************
* Maintenance Log
*********************************************************************
* FIX001 02/22/14 Kenny Hegeland
* Moved all exception logic to the run method which allows a try-
* catch-finally block to open streams, do stuff and ensure they are
* closed even if an exception occurs.
* FIX002 02/22/14 Kenny Hegeland
* Added support to change the translation dictionary file. We do this
* by allowing the user to use the flag -l, followed by the language
* to translate the word to.
* FIX003 02/22/14 Kenny Hegeland
* Allowed the output file to specify the language the english word is
* being translated to.
********************************************************************/

/********************************************************************
*                            Translate Class
* Gets user input and writes it to a seocket. The other end of the 
* socket is connected to the dictionary (which works as our server) 
* where the word is translated into a foreign word.
*********************************************************************
* Input: English word
* Output: Foreign word
********************************************************************/
public class Translate{
	private Scanner in;
	private PrintWriter writer;
	private BufferedReader socketRead;
	private PrintWriter socketWrite;
	private String language;
	
	public static final String host = "127.0.0.1";
	public static final int port = 8080;
		
	public Translate(){
		// Open scanner to read user input
		this.in = new Scanner(System.in);
		this.language = "norwegian";
	}
	
	// Makes a connection with the dictionary server.
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
	
	/**************************************************************
	* Gives the word to the dictionary to translate and updates the
	* log file.
	**************************************************************/
	public void callDictionary(String en) throws IOException{
		/*********************************************************
		* Write the english word to the socket for the dictionary.
		*********************************************************/
		socketWrite.println(en);
		String foreign;
		
		// Read translated word from the socket
		foreign = socketRead.readLine();
		// We changed the language - do not log this
		if(foreign.startsWith("Language changed to")){
			this.language = foreign.split(" ")[3];
			System.out.println(foreign);
			return;
		}
		writer.print("ENGLISH: " + en);
		writer.println(", "+this.language.toUpperCase()+": "+foreign);
		System.out.println(foreign);
	}
	
	public Scanner getIn(){ return this.in; }
	
	/**************************************************************
	* This method contains all the logic to run the program. It 
	* opens streams to the server and gets the user input which is
	* passed to the dictionary to be translated.
	**************************************************************/
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
