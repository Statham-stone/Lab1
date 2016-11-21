package java_lab1;

import static org.junit.Assert.*;

import org.junit.Test;

public class lab6_junit_test {

	@Test
	public void test1() {
		
		java_lab1 junit_test = new java_lab1();
		junit_test.all_else();
		String result = junit_test.deriative("!d/da");
		assertEquals("1.0+1.0*d*d*d*s+2.0*a*c*d*f*s*s", result);		
	}


}
