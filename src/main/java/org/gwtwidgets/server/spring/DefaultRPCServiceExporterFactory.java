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

package org.gwtwidgets.server.spring;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;

/**
 * Returns for each invocation a new {@link GWTRPCServiceExporter} 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 * 
 */
public class DefaultRPCServiceExporterFactory implements RPCServiceExporterFactory{

	private boolean responseCompressionEnabled;
	private SerializationPolicyProvider serializationPolicyProvider = new DefaultSerializationPolicyProvider();
	private int serializationFlags = AbstractSerializationStream.DEFAULT_FLAGS;
	private boolean shouldCheckPermutationStrongName = false;
	private ModulePathTranslation modulePathTranslation = new ModulePathTranslation() {
		
		public String computeModuleBaseURL(HttpServletRequest request,
				String moduleBaseURL, String strongName) {
			return moduleBaseURL;
		}
	};
	
	
	public void setModulePathTranslation(ModulePathTranslation modulePathTranslation){
		this.modulePathTranslation = modulePathTranslation;
	}
	
	
	public boolean isShouldCheckPermutationStrongName() {
		return shouldCheckPermutationStrongName;
	}

	/**
	 * Should RPC check the X-GWT-Permutation headers?
	 * @param shouldCheckStrongPermutationName
	 */
	public void setShouldCheckPermutationStrongName(
			boolean shouldCheckPermutationStrongName) {
		this.shouldCheckPermutationStrongName = shouldCheckPermutationStrongName;
	}

	public int getSerializationFlags() {
		return serializationFlags;
	}

	public void setSerializationFlags(int serializationFlags) {
		this.serializationFlags = serializationFlags;
	}

	public void setResponseCompressionEnabled(boolean responseCompressionEnabled) {
		this.responseCompressionEnabled = responseCompressionEnabled;
	}

	/**
	 * Constructor for the default Factory
	 */
	public DefaultRPCServiceExporterFactory(){
	}
	
	/**
	 * Returns a new instance of an {@link RPCServiceExporter}
	 * @return {@link RPCServiceExporter}
	 */
	public RPCServiceExporter create() {
		GWTRPCServiceExporter exporter = new GWTRPCServiceExporter();
		exporter.setCompressResponse(responseCompressionEnabled?GWTRPCServiceExporter.COMPRESSION_AUTO:GWTRPCServiceExporter.COMPRESSION_DISABLED);
		exporter.setSerializationPolicyProvider(serializationPolicyProvider);
		exporter.setSerializationFlags(serializationFlags);
		exporter.setShouldCheckPermutationStrongName(shouldCheckPermutationStrongName);
		exporter.setModulePathTranslation(modulePathTranslation);
		return exporter;
	}

	public SerializationPolicyProvider getSerializationPolicyProvider() {
		return serializationPolicyProvider;
	}

	public void setSerializationPolicyProvider(
			SerializationPolicyProvider serializationPolicyProvider) {
		this.serializationPolicyProvider = serializationPolicyProvider;
	}

}
