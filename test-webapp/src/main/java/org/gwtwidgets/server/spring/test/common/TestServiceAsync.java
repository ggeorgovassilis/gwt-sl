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

package org.gwtwidgets.server.spring.test.common;

import org.gwtwidgets.server.spring.test.client.GWTSerialisableObject;
import org.gwtwidgets.server.spring.test.client.JavaSerialisableObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of the {@link TestService} service. 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public interface TestServiceAsync {

	void add(int a, int b, AsyncCallback<Integer> callback);

	void throwDeclaredException(AsyncCallback<Void> callback);

	void throwUndeclaredException(AsyncCallback<Object> callback);

	void replaceAttribute(String name, String value, AsyncCallback<String> callback);

	void replaceAttributeAlt(String name, String value, AsyncCallback<String> callback);

	void getGWTSerialisableObject(GWTSerialisableObject o, AsyncCallback<GWTSerialisableObject> callback);

	void getJavaSerialisableObject(JavaSerialisableObject o, AsyncCallback<JavaSerialisableObject> callback);
	
	void getRandomNumber(AsyncCallback<Integer> callback);

}
