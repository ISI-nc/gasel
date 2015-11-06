import junit.textui.TestRunner;
import tests.model.AllModelTests;

public class TestSurJar {

	public static void main(String[] args) {
		TestRunner.run(AllModelTests.suite());
	}

}
