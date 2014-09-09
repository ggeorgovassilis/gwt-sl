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

package org.gwtwidgets.server.spring.test.serverimpl;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.ServletUtils;
import org.gwtwidgets.server.spring.test.client.GWTSerialisableObject;
import org.gwtwidgets.server.spring.test.client.JavaSerialisableObject;
import org.gwtwidgets.server.spring.test.domain.CustomException;
import org.gwtwidgets.server.spring.test.server.ServiceTest;

/**
 * Implements all methods of the {@link ServiceTest} interface without
 * implementing the interface itself. We'll use this service to demonstrate
 * publishing POJOs as RPC services.
 * 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 * 
 */
public class ServiceTestPOJO {

	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	private Log log = LogFactory.getLog(getClass());
	private Random random = new Random();
	
	public void setRequest(HttpServletRequest request){
		this.request.set(request);
	}

	public void setResponse(HttpServletResponse response){
		this.response.set(response);
	}

	
	public int add(int a, int b) {
		return a + b;
	}
	
	private String replaceAttribute(HttpSession session, String name, String value) {
		String oldValue = (String) session.getAttribute(name);
		session.setAttribute(name, value);
		return oldValue;
	}

	public String replaceAttribute(String name, String value) {
		return replaceAttribute(ServletUtils.getRequest().getSession(true), name, value);
	}

	public String replaceAttributeAlt(String name, String value) {
		return replaceAttribute(request.get().getSession(true), name, value);
	}

	public void throwDeclaredException() throws CustomException {
		log.debug("The following exception is intentional and part of the unit test");
		throw new CustomException();
	}

	public void throwUndeclaredException(){
		log.debug("The following exception is intentional and part of the unit test");
		throw new IllegalArgumentException("Undeclared exception");
	}

	public GWTSerialisableObject getGWTSerialisableObject(GWTSerialisableObject o){
		o.setString(o.getString().toLowerCase());
		o.setNumber(o.getNumber()+1);
		return o;
	}

	public JavaSerialisableObject getJavaSerialisableObject(JavaSerialisableObject o){
		o.setString(o.getString().toLowerCase());
		o.setNumber(o.getNumber()+1);
		return o;
	}
	
	public Integer getRandomNumber(){
		return random.nextInt();
	}

}
