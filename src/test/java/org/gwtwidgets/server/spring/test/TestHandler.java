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

package org.gwtwidgets.server.spring.test;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.GWTHandler;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.gwtwidgets.server.spring.test.domain.CustomException;
import org.gwtwidgets.server.spring.test.server.ServiceTest;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;

/**
 * Unit test for the handler. Loads handler-servlet.xml and applicationContext.xml
 * spring bean definitions. 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public class TestHandler extends BaseTest {

	private Log logger = LogFactory.getLog(getClass());

	XmlWebApplicationContext applicationContext;

	HttpServletRequest requestService;

	GWTHandler handler;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ServletContext servletContext = new MockServletContext(
				new FileSystemResourceLoader());
		requestService = new MockHttpServletRequest("PUT", "/service");
		applicationContext = new XmlWebApplicationContext();
		applicationContext.setServletContext(servletContext);
		applicationContext.setConfigLocations(new String[] {
				"src/test/webapp/WEB-INF/handler-servlet.xml",
				"src/test/webapp/WEB-INF/applicationContext.xml" });
		try{
		applicationContext.refresh();
		}
		catch (Throwable e){
			e.printStackTrace();
		}
		handler = (GWTHandler) applicationContext.getBean("urlMapping", GWTHandler.class);
	}

	public void testServiceInvocation() throws Exception {
		logger.info("Testing simple invocation");
		HandlerExecutionChain chain = handler.getHandler(requestService);
		GWTRPCServiceExporter exporter = (GWTRPCServiceExporter) chain.getHandler();
		ServiceTest service = (ServiceTest) exporter.getService();
		assertEquals(service.add(3, -5),-2);
	}

	public void testExceptionTranslation() throws Exception {
		logger.info("Testing exception translation for plain service");
		HandlerExecutionChain chain = handler.getHandler(requestService);
		GWTRPCServiceExporter exporter = (GWTRPCServiceExporter) chain.getHandler();
		ServiceTest service = (ServiceTest) exporter.getService();
		try {
			service.throwDeclaredException();
		} catch (Throwable t) {
			assertTrue(t instanceof CustomException);
		}
	}

	public void testExceptionTranslationTx() throws Exception {
		logger.info("Testing exception translation for proxied service");
		HandlerExecutionChain chain = handler.getHandler(requestService);
		GWTRPCServiceExporter exporter = (GWTRPCServiceExporter) chain.getHandler();
		ServiceTest service = (ServiceTest) exporter.getService();
		try {
			service.throwDeclaredException();
		} catch (Throwable t) {
			assertTrue(t instanceof CustomException);
		}
	}

}
