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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used in unit tests in order to verify that methods from super-classes are still visible
 * to the RPC mapper. 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */
public class ExtendedTestServiceImpl extends TestServiceImpl{

	private Log logger = LogFactory.getLog(getClass());
	
	@Override
	public int add(int a, int b) {
		logger.info("Called add from extending service");
		return super.add(a, b);
	}
}
