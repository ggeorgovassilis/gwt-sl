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

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.junit.After;
import org.junit.Before;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Base class for integration tests which starts an embedded tomcat.
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public abstract class BaseIntegrationTest {

	public final String WEBAPP_BASE = "/test-webapp";
	public final int SERVER_PORT = 7777;
	public final int TIMEOUT = 30000;
	public final String baseUrl="http://localhost:"+SERVER_PORT+WEBAPP_BASE+"/";

	Tomcat tomcat;
	WebClient webClient;

	@Before
	public void setupServer() throws Exception {
		String webappDirLocation = "target/test-webapp";
		tomcat = new Tomcat();
		File temp = File.createTempFile("test-webapp", ".tmp");
		tomcat.setBaseDir(temp.getParent());
		tomcat.setPort(SERVER_PORT);
		tomcat.enableNaming();
		Context context = tomcat.addWebapp(WEBAPP_BASE, new File(webappDirLocation).getAbsolutePath());
		ContextResource resource = new ContextResource();
		resource.setName("jdbc/twetDataSource");
		resource.setAuth("Container");
		resource.setType(javax.sql.DataSource.class.getName());
		resource.setScope("Sharable");
		resource.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver");
		resource.setProperty("url", "jdbc:hsqldb:mem:testdb");
		context.getNamingResources().addResource(resource);
		tomcat.getServer().getGlobalNamingResources().addResource(resource);
		tomcat.start();
	}

	@Before
	public void setupBrowser() {
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setCssEnabled(true);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
		webClient.getOptions().setThrowExceptionOnScriptError(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setTimeout(TIMEOUT);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setAlertHandler(new AlertHandler() {

			public void handleAlert(Page page, String message) {
				System.err.println("[alert] " + message);
			}

		});
		webClient.waitForBackgroundJavaScript(TIMEOUT);
	}
	
	@After
	public void after() throws Exception {
		tomcat.stop();
		tomcat.destroy();
		webClient.close();
	}

}

