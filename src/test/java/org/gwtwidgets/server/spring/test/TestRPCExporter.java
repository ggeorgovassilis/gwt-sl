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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 * Tests RPC to a {@link GWTRPCServiceExporter}.
 * 
 * @author George Georgovassilis g.georgovassilis[at]gmail.com
 * 
 */
public class TestRPCExporter extends BaseTest {

	private String requestPayload;
	private String responsePayload;
	private Log logger = LogFactory.getLog(getClass());
	private final static int testCount = 100000;

	XmlWebApplicationContext applicationContext;
	SimpleUrlHandlerMapping handlerMapping;

	@Override
	protected void setUp() throws Exception {

		requestPayload = readResource("request_add.txt");
		responsePayload = readResource("response_add.txt");
		MockServletContext servletContext = new MockServletContext(new TestResourceLoader());
		MockServletConfig servletConfig = new MockServletConfig(servletContext);
		applicationContext = new XmlWebApplicationContext();
		applicationContext.setServletContext(servletContext);
		applicationContext.setServletConfig(servletConfig);
		applicationContext.setConfigLocations(new String[] { "src/test/webapp/WEB-INF/exporter-servlet.xml",
				"src/test/webapp/WEB-INF/applicationContext.xml" });
		applicationContext.refresh();
		handlerMapping = (SimpleUrlHandlerMapping) applicationContext
				.getBean("org.springframework.web.servlet.handler.SimpleUrlHandlerMapping");
	}

	private HttpServletRequest getServiceRequest() throws Exception {
		MockHttpServletRequest serviceRequest = new MockHttpServletRequest("PUT", "/service");
		serviceRequest.setContentType("text/x-gwt-rpc");
		serviceRequest.setCharacterEncoding("UTF-8");
		serviceRequest.setContent(requestPayload.getBytes("UTF-8"));
		return serviceRequest;
	}

	private MockHttpServletResponse getServiceResponse() {
		return new MockHttpServletResponse();
	}

	public void testExporter() throws Exception {
		try {
			logger.info("stressTestExporter: testing RPC");
			HttpServletRequest serviceRequest = getServiceRequest();
			HandlerExecutionChain chain = handlerMapping.getHandler(serviceRequest);
			GWTRPCServiceExporter exporter = (GWTRPCServiceExporter) chain.getHandler();
			long start = System.currentTimeMillis();
			for (int i = 0; i < testCount; i++) {
				MockHttpServletResponse serviceResponse = getServiceResponse();
				exporter.handleRequest(serviceRequest, serviceResponse);
				String sResponse = serviceResponse.getContentAsString();
				assertEquals(responsePayload, sResponse);
			}
			long end = System.currentTimeMillis();
			logger.info("stressTestExporter: " + testCount + " invocations in " + (end - start) + " ms");
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			fail();
		}
	}
}
