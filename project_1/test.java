import java.io.InputStream;
import java.io.BufferedReader;
import jave.io.OutputStream;

public class test{
	private static final String cmd = "java -jar ";
	private static final String jarName = "Input.jar";
	public static void main(String[] args){
		//System.out.println("HI");
		try{
			Runtime runner = Runtime.getRuntime();
			Process output = runner.exec(cmd + jarName);
			InputStream processOut = output.getInputStream();
			BufferedReader br = new BufferedReader(processOut);
			OutputStream precessIn = output.getOutputStream();
			/*
			InputStream t1 = output.getInputStream();
			int temp;
			while((temp = t1.read()) > 0){
				System.out.print((char)temp);
			}*/
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
