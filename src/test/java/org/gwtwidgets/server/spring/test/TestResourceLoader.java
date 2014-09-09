/* Licensed under the Apache License, Version 2.0 (the "License");
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

/**
 * Resource loader for JUnit tests
 * @author george georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public class TestResourceLoader extends FileSystemResourceLoader{

	private Log logger = LogFactory.getLog(getClass());
	
	Pattern serialisationPolicyPattern= Pattern.compile("(.*?)\\/static\\/(.*)");
	@Override
	public Resource getResource(String arg) {
		Matcher matcher = serialisationPolicyPattern.matcher(arg);
		boolean serializationPolicyFound = matcher.find();
		logger.debug("Looking up resource "+arg +" "+serializationPolicyFound);
		if (!serializationPolicyFound)
			return super.getResource(arg);
		String newResource = "target/webapp/static/" + matcher.group(2);
		logger.debug("Translating resource to "+newResource);
		return new FileSystemResource(newResource);
	}
}
