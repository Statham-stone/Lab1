package java_lab1;

import static org.junit.Assert.*;

import org.junit.Test;

public class lab6_junit_test {

	@Test
	public void test1() {
		
		java_lab1 junit_test = new java_lab1();
		junit_test.all_else();
		String result = junit_test.simplify("!simplify a=1.0");
		assertEquals("1.0+1.0*b+1.0*d*d*d*s+1.0*c*d*f*s*s", result);		
	}
	
	@Test
	public void test2() {
		
		java_lab1 junit_test = new java_lab1();
		junit_test.all_else();
		String result = junit_test.simplify("!simplify a=1 b=2");
		assertEquals("3.0+1.0*d*d*d*s+1.0*c*d*f*s*s", result);		
	}

}
