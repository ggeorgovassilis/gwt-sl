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
package org.gwtwidgets.server.spring.test.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A product order consists of an ID and a set of {@link Product}s.  
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */

public class ProductOrder extends BaseEntity{

	/**
	 * This field is a List that must always contain Strings.
	 * 
	 */
	private List<Product> products = new ArrayList<Product>();

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
