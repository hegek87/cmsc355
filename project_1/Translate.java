import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

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
	public static final String cmd = "java -jar ";
	public static final String dict = "Dictionary.jar ";
	private Scanner in;
	private Runtime runner;
	
	public Translate(){
		// Open scanner to read user input
		this.in = new Scanner(System.in);
		// Get a Runtime environment to call Dictionary
		this.runner = Runtime.getRuntime();
	}
	
	public void callDictionary(String en, String fileName){
		String pCall = cmd + dict + en;
		// Determine whether or not the user supplied a file
		pCall += (fileName == null) ? "" : " " + fileName;
		try{
			// Execute Dictionary.jar
			Process p = this.runner.exec(pCall);
			// Stream for reading output of the call to Dictionary.jar
			InputStream is = p.getInputStream();
			//wait for exec to finish
			p.waitFor();
			int el;
			/**************************************************
			* Read the output from the call to Dictionary.jar
			**************************************************/
			while((el = is.read()) > 0){ System.out.print((char)el); }
		} catch(Exception e){ e.printStackTrace(); }
	}
	
	public Scanner getIn(){ return this.in; }
	
	public static void run(String fileName){
		Translate tran = new Translate();	
		/**************************************************
		* Continually get user input until user enters q.
		**************************************************/
		while(true){
			System.out.print("> ");
			// Read user input
			String en = tran.getIn().nextLine();
			// Has user entered q?
			if(en.equals("q")){ return; }
			tran.callDictionary(en, fileName);
		}
	}
	
	/********************
	* args[0] == fileName
	********************/
	public static void main(String[] args){
		// Scanner to get user input
		String fName;
		/*********************************************************
		* Test if user has supplied an (english,foreign) word pair
		* file.
		*********************************************************/
		if(args.length == 1){ fName = args[0]; }
		else{ fName = null; }
		run(fName);
	}	
}			
