/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtwidgets.server.spring.integrationtests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import static org.junit.Assert.*;


/**
 * Integration test that runs a tomcat, deploys the GWT web applications,
 * hits it with an htmlunit browser and checks that the web applications
 * runs in the browser as expected. The test web application loads the
 * GWT compiled applications and communicates rather heavily with the
 * server over RPC, testing GWT-SL Spring integration.
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */

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
		
		assertTrue(p.contains("(9) Test success"));
		assertTrue(p.contains("(10) Test success"));
		assertTrue(p.contains("(11) Test success"));
	}

}
