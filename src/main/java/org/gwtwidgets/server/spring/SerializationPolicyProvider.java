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

package org.gwtwidgets.server.spring;

import java.lang.reflect.Method;

import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Implementations of this interface can be consulted by the
 * {@link GWTRPCServiceExporter} to provide serialization policies for a
 * specific request
 * 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 */
public interface SerializationPolicyProvider {

	/**
	 * This method returns the appropriate serialization policy in case of a successful service invocation
	 * @param request The RPCRequest
	 * @param target The service object on which the method was invoked
	 * @param method The method invoked
	 * @param parameters Arguments provided to the method invoked
	 * @param result The result returned by the method
	 * @return SerializationPolicy to use. If null is returned, then the default serialization policy will be used
	 */
	SerializationPolicy getSerializationPolicyForSuccess(RPCRequest request, Object target, Method method, Object[] parameters, Object result);

	/**
	 * This method returns the appropriate serialization policy in case of a failed service invocation
	 * @param request The RPCRequest
	 * @param target The service object on which the method was invoked
	 * @param method The method invoked
	 * @param parameters Arguments provided to the method invoked
	 * @param exception The exception thrown while invoking the method
	 * @return SerializationPolicy to use. If null is returned, then the default serialization policy will be used
	 */
	SerializationPolicy getSerializationPolicyForFailure(RPCRequest request, Object target, Method method, Object[] parameters, Throwable exception);
}
