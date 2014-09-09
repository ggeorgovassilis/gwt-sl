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

package org.gwtwidgets.server.spring.test.domain;

import java.util.HashSet;
import java.util.Set;
/**
 * A product consists of an id,a name and a price.  
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 *
 */

public class Product extends BaseEntity{
	private String name;
	private double price;
	private Set<Discount> discounts = new HashSet<Discount>();
	
	public Set<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Set<Discount> discounts) {
		this.discounts = discounts;
	}

	public Product(){}
	
	public Product(String name, double price){
		setName(name);
		setPrice(price);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return getId()+":"+getName()+","+getPrice();
	}
}
