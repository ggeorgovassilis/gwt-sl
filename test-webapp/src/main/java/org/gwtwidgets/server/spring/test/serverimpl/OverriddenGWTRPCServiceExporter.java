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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.gwtwidgets.server.spring.GWTRPCServiceExporter;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * Exists only to warn developers via the override annotation of contract
 * violations in {@link GWTRPCServiceExporter} - i.e. when a method is deleted
 * which was promised to always exist for overriding purposes.
 * 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 * 
 */
public class OverriddenGWTRPCServiceExporter extends GWTRPCServiceExporter {

	@Override
	protected String handleServiceException(Exception e, Object service,
			Method targetMethod, RPCRequest rpcRequest) throws Exception {
		return null;
	}

	@Override
	protected String handleIllegalAccessException(IllegalAccessException e,
			Object service, Method targetMethod, RPCRequest rpcRequest) {
		return null;
	}

	@Override
	protected String handleIllegalArgumentException(IllegalArgumentException e,
			Object service, Method targetMethod, RPCRequest rpcRequest) {
		return null;
	}

	@Override
	protected String handleInvocationTargetException(
			InvocationTargetException e, Object service, Method targetMethod,
			Object[] parameters, RPCRequest rpcRequest) throws Exception {
		return null;
	}

	@Override
	protected String handleUndeclaredThrowableException(Exception e,
			Object service, Method targetMethod, RPCRequest rpcRequest)
			throws Exception {
		return null;
	}

	@Override
	protected Method getMethodToInvoke(Method decodedMethod)
			throws NoSuchMethodException {
		return null;
	}

	@Override
	protected String handleExporterProcessingException(Exception e) {
		return null;
	}

	@Override
	protected String handleIncompatibleRemoteServiceException(
			IncompatibleRemoteServiceException e) throws SerializationException {
		return null;
	}
}
