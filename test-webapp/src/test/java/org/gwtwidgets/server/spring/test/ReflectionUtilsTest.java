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
package org.gwtwidgets.server.spring.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.gwtwidgets.server.spring.ReflectionUtils;
import org.junit.Test;

import com.google.gwt.user.client.rpc.RemoteService;

import static org.junit.Assert.*
;/**
 * Test for #2035066 - ReflectionUtils does not search super classes for interfaces
 * @author Daniel Spangler
 */
public class ReflectionUtilsTest{

	public static abstract class MyAbstractTestClass {
		public abstract void doSomething();
	}

	public static class MyTestClass extends MyAbstractTestClass implements RemoteService {
		@Override
		public void doSomething() {
			System.out.println("I did something");
		}
	}

	public static class CGLibProxy implements MethodInterceptor {

		public static MyAbstractTestClass newInstance(final Class<?> clazz) {
			try {
				final Enhancer e = new Enhancer();
				e.setSuperclass(clazz);
				e.setCallback(new CGLibProxy());
				return (MyAbstractTestClass) e.create();
			} catch (Throwable e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}


		public Object intercept(Object target, Method method, Object[] arguments, MethodProxy proxy) throws Throwable {
			System.out.println("I intercepted something - " + method.getName());
			return method.invoke(target, arguments);
		}

	}

	@Test
	public void testGetInterfaces() {
		MyAbstractTestClass instance = CGLibProxy.newInstance(MyTestClass.class);
		assertTrue(instance instanceof RemoteService);

		assertTrue(ReflectionUtils.getExposedInterfaces(instance.getClass()).length == 1);
	}



}
