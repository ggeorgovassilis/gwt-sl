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

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.gwtwidgets.server.spring.ServletUtils;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Abstract basis test for unit tests
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public abstract class BaseTest{

	protected String readResource(String resource) throws Exception {
		URL url = getClass().getClassLoader().getResource(resource);
		if (url == null)
			url = ClassLoader.getSystemResource(resource);
		if (url == null)
			url = ClassLoader.getSystemClassLoader().getResource(resource);
		URLConnection conn = url.openConnection();
		byte[] b = new byte[conn.getContentLength()];
		InputStream in = conn.getInputStream();
		in.read(b);
		in.close();
		return new String(b, "UTF-8");
	}

	@Before
	public void setUp() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// need to override our own request
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		ServletUtils.setResponse(response);
	}
	
}
