import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;

/********************************************************
*                  Dictionary
* Translate an english word to some foreign language. The
* language translated to by default is norwegian. After 
* the word has been translated, it is written back to the
* socket.
*********************************************************
* Input: None
* Output: Writes a foreign word to the socket
********************************************************/
public class Dictionary{
	private String fileName;
	private BufferedReader reader;
	
	private PrintWriter sockWrite;
	private BufferedReader sockRead;
	
	public static final int port = 8080;
	
	public Dictionary(){
		this.fileName = "norwegian";
	}
	
	// Open streams and start accepting clients
	public void openConnection() throws 	UnknownHostException,
								IOException{
		// Start socket connection
		ServerSocket serSock = new ServerSocket(port);
		Socket client = serSock.accept();
		
		// Open streams to socket
		this.sockWrite = 
			new PrintWriter(client.getOutputStream(), true);
			
		InputStreamReader irs = 
			new InputStreamReader(client.getInputStream());
		this.sockRead = new BufferedReader(irs);
	}
	
	// clean up -- close the open readers
	public void cleanUp(){
		try{
			// we are done reading. Close our streams.
			reader.close();
			sockWrite.close();
			sockRead.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// returns the foreign word if found, otherwise return null
	public String translate(String en) throws IOException{
		String[] enFo;
		String temp;
		if(en.startsWith("-l")){
			this.fileName = en.split(" ")[1];
			return "Language changed to " + this.fileName;
		}
		FileReader fr = new FileReader(this.fileName+".txt");
		this.reader = new BufferedReader(fr);
		// read from file until we reach eof (and get null)
		while((temp = reader.readLine()) != null){
			enFo = temp.split(", ");
			// our word matches an (english, foreign) word pair
			if(en.equals(enFo[0])){
				return enFo[1];
			}
		}
		return "Translation not found";
	}
	
	public BufferedReader getSockReader(){ return sockRead; }
	public PrintWriter getSockWrite(){ return sockWrite; }
	
	// Only set the file if a new file was given
	public void setFileName(String fileName){ 
		if(fileName.equals(this.fileName)){ return; }
		this.fileName = fileName;
	}
	
	/*********************************************************
	* Creates a dictionary, and opens a socket. Continually
	* reads from the socket and translates the word found and
	* writes it back to the socket.
	*********************************************************/
	public static int processWord(){
		Dictionary d = new Dictionary();
		try{
			// Open the socket.
			d.openConnection();
			
			// Read the word from the socket
			String en;
			while((en = d.getSockReader().readLine()) != null){
				// Translate the word
				String foreign = d.translate(en);
						
				// Write translated word to the socket
				d.getSockWrite().println(foreign);
			}
		} catch(UnknownHostException uhe){
			uhe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		} finally{
			d.cleanUp();
		}
		return 0;
	}
	
	public static void main(String[] args){
		processWord();
	}
		
}
