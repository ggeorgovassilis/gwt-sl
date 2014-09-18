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

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Declares three methods which our test service will expose.
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public interface TestService extends RemoteService{
	
/**
 * Adds two integers together and return result
 * @param a
 * @param b
 * @return
 */
int add(int a, int b);

/**
 * Set a session attribute and return old value
 * @param name
 * @param value
 * @return
 */
String replaceAttribute(String name, String value);

/**
 * Set a session attribute and return old value. Should use
 * an alternative implementation to access the servlet session.
 * @param name
 * @param value
 * @return
 */
String replaceAttributeAlt(String name, String value);

/**
 * Throws an exceptions that has been declared in the interface}
 * @throws CustomException
 */
void throwDeclaredException() throws CustomException;

/**
 * Throws an exceptions that has not been declared in the interface}
 * @throws RuntimeException
 */
void throwUndeclaredException();

/**
 * @param o Object argument. Will turn string to lower case and increment number by 1
 * @return Testing {@link GWTSerialisableObject}
 */

GWTSerialisableObject getGWTSerialisableObject(GWTSerialisableObject o);

/**
 * @param o Object argument. Will turn string to lower case and increment number by 1
 * @return Testing {@link JavaSerialisableObject}
 */

JavaSerialisableObject getJavaSerialisableObject(JavaSerialisableObject o);

/**
 * Returns a random number. 
 * @return
 */
Integer getRandomNumber();

}
