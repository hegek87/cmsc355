import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
/*
*  CHANGE TO PRINTWRITER INSTEAD OF BUFFEREDWRITER
*/
public class Dictionary{
	private String fileName;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public Dictionary(String fileName){
		this.fileName = fileName;
	}
	
	// open files for reading and writing dictionary data
	private void openDict(){
		try{
			FileReader fr = new FileReader(this.fileName);
			FileWriter fw = new FileWriter(this.fileName);
			this.reader = new BufferedReader(fr);
			this.writer = new BufferedWriter(fw);
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// clean up -- close the open readers
	public void cleanUp(){
		reader.close();
		write.close();
	}
	
	public BufferedReader getReader(){ return reader; }
	public BufferedWriter getWriter(){ return writer; }
	
	/*
	*  args[0] == Dictionary
	*  args[1] == English word
	*  args[2] == fileName
	*/
	public static void main(String[] args){
		Dictionary d;
		String fileName;
		if(args.length < 3){
			// no dictionary supplied
			fileName = "norwegian.txt";
		}
		else{
			fileName = args[3];
		}
		d = new Dictionary(fileName);
		d.openDict();
		
		String foreign = d.translate();
		// display to user
		System.out.println(foreign);
		
		d.getWriter().
		
}
