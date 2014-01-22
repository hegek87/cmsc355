import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class Translate{
	public static final String cmd = "java -jar ";
	public static final String dict = "Dictionary.jar ";
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		String fileName, en;
		BufferedReader out;
		InputStream is;
		OutputStream os;
		if(args.length != 1){
			fileName = null;
		}
		else{
			fileName = args[0];
		}
		try{
			Runtime runner = Runtime.getRuntime();
			while(true){
				System.out.print("> ");
				en = in.nextLine();
				System.out.println(en);
				if(en.equals("q")){
					break;
				}
				String pCall = cmd+dict+en+" "+fileName;
				Process p = runner.exec(pCall);	
				int retVal = p.waitFor();
				
				is = p.getInputStream();
				int el;
				while((el = is.read()) > 0){
					System.out.print((char)el);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}	
}
			
			
