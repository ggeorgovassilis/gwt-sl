/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtwidgets.server.spring.test.serverimpl;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.springframework.web.servlet.ModelAndView;

/**
 * For development only
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public class DummyGWTRPCServiceExporter extends GWTRPCServiceExporter {

	Log log = LogFactory.getLog(getClass());
	
	private void printHeaders(HttpServletRequest request){
		if (!log.isDebugEnabled()) return;
		log.debug("Handling request with headers:");
		Enumeration<String> e = request.getHeaderNames();
		while (e.hasMoreElements()){
			String name = e.nextElement().toString();
			String value = request.getHeader(name);
			log.debug(name+" = "+value);
		}
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		printHeaders(request);
		return super.handleRequest(request, response);
	}
}