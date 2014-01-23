import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Tests for {@link Dictionary}.
 *
 * @author hegelandka@vcu.edu (Kenny Hegeland)
apple, eple
orange, appelsin
grape, drue
bike, sykkel
car, bil
airplane, fly
face, ansikt
table, bord
quit, slutte
bark, bjeff
sleep, sove
clothing, kledning
finish, avslutte
movie, film
house, hus
 */
@RunWith(JUnit4.class)
public class TestDictionary {
	Dictionary dict;
	public static final String[] fileData = { "apple, eple",
								"orange, appelsin",
								"grape, drue",
								"bike, sykkel",
								"car, bil",
								"airplane, fly",
								"face, ansikt",
								"table, bord",
								"quit, slutte",
								"bark, bjeff",
								"sleep, sove",
								"clothing, kledning",
								"finish, avslutte",
								"movie, film",
								/*"house, hus"*/
							};
	
	
	@Before
	public void setUp(String fileName) throws Exception{
		this.dict = new Dictionary(fileName);
	}
	/*
	@Test
	public void openFiles(){
		dict.openDict();
		if
	*/
	@Test
	public void fileReading() throws Exception{
		String temp;
		String[] fileDat = new String[15];
	 	int i = 0;
		while((temp = dict.getReader().readLine()) != null){
			fileData[i++] = temp;
		}
		assertArrayEquals(fileData, fileDat);		
	}

	@Test
	@Ignore
	public void thisIsIgnored() {
	
	}
	
	public static void main(String[] args){
		Result result = JUnitCore.runClasses(Dictionary.class);
		for(Failure f : result.getFailures()){
			System.out.println(f);
		}
	}
}
