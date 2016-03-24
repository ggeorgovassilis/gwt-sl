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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.util.ImmutableCopyMap;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * <p>This component publishes an object (see {@link #setService(Object)}) as a
 * service to the GWT RPC protocol. Service targets can be:</p>
 * <ul>
 * <li>POJOs which don't have to extend any class or implement any interface.
 * However you should provide a service interface (see
 * {@link #setServiceInterfaces(Class[])})</li>
 * <li>POJOs which implement {@link RemoteService}</li>
 * <li>You can extend the GWTRPCServiceExporter which assigns the target
 * service to itself.</li>
 * </ul>
 * <p>
 * Exceptions directly thrown from the target service are propagated back to the
 * client. For special exception handling you can override the various <code>handle</code>*
 * methods which are invoked by the GWTRPCServiceExporter.</p>
 * 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 * @author Max Jonas Werner
 */
public class GWTRPCServiceExporter extends RemoteServiceServlet implements RPCServiceExporter, ServletContextAware, ServletConfigAware, BeanNameAware {

	/**
	 * Disable RPC response compression. Value is 0.
	 */
	public final static int COMPRESSION_DISABLED = 0;

	/**
	 * Leave default RPC response compression behavior. Value is 1.
	 */

	public final static int COMPRESSION_AUTO = 1;

	/**
	 * Force compression of all RPC responses. Value is 2.
	 */
	public final static int COMPRESSION_ENABLED = 2;

	protected Log logger = LogFactory.getLog(getClass());

	protected Class<?>[] serviceInterfaces;

	protected Object service = this;

	protected ServletContext servletContext;

	protected int compressResponse = COMPRESSION_AUTO;
	
	protected boolean disableResponseCaching = false;
	
	protected boolean throwUndeclaredExceptionToServletContainer = false;
	
	protected String beanName = "GWTRPCServiceExporter";
	
	protected SerializationPolicyProvider serializationPolicyProvider = new DefaultSerializationPolicyProvider();
	
	protected int serializationFlags = AbstractSerializationStream.DEFAULT_FLAGS;
	
	protected boolean shouldCheckPermutationStrongName = false;
	
	protected ModulePathTranslation modulePathTranslation = new ModulePathTranslation() {
		
		public String computeModuleBaseURL(HttpServletRequest request,
				String moduleBaseURL, String strongName) {
			return moduleBaseURL;
		}
	};
	
	public void setModulePathTranslation(ModulePathTranslation modulePathTranslation) {
		this.modulePathTranslation = modulePathTranslation;
	}

	@Override
	protected void checkPermutationStrongName(){
		if (shouldCheckPermutationStrongName)
			super.checkPermutationStrongName();
	}
	
	public void setShouldCheckPermutationStrongName(
			boolean shouldCheckPermutationStrongName) {
		this.shouldCheckPermutationStrongName = shouldCheckPermutationStrongName;
	}

	/**
	 * Return the set serialization flags (see {@link AbstractSerializationStream#getFlags()}
	 * @return Active serialization flags
	 */
	public int getSerializationFlags() {
		return serializationFlags;
	}

	/**
	 * Set serialization flags (see {@link AbstractSerializationStream#getFlags()}. Default value is
	 * {@link AbstractSerializationStream#DEFAULT_FLAGS}
	 * @param serializationFlags Serialization flags to use
	 */
	public void setSerializationFlags(int serializationFlags) {
		this.serializationFlags = serializationFlags;
	}

	/**
	 * Post-processes an RPC response. Default method returns the method's argument
	 * @param response Response
	 * @return Possibly changed response
	 */
	protected String processResponse(String response){
		return response;
	}
	
	@Override
	protected void doUnexpectedFailure(Throwable e) {
		super.doUnexpectedFailure(e);
		if (throwUndeclaredExceptionToServletContainer)
			throw new RuntimeException(e);
	}

	protected Map<Method, Method> methodCache = new ConcurrentHashMap<Method, Method>();

	/**
	 * Disables HTTP response caching by modifying response headers for browsers.
	 * Can be overridden by extending classes to change behavior.
	 * @param request Request to preprocess
	 * @param response Response to preprocess
	 */
	protected void preprocessHTTP(HttpServletRequest request, HttpServletResponse response){
		if (disableResponseCaching)
			ServletUtils.disableResponseCaching(response);
	}

	/**
	 * Returns the installed serialization policy provider. If none other was specified,
	 * the {@link DefaultSerializationPolicyProvider} is used
	 * @return Serialization provider used
	 */
	public SerializationPolicyProvider getSerializationPolicyProvider() {
		return serializationPolicyProvider;
	}

	/**
	 * Assign a new serialization policy provider.
	 * @param serializationPolicyProvider GWT RPC serialization policy registry
	 */
	public void setSerializationPolicyProvider(
			SerializationPolicyProvider serializationPolicyProvider) {
		this.serializationPolicyProvider = serializationPolicyProvider;
	}

	/**
	 * Implementation of {@link ServletContextAware}, is invoked by the Spring
	 * application context.
	 * 
	 * @param servletContext ServletContext to use
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * Returns the servlet context
	 * @return {@link ServletContext}
	 */
	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	protected void onAfterResponseSerialized(String serializedResponse) {
		if (logger.isTraceEnabled())
			logger.trace("Serialised RPC response: [" + serializedResponse + "]");
	}

	@Override
	protected void onBeforeRequestDeserialized(String serializedRequest) {
		if (logger.isTraceEnabled())
			logger.trace("Serialised RPC request: [" + serializedRequest + "]");
	}

	/**
	 * Handles method invocation on a service and is invoked by
	 * {@link #processCall(String)}.
	 * 
	 * @param service
	 *            Service to invoke method on
	 * @param targetMethod
	 *            Method to invoke.
	 * @param targetParameters
	 *            Parameters to pass to method. Can be null for no arguments.
	 * @param rpcRequest
	 *            RPCRequest instance for this request
	 * @return Return RPC encoded result.
	 * @throws Exception Any exception caused in serialization
	 */
	protected String invokeMethodOnService(Object service, Method targetMethod, Object[] targetParameters,
			RPCRequest rpcRequest) throws Exception {
		Object result = targetMethod.invoke(service, targetParameters);
		SerializationPolicy serializationPolicy = getSerializationPolicyProvider().getSerializationPolicyForSuccess(rpcRequest, service, targetMethod, targetParameters, result);
		String encodedResult = RPC.encodeResponseForSuccess(rpcRequest.getMethod(), result, serializationPolicy, serializationFlags);
		return encodedResult;
	}

	/**
	 * Handles an exception which is raised when a method invocation with bad
	 * arguments is attempted. This implementation throws a
	 * {@link SecurityException}. For details on arguments please consult
	 * {@link #invokeMethodOnService(Object, Method, Object[], RPCRequest)}.
	 * 
	 * @param e
	 *            Exception thrown
	 * @param service Service object that raised exception
	 * @param targetMethod Method that raised exception
	 * @param rpcRequest Request that caused exception
	 * @return RPC encoded response (such as an RPC client exception)
	 */
	protected String handleIllegalArgumentException(IllegalArgumentException e, Object service, Method targetMethod,
			RPCRequest rpcRequest) {
		SecurityException securityException = new SecurityException("Blocked attempt to invoke method " + targetMethod);
		securityException.initCause(e);
		throw securityException;
	}

	/**
	 * Handles an exception which is raised when a method access is attempted to
	 * a method which is not part of the RPC interface. This method is invoked
	 * by {@link #processCall(String)}. This implementation throws a
	 * {@link SecurityException}. For details on arguments please consult
	 * {@link #invokeMethodOnService(Object, Method, Object[], RPCRequest)}.
	 * 
	 * @param e
	 *            Exception thrown
	 * @param service Service that raised exception
	 * @param targetMethod method that raised exception
	 * @param rpcRequest Request that raised exception
	 * @return RPC encoded response (such as an RPC client exception)
	 */
	protected String handleIllegalAccessException(IllegalAccessException e, Object service, Method targetMethod,
			RPCRequest rpcRequest) {
		SecurityException securityException = new SecurityException("Blocked attempt to access inaccessible method "
				+ targetMethod + (service != null ? " on service " + service : ""));
		securityException.initCause(e);
		throw securityException;
	}
	
	/**
	 * Wrapper around RPC utility invocation
	 * @param rpcRequest RPCRequest
	 * @param cause Exception to handle
	 * @param targetMethod Method which threw the exception
	 * @param targetParameters Method arguments  
	 * @return RPC payload
	 * @throws SerializationException Exception caused by RPC serialization
	 */
	protected String encodeResponseForFailure(RPCRequest rpcRequest, Throwable cause, Method targetMethod, Object[] targetParameters) throws SerializationException{
		SerializationPolicy serializationPolicy = getSerializationPolicyProvider().getSerializationPolicyForFailure(rpcRequest, service, targetMethod, targetParameters, cause);
		return RPC.encodeResponseForFailure(rpcRequest.getMethod(), cause, serializationPolicy, serializationFlags);
	}

	/**
	 * Handles exceptions thrown by the target service, which are wrapped in
	 * {@link InvocationTargetException}s due to invocation by reflection. This
	 * method is invoked by {@link #processCall(String)}. This implementation
	 * encodes exceptions as RPC errors and returns them. For details on
	 * arguments please consult
	 * {@link #invokeMethodOnService(Object, Method, Object[], RPCRequest)}.
	 * 
	 * @param e
	 *            Exception thrown
	 * @param service Service that caused exception
	 * @param targetMethod Method that raised exception
	 * @param parameters Method parameters
	 * @param rpcRequest GWT RPC request
	 * @return RPC payload
	 * @throws Exception Any exception that may occur during RPC serialization
	 */
	protected String handleInvocationTargetException(InvocationTargetException e, Object service, Method targetMethod, Object[] parameters,
			RPCRequest rpcRequest) throws Exception {
		Throwable cause = e.getCause();
		if (!(cause instanceof RuntimeException))
			logger.warn(cause);
		return encodeResponseForFailure(rpcRequest, cause, targetMethod, parameters);
	}

	/**
	 * Handles exceptions thrown during a service invocation that are not
	 * handled by other exception handlers. {@link #processCall(String)} on
	 * exceptions which have escaped the other exception handlers such as
	 * {@link #handleIllegalAccessException(IllegalAccessException, Object, Method, RPCRequest)}
	 * etc. This implementation re-casts 'e'. For details on arguments please
	 * consult
	 * {@link #invokeMethodOnService(Object, Method, Object[], RPCRequest)}.
	 * 
	 * @param e
	 *            Exception thrown
	 * @param service Service object
	 * @param targetMethod Method that caused the exception
	 * @param rpcRequest GWT request
	 * @return RPC payload
	 * @throws Exception Exactly e
	 */
	protected String handleServiceException(Exception e, Object service, Method targetMethod, RPCRequest rpcRequest)
			throws Exception {
		throw e;
	}

	/**
	 * Handles {@link UndeclaredThrowableException}s which are thrown by the
	 * target service. This method is invoked by
	 * {@link #processCall(String)}. This implementation re-casts 'e'. For
	 * details on arguments please consult
	 * {@link #invokeMethodOnService(Object, Method, Object[], RPCRequest)}.
	 * 
	 * @param e
	 *            Exception thrown
	 * @param service Service that caused exception
	 * @param targetMethod Method that raised exception
	 * @param rpcRequest RPC request that was processed
	 * @return RPC payload
	 * @throws Exception Exactly e
	 */
	protected String handleUndeclaredThrowableException(Exception e, Object service, Method targetMethod,
			RPCRequest rpcRequest) throws Exception {
		throw e;
	}

	/**
	 * Returns method to invoke on service. This implementation calls
	 * {@link ReflectionUtils#getRPCMethod(Object, Class[], Method)}
	 * 
	 * @param decodedMethod
	 *            Method as determined by RPC
	 * @return Method to invoke.
	 * @throws NoSuchMethodException In case method isn't found on service
	 */
	protected Method getMethodToInvoke(Method decodedMethod) throws NoSuchMethodException {
		// Synchronization is unnecessary here, the worst thing that can happen
		// is that a method key is put multiple times in a map, which still
		// would leave only the latest addition. After a short time of operation
		// the method cache should get no writes at all, which makes the
		// immutable copy pattern a good choice.
		Method method = methodCache.get(decodedMethod);
		if (method != null)
			return method;
		method = ReflectionUtils.getRPCMethod(service, serviceInterfaces, decodedMethod);
		return methodCache.put(decodedMethod, method);
	}
	

	/**
	 * Overridden from {@link RemoteServiceServlet} and invoked by the servlet
	 * code.
	 */
	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			checkPermutationStrongName();
			// Copy & pasted & edited from the GWT 1.4.3 RPC documentation
			RPCRequest rpcRequest = RPC.decodeRequest(payload, null, this);
			onAfterRequestDeserialized(rpcRequest);
			Method targetMethod = getMethodToInvoke(rpcRequest.getMethod());
			Object[] targetParameters = rpcRequest.getParameters();

			try {
				return processResponse(invokeMethodOnService(service, targetMethod, targetParameters, rpcRequest));
			} catch (IllegalArgumentException e) {
				return processResponse(handleIllegalArgumentException(e, service, targetMethod, rpcRequest));
			} catch (IllegalAccessException e) {
				return processResponse(handleIllegalAccessException(e, service, targetMethod, rpcRequest));
			} catch (InvocationTargetException e) {
				return processResponse(handleInvocationTargetException(e, service, targetMethod, targetParameters, rpcRequest));
			} catch (UndeclaredThrowableException e) {
				return processResponse(handleUndeclaredThrowableException(e, service, targetMethod, rpcRequest));
			} catch (Exception e) {
				return processResponse(handleServiceException(e, service, targetMethod, rpcRequest));
			}
		} catch (IncompatibleRemoteServiceException e) {
			return processResponse(handleIncompatibleRemoteServiceException(e));
		} catch (Exception e) {
			return processResponse(handleExporterProcessingException(e));
		}
	}

	/**
	 * Invoked by {@link #processCall(String)} when RPC throws an
	 * {@link IncompatibleRemoteServiceException}. This implementation
	 * propagates the exception back to the client via RPC.
	 * 
	 * @param cause
	 *            Exception thrown
	 * @return RPC encoded failure response
	 * @throws SerializationException If problems during serialization of cause to RPC
	 */
	protected String handleIncompatibleRemoteServiceException(IncompatibleRemoteServiceException cause)
			throws SerializationException {
		logger.warn(cause.getMessage());
		return RPC.encodeResponseForFailure(null, cause);
	}

	/**
	 * Invoked by {@link #processCall(String)} for an exception if no suitable
	 * exception handler was found. This is the outermost exception handler,
	 * catching any exceptions not caught by other exception handlers or even
	 * thrown by those handlers. This implementation wraps 'e' in a
	 * {@link RuntimeException} which is then thrown.
	 * 
	 * @param e Exception
	 * @return RPC encoded failure response
	 */
	protected String handleExporterProcessingException(Exception e) {
		throw new RuntimeException(e);
	}

	/**
	 * Set the wrapped service bean. RPC requests are decoded and the corresponding
	 * method of the service object is invoked.
	 * 
	 * @param service Service to which the decoded requests are forwarded
	 */
	public void setService(Object service) {
		this.service = service;
	}

	/**
	 * Implementation of inherited interface
	 * @param request Request
	 * @param response Response
	 * @return {@link ModelAndView}
	 * @throws ServletException Exception
	 * @throws IOException Exception
	 * @see HttpRequestHandler#handleRequest(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			preprocessHTTP(request, response);
			ServletUtils.setResponse(response);
			doPost(request, response);
			return null;
		} finally {
			ServletUtils.setResponse(null);
		}
	}

	/**
	 * Specifies the interfaces which must be implemented by the service bean.
	 * If not specified then any interface extending {@link RemoteService} which
	 * is implemented by the service bean is assumed. Implementation note:
	 * as methods are only lazily bound to the service implementation you may get
	 * away with mismatches between the specified interfaces and the actual implementation
	 * as long as no method is invoked which has a different/missing signature in the interface
	 * and the service implementation.
	 * 
	 * @param serviceInterfaces List of interfaces
	 */
	public void setServiceInterfaces(Class<RemoteService>[] serviceInterfaces) {
		this.serviceInterfaces = serviceInterfaces;
	}

	/**
	 * Should be invoked after all properties have been set. Normally invoked
	 * by the Spring application context setup.
	 * @see InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (service == null)
			throw new Exception("You must specify a service object.");
		if (serviceInterfaces == null) {
			logger.debug("Discovering service interfaces");
			serviceInterfaces = ReflectionUtils.getExposedInterfaces(service.getClass());
			if (serviceInterfaces.length == 0)
				logger.warn("The specified service does neither implement RemoteService "
						+ "nor were any service interfaces specified. RPC access to *all* object methods is allowed.");
		}
		if (servletContext == null){
			logger.warn("No servlet context found. You should declare a GWTRPCServiceExporter or GWTHandler in a servlet context and not the application context.");
		}
	}

	/**
	 * Return target service. Each {@link GWTRPCServiceExporter} has a single
	 * target service which it redirects RPC to.
	 * 
	 * @return Object
	 */
	public Object getService() {
		return service;
	}

	@Override
	protected boolean shouldCompressResponse(HttpServletRequest request, HttpServletResponse response,
			String responsePayload) {
		switch (compressResponse) {
		case COMPRESSION_DISABLED:
			return false;
		case COMPRESSION_ENABLED:
			return true;
		}
		return super.shouldCompressResponse(request, response, responsePayload);
	}

	/**
	 * Enables or disables compression of RPC output. Defaults to
	 * {@link #COMPRESSION_AUTO}. 
	 * 
	 * @param compressResponse Allowed values are
	 * {@link #COMPRESSION_ENABLED}, {@link #COMPRESSION_DISABLED} and
	 * {@link #COMPRESSION_AUTO}.
	 */
	protected void setCompressResponse(int compressResponse) {
		if (compressResponse != COMPRESSION_ENABLED && compressResponse != COMPRESSION_DISABLED && compressResponse != COMPRESSION_AUTO)
			throw new IllegalArgumentException("Invalid compressResponse argumnet "+compressResponse);
		this.compressResponse = compressResponse;
	}

	/**
	 * Can be used to set HTTP response headers that explicitly disable caching on the browser side.
	 * Note that due to the additional headers the response size increases.
	 * @param disableResponseCaching disableResponseCaching
	 */
	public void setResponseCachingDisabled(boolean disableResponseCaching) {
		this.disableResponseCaching = disableResponseCaching;
	}

	/**
	 * When enabled will throw exceptions which originate from the service and have not been
	 * declared in the RPC interface back to the servlet container.
	 * @param throwUndeclaredExceptionToServletContainer Defaults to <code>false</code> 
	 */
	public void setThrowUndeclaredExceptionToServletContainer(boolean throwUndeclaredExceptionToServletContainer) {
		this.throwUndeclaredExceptionToServletContainer = throwUndeclaredExceptionToServletContainer;
	}

	/**
	 * Setter for servlet configuration
	 */
	public void setServletConfig(ServletConfig servletConfig) {
		try {
			init(servletConfig);
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		String newModuleBaseURL = modulePathTranslation.computeModuleBaseURL(request, moduleBaseURL, strongName);
		return super.doGetSerializationPolicy(request, newModuleBaseURL, strongName);
	}
	
	@Override
	public String toString() {
		return beanName;
	}
	

}