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
 * This serialization policy provider simply returns {@link RPCRequest#getSerializationPolicy()}
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 */
public class DefaultSerializationPolicyProvider implements SerializationPolicyProvider{

	public SerializationPolicy getSerializationPolicyForSuccess(
			RPCRequest request, Object target, Method method,
			Object[] parameters, Object result) {
		return request.getSerializationPolicy();
	}

	public SerializationPolicy getSerializationPolicyForFailure(
			RPCRequest request, Object target, Method method,
			Object[] parameters, Throwable exception) {
		return request.getSerializationPolicy();
	}

}
