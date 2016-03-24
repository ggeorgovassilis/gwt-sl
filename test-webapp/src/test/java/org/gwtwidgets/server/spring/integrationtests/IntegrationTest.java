package org.gwtwidgets.server.spring.integrationtests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Category(WebserverTest.class)
public class IntegrationTest extends BaseIntegrationTest{

	void waitForTextOnPage(HtmlPage page, String text) throws Exception{
		//wait for tests to finish
		long time = -System.currentTimeMillis();
		while (!page.asText().contains(text)){
			Thread.sleep(500);
			if (time+System.currentTimeMillis()>TIMEOUT)
				throw new Exception("Test timed out");
		}
	}
	
	@Test
	public void test_page() throws Exception{

		HtmlPage page = webClient.getPage(baseUrl);
		waitForTextOnPage(page, "Tests finished in");
		String p = page.asText();
		assertTrue(p.contains("(0) Test1 success: 7"));
		assertTrue(p.contains("(0) Test2 success: null"));
		assertTrue(p.contains("(0) Test3 success"));
		assertTrue(p.contains("(0) Test4 success"));
		assertTrue(p.contains("(0) Test5 success"));
		assertTrue(p.contains("(0) Test5b success"));
		
		assertTrue(p.contains("(1) Test1 success: 7"));
		assertTrue(p.contains("(1) Test2 success"));
		assertTrue(p.contains("(1) Test3 success:"));
		assertTrue(p.contains("(1) Test4 success"));
		assertTrue(p.contains("(1) Test5 success"));
		assertTrue(p.contains("(1) Test5b success"));
		
		assertTrue(p.contains("(2) Test1 success: 7"));
		assertTrue(p.contains("(2) Test2 success"));
		assertTrue(p.contains("(2) Test3 success"));
		assertTrue(p.contains("(2) Test4 success"));
		assertTrue(p.contains("(2) Test5 success"));
		assertTrue(p.contains("(2) Test5b success"));
		
		assertTrue(p.contains("(3) Test1 success: 7"));
		assertTrue(p.contains("(3) Test2 success"));
		assertTrue(p.contains("(3) Test3 success"));
		assertTrue(p.contains("(3) Test4 success"));
		assertTrue(p.contains("(3) Test5 success"));
		assertTrue(p.contains("(3) Test5b success"));
		
		assertTrue(p.contains("(4) Test1 success: 7"));
		assertTrue(p.contains("(4) Test2 success"));
		assertTrue(p.contains("(4) Test3 success"));
		assertTrue(p.contains("(4) Test4 success"));
		assertTrue(p.contains("(4) Test5 success"));
		assertTrue(p.contains("(4) Test5b success"));

		assertTrue(p.contains("(5) Test1 success: 7"));
		assertTrue(p.contains("(5) Test2 success"));
		assertTrue(p.contains("(5) Test3 success"));
		assertTrue(p.contains("(5) Test4 success"));
		assertTrue(p.contains("(5) Test5 success"));
		assertTrue(p.contains("(5) Test5b success"));

		assertTrue(p.contains("(6) Test1 success: 7"));
		assertTrue(p.contains("(6) Test2 success"));
		assertTrue(p.contains("(6) Test3 success"));
		assertTrue(p.contains("(6) Test4 success"));
		assertTrue(p.contains("(6) Test5 success"));
		assertTrue(p.contains("(6) Test5b success"));

		assertTrue(p.contains("(7) Test1 success: 7"));
		assertTrue(p.contains("(7) Test2 success"));
		assertTrue(p.contains("(7) Test3 success"));
		assertTrue(p.contains("(7) Test4 success"));
		assertTrue(p.contains("(7) Test5 success"));
		assertTrue(p.contains("(7) Test5b success"));

		assertTrue(p.contains("(8) Test1 success: 7"));
		assertTrue(p.contains("(8) Test2 success"));
		assertTrue(p.contains("(8) Test3 success"));
		assertTrue(p.contains("(8) Test4 success"));
		assertTrue(p.contains("(8) Test5 success"));
		assertTrue(p.contains("(8) Test5b success"));
		
		assertTrue(p.contains("Testing IsSerializable compatibility\n(9) Test success"));
		assertTrue(p.contains("Testing Serializable compatibility\n(9) Test success"));
		Matcher m=Pattern.compile("Testing that RPC responses are not cached.*\\(9\\) Test success",Pattern.DOTALL|Pattern.MULTILINE).matcher(p);
		assertTrue(m.find());
	}

}
