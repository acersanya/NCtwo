package test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * 
 * @author acersanya
 *	Invoking Junit tests from here
 */
public class TestRunner {

	public static void main(String[] args){
		Result result = JUnitCore.runClasses(ArrayJournalTest.class);
		for(Failure failure: result.getFailures()){
			System.out.println(failure.toString());
		}
		System.out.println("Congruts:"+result.wasSuccessful());
		
	}
	
}
