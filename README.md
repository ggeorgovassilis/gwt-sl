Server Library for GWT- Reference Documentation v1.5
======

The GWT Server Library is a collection of Java server side components for the Google Web Toolkit AJAX framework with the current focus on the Spring framework by facilitating publishing of Spring beans as RPC services.

Features

* Binding POJOs to RPC in both annotation and XML driven declarations
* Servlet filters for manipulating HTTP headers
* Extensive documentation and high degree of maturity 

This project used to be hosted at Sourceforge as part of the [GWT Widget Library](http://gwt-widget.sourceforge.net/), later at [Google Code](https://code.google.com/p/gwt-sl/) and has now moved to [Github](https://github.com/ggeorgovassilis/gwt-sl).

## <a name="TOC"></a>0. Table of Contents

* [0. Table of contents](#TOC)
* [1. Changelog](#Changelog)
* [2. General overview](#Overview)
* [2.1. Quickstart](#Quickstart)
* [3. Publishing beans as RPC services](#RPC)
* [3.1 Publishing POJOs as services - `GWTRPCServiceExporter`](#GWTRPCServiceExporter)
* [3.2 Publishing multiple beans - `GWTHandler`](#GWTHandler)
* [3.3 Extending a base class - `GWTSpringController`](#GWTSpringController)
* [4. How to](#HowTo)
* [4.1 Exception translation](#ExceptionTranslation)
* [4.2 Accessing the Servlet API from inside a service](#ServletAPI)
* [4.4 Changing HTTP Headers](#HTTPHeaders)
* [4.5 Enhance RPC security (strong name permutation check)](#StrongNamePermutationCheck)
* [4.6 Decrease RPC payload size](#ObfuscatedTypeNames)
* [4.7 Do more things with Serialization Policies](#SerializationPolicyProvider)
* [4.8 Use multiple serialization policies](#MultipleSerializationPolicies)
* [5. FAQ](#FAQ)
* [I am seeing a `java.lang.NoClassDefFoundError` although the class/jar is there!](#Classloading)
* [My application runs on a 1.4 JRE and I'm getting a `ClassFormatError`](#java14)
* [I am getting a `java.lang.reflect.InvocationTargetException`](#InvocationTargetException)
* [RPC to my services don't work, I'm getting a `404` but no exception in the server log](#404)
* [How do I run the unit tests?](#Tests)
* [Why does my project not run with the hosted mode browser?](#Hosted)
* [RPCs are always returning the same objects to the client](#CachedRPC)
* [I am getting a `NullPointerException` in `GenericServlet`](#NoRPCInAppContext)
* [6. Links and Resources](#Resources)

## <a name="Changelog"></a>1. Changelog

#### GWT-SL 1.4

* Moved project to github
* Upgraded GWT and Spring dependencies
* Setup a public maven repository 
* Removed support for streaming

#### GWT-SL 1.3

* Breaking change: @GWTRequestMapping replaced with @RemoteServiceRelativePath (thanks Jens Rohloff)
* Removed support for Gilead
* Mavenized project


#### GWT-SL 1.2

* Moved project to google code https://code.google.com/p/gwt-sl/
* Updated project dependencies to Spring 3.0.5, GWT 2.1.1
* Allowing GWTRPCServiceExporter and GWTHandler (through DefaultRPCServiceExporterFactory) to use custom serialization policy providers
* Support for obfuscated RPC classifiers
* Support checking for X-GWT-Permutation headers (thanks Josh Drummond)
* Deprecated ServletUtils.setRequest, using Spring support classes not (requires use of DispatcherServlet)
* Breaking change: GWTRPCServiceExporter.encodeResponseForFailure has two new arguments

#### GWT-SL 1.1c

* GWTRPCServiceExporter implement Controller instead of RequestHandler
* Added processResponse method to GWTRPCServiceExporter which allows postprocessing of RPC payloads

#### GWT-SL 1.1b

* reduced loglevel in GileadRPCServiceExporter to DEBUG
* Implemented GileadRPCServiceExporterFactory
* Corrected comments in handler-servlet.xml

#### GWT-SL 1.1a

* Updated dependencies to GWT 2.0.3

#### GWT-SL 1.1

* Updated dependencies to GWT 2.0.0, Hibernate 3.3.2, Spring 3.0, Gilead 1.3
* Corrected mistakes in documentation

#### GWT-SL 1.0a

* Compile targets java 1.5 instead of 1.6
* Corrected example in documentation about setting up gilead with annotations
* GWTRPCServiceExporter & co now warn when declared in an application context instead of a servlet context.
* GWTRPCServiceExporter & co now call onAfterResponseDeserialized
* Fixed NPE in the GWTSpringController when no serialization policy was provided
* Included fix for serializing Gilead domain objects in exceptions

#### GWT-SL 1.0

* Annotation support added. Thanks to John Chilton.
* Added support for Gilead's dynamic proxy mode. Thanks to Bruno Marchesson.
* GWTRPCServiceExporter now throws undeclared exceptions to the servlet container. Can be switched off.
Thanks to David Durham.

* Upgraded dependencies:
Java 1.5
GWT 1.7
Gilead 1.2.2.598
jboss-serialization 1.0.3GA
trove 2.0.5

* Removed support for SerializationException, use regular Exceptions implementing java.io.Serializable.
* Fix for #2711426 (NullPointerException during some error reporting & reloads)
* GileadRPCServiceExporter used to always create servlet sessions for the stateless proxy store.
Added new setter setCreateSessionIfNotExists which allows for disabling creation of new sessions.
* Added GileadRPCServiceExporterFactory for use with GWTHandler.
* Fixed typo in 0.1.5b changelog

#### GWT-SL 0.1.5b

* further work on stream protocol
* further fix for 2035066 - thanks to Daniel Spangler
* hibernate4gwt replaced with gilead
* added transaction support to hb4gwt example
* upgraded dependencies:
gwt 1.5.3
spring 2.5.6
hibernate 3.3.1
junit 4.4
log4j 1.2.15
hibernate4gwt replaced with gilead 1.2.029
commons logging 1.1.1
aspectj 1.6.2
commons collections 3.2
added javassist 3.4GA, new dependency for hibernate
added slf4j 1.5.0, new dependency for hibernate

* new ant target "all" - alias of "package"
* eased dependencies in tests to work with SL interfaces

#### GWT-SL 0.1.5a

* Fixed example of GWTRPCServiceExporter in documentation, thanks to Martin Konzett for spotting
* Updated dependencies to Hibernate4gwt 1.1b, thanks to Bruno Marchesson for hints on the HB4GWTRpcServiceExporter
* Updated dependencies to GWT-1.5.2
* Removed javassist dependency from demo project
* Added unit tests for verification of correct RPC mapping of service that extend other service classes
* Fixed bug in client tests that would omit calls to certain services

#### GWT-SL 0.1.5

* Added switch to RPC components for disabling response caching
* Deprecated request/response getters in GWTSpringController
* Added ResponseHeaderFilter for setting HTTP response headers.
* Updated dependencies to GWT 1.5 RC1, hibernate4gwt 1.0.4, spring 2.5
* Removed Java 1.4 support
* Removed methods which were deprecated in previous versions from GWTRPCServiceExporter and GWTHandler
* Fixed bug in handling of serialisable exceptions in conjunction with serialisation policy, credits to
Robert Schreiber for spotting and providing the fix.
* Added some sanity checks to catch configuration mistakes
* GWTRPCServiceExporter will log now only unchecked exceptions
* Fixed documentation (thanks to Martin Konzett)

#### GWT-SL 0.1.4e

* Bug fix in GWTHandler: The service exporter fix in 0.1.4d was revised. A RPCServiceExporterFactory
is now used to generate RPCServiceExporter instances. Credits to Andrew McAllister for reporting.
* The NoSuchMethodException which is thrown when a requested method cannot be found on the service
not produces a more descriptive message.
* Fixed serialisation policy generation issue with unit tests
* Fixed documentation bugs

#### GWT-SL 0.1.4d

* Changed service exporter injection in GWTHandler from FactoryBean to RPCServiceExporter, since
there was no easy way of setting FactoryBeans and provided a necessary refactoring for
GWTRPCServiceExporter to implement an interface. Thanks to Ed for spotting this problem
and providing the solution.
* added unittest for hibernate4gwt.
* updated hibernate4gwt to 1.0.3, spring to 2.0.8
* fixed unit test
* fixed comments in web.xml
* updated build script
* fixed copyright notices
* restructured build process

#### GWT-SL 0.1.4c

* Updated dependencies to Spring v2.0.6
* Refactored GWTRPCServiceExporter for easier exception translation
* Refactored GWTHandler to support GWTRPCServiceExporter object factories
* Expanded documentation on exception translation and deployment FAQ
* Added support for Hibernate4GWT
* Reverted copyright notice to plain Apache License 2
* GWTHandler is no longer a FactoryBean - caused semantics mixup when requesting "urlMapping" from application context
* Added new tests
* GWTRPCServiceExporter caches now reflective method lookups for a moderate gain in speed
* Depreciated GWTRPCServiceExporter.getRequest/getResponse
* Added setter for compression logic to GWTRPCServiceExporter

#### GWT-SL 0.1.4b

* Incorporated patch from Max Jonas Werner to GWTRPCServiceExporter which handles
java.io.Serializable Objects in RPC


#### GWT-SL 0.1.4

* Removed javassist, cglib and the concept of runtime-weaving in general
* Made exception wrapping default for all components
* Duplicated 'mapping' (deprecated) as 'mappings'
* Introduced GWTRPCServiceExporter which publishes real (interface-less) POJOs as RPC services
* Removed the usingInstrumentation property
* Switched entirely to commons logging
* Changed project structure
* Added regular documentation
* Improved building
* Logging more information in debug level
* Refined method inspection
* Rationalised unit tests
* Fixed ReflectionUtils (thanks to Dmitri Shestakov) to compile for 1.4 JDK

#### GWT-SL 0.1.3 b

* Fixed the build script to build demo WAR without the duplicate GWL-SL classes in both the WEB-INF/classes and gwt-sl.jar
* defaulted the build do produce 1.5 compatible classes
* added proper log4j setup to demo WAR


#### GWT-SL 0.1.3

New features:

* Added GWTController which delegates requests to encapsulated services.
* Reworked the examples
* Reworked the build process
* Added build target which builds java 1.4 jars with 1.5 JDKs
* Added changelog
* Refactored GWTSpringController to use some common code with the new GWTController
* Added ServletUtils helper class which allows access to the invoking thread's ServletRequest and ServletResponse
* GWTHandler by default enables now SerializableException translation
* Added Apache License 2.0 Headers where missing

## <a name="Overview"></a>2. General Overview

The Server Library for GWT (in short SL) is a collection of Java server side components for the Google Web Toolkit AJAX framework
with the current focus on the Spring framework by facilitating publishing of Spring beans as RPC services. The main
binary dependencies are GWT 2.6.1 and Spring 3.2.x (older versions may work too).

The Spring Framework is an established component framework for web applications that span
authentication, database access and complex page flow. Through its aspect oriented approach
it is unobtrusive and cleanly separates the presentation layer from the business logic and
the data model, allowing for back end services which are agnostic towards the way the
presentation is rendered. This is the ideal base for a GWT application, which also separates the
presentation (widgets that run in the browser) from the business logic (RPC services
running on the web server) from the data model (the objects serialized over RPC).

GWT binds java methods to RPC calls by using the Servlet API so that
each service you write is a servlet.The Servlet API however is rather crude and the servlet
container (like Tomcat) is a gross environment providing little assistance to elaborate tasks
like transaction management, AOP tasks (authentication, logging, per-task caching) etc. Also
the notoriously scarce configuration abilities are by far inferior to Spring's XML
configuration and bean injection which allows even the most complex configurations by
plugging objects together in XML. With the SL you can easily write Spring managed beans
which act as GWT services, taking full advantage of both frameworks.

### <a name="Quickstart"></a>2.1. Quickstart

Get it with maven by specifying first the GWT-SL repository:
```xml
<repositories>
	<repository>
		<id>gwt-sl-mvn-repo</id>
		<url>https://raw.github.com/ggeorgovassilis/gwt-sl/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>
```

And this dependency:

```xml
<dependency>
	<groupId>net.sf.gwt-widget</groupId>
	<artifactId>gwt-sl</artifactId>
	<version>1.5</version>
</dependency>
```
We'll briefly cover the steps necessary to publish a simple Spring managed bean so that a GWT client can communicate with it over RPC. There are several ways of
exporting a bean to RPC, here we shall follow the simplest one, namely the `GWTHandler`. Details follow in [Chapter 3](#RPC).

We start off with the definition of the service interface. Our service will perform a simple arithmetic addition. Since
the client application running in a browser will access this interface also (its Async version, to be precise), the service interface must extend GWT's
`RemoteService` interface.

ServiceTest.java :

```java
package org.gwtwidgets.server.spring.test.server;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ServiceTest extends RemoteService{
  public int add(int a, int b);
}
```

The implementation is free of any dependencies on Spring, GWT or the servlet API.

ServiceTestImpl.java :

```java
package org.gwtwidgets.server.spring.test.serverimpl;
import org.gwtwidgets.server.spring.test.server.ServiceTest;

public class ServiceTestImpl implements ServiceTest{

  public int add(int a, int b) {
    return a + b;
  }
}
```

This is a Spring application, so we load the Spring servlet which will export our service bean under the url `http://localhost:8080/gwtWebApp/handler/rpctest`

web.xml :

```xml
<web-app>

<!--
Mapping an RPC service defined in handler-servlet.xml
-->

<servlet>
  <servlet-name>handler</servlet-name>`
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>handler</servlet-name>
  <url-pattern>/handler/*</url-pattern>
</servlet-mapping>

</web-app>
```

Last, we define a URL handler mapping like we'd do with any regular Spring MVC application, only that we won't use a `SimpleUrlHandlerMapping`
but a `GWTHandler` which knows how to translate RPC calls to method invocations on POJOs.

handler-servlet.xml :

```xml
<beans>

  <bean class="org.gwtwidgets.server.spring.GWTHandler">
    <property name="mappings">
      <map>
        <entry key="/rpctest" value-ref="RPCTest" />
    </map>
    </property>
  </bean>

  <bean id="RPCTest" class="org.gwtwidgets.server.spring.test.serverimpl.ServiceTestImpl" />

</beans>
```

## <a name="RPC"></a>3. Publishing beans as RPC services

The SL creates RPC services following different implementation strategies, from extending a base
class similar to the default way services are created in GWT to publishing POJOs as services without
dependencies on any class, interface or even annotation. It is common to all approaches that eventually
a `RemoteServiceServlet` is generated (usually at runtime) automatically which delegates
RPCs to method invocations to a specified service bean. The following sections describe the different
approaches towards creating services which can be used alone or together.

There are always three common steps and you always need a service interface, an asynchronous callback version
of that interface and a service implementation.

*   Create the service interface A, the `RemoteService` and `AsyncCallback` interfaces from the service interface
*   Create a service implementation of A
*   Use one of the RPC publishing methods to expose your service implementation
Note that nothing changes on the client, regardless of which way you use to expose a service to RPC.
The client does not notice whether you use plain GWT, the SL or even a PHP backend that simulates the RPC
protocol.<p/>
Without Spring and the SL, a regular RPC request is processed by GWT similar to this figure:<p/>
<table>
<tr class="inBrowser"><td>1.</td><td colspan="2">Browser encodes objects to RPC string</td></tr>
<tr class="inBrowser"><td>2.</td><td colspan="2">Browser makes HTTP request to the designated RPC URL on the server</td></tr>
<tr class="inServlet"><td/><td>3.</td><td>Web server forwards the HTTP request to the web application which is bound to that URL</td></tr>
<tr class="inServlet"><td/><td>4.</td><td>Based on the servlet mapping in `web.xml` the request is routed to the corresponding servlet</td></tr>
<tr class="inServlet"><td/><td>5.</td><td>The `RemoteServiceServlet` decodes the RPC payload and invokes the service method</td></tr>
<tr class="inServlet"><td>6.</td><td colspan="2">The service response is encoded as an RPC response payload and send as an HTTP response back to the browser</td></tr>
<tr class="inBrowser"><td>7.</td><td colspan="2">Browser decodes the RPC payload and invokes the service callback</td></tr>
</table>
<p/>
Now, with Spring and the SL, there are a few more steps which are performed by the framework:<p/>
<table>
<tr class="inBrowser"><td>1.</td><td colspan="3">Browser encodes objects to RPC string</td></tr>
<tr class="inBrowser"><td>2.</td><td colspan="3">Browser makes HTTP request to the designated RPC URL on the server</td></tr>
<tr class="inServlet"><td/><td>3.</td><td colspan="2">Web server forwards the HTTP request to the web application which is bound to that URL</td></tr>
<tr class="inServlet"><td/><td>4.</td><td colspan="2">Based on the servlet mapping in `web.xml` the request is routed to the corresponding servlet</td></tr>
<tr class="inSpring"><td/><td>5.</td><td colspan="2">Spring routes the request based on its mapping (i.e. the `SimpleUrlHandlerMapping`) to the corresponding controller</td></tr>
<tr class="inSL"><td/><td/><td>5.1</td><td>SL's `GWTRPCServiceExporter` decodes the RPC request into Java objects</td></tr>
<tr class="inSL"><td/><td/><td>5.2</td><td>The `GWTRPCServiceExporter` forwards the request to a specified service bean</td></tr>
<tr class="inSL"><td/><td/><td>5.3</td><td>The `GWTRPCServiceExporter` encodes the service bean's response into an RPC payload</td></tr>
<tr class="inSpring"><td/><td>6.</td><td colspan="2">Spring sends the response back to the servlet container and back to the Browser</td></tr>
<tr class="inBrowser"><td>7.</td><td colspan="4">Browser decodes the RPC payload and invokes the service callback</td></tr>
</table>

## <a name="GWTRPCServiceExporter"></a>3.1 Publishing POJOs as services - GWTRPCServiceExporter

The `GWTRPCServiceExporter` is the purist's take at developing services since it does not introduce any API and compile
time dependency to your service. Essentially it is a wrapper which exports any POJO as an RPC service. In order for the
`GWTRPCServiceExporter` to know which methods to bind to RPC, the service bean should either implement a `RemoteService`
interface or you should provide it in the configuration. If no interface is specified, all methods are published to RPC
which should be avoided - even fundamental methods like `wait()` can provide backdoors for various denial of service
attacks.

Let's create a simple bean which we will later publish as a service. This example is taken from the unit
test web application and it shows a trivial bean with a method that adds two integers. Note that
the bean is really a POJO, it does not extend or implement any class or interface.

```java
package org.gwtwidgets.server.spring.test.serverimpl;

public class ServiceTestPOJO {

public int add(int a, int b) {
  return a + b;
}
...
}
```

Next we need an interface which extends `RemoteService` and declares the methods which should be exposed
to RPC. Since in our example we have only one, this is an easy task:

```java
package org.gwtwidgets.server.spring.test.server;

public interface ServiceTest extends RemoteService{

  public int add(int a, int b);

}
```

That leaves only the correct bean creation and URL mapping in the Spring servlet XML:

```xml
<!--
Create POJO version of the Test service: implements only methods but does not expose any RPC interfaces
-->

<bean id="ServiceTestPOJO" class="org.gwtwidgets.server.spring.test.serverimpl.ServiceTestPOJO" />

<!--
Declaration of RPC service with the RPCServiceExporter which behaves very similar to
a Spring controller and needs to be mapped to URLs the same way any other Spring
controller is mapped...
-->

<bean id="RPCTestPOJO" class="org.gwtwidgets.server.spring.GWTRPCServiceExporter">

<!--
Reference to the service bean which should be exported via RPC to the web.
-->

  <property name="service" ref="ServiceTestPOJO" />

<!--
If our Test service was not a 100% pure POJO but also implemented the ServiceTest interface then
we wouldn't have to specify it here. Note that you can provide multiple interface names, as long as
your service has the corresponding methods with a matching signature.
-->

  <property name="serviceInterfaces">
    <value>
      org.gwtwidgets.server.spring.test.server.ServiceTest
    </value>
  </property>
</bean>

<!--
... with a SimpleUrlHandlerMapping for instance:
-->

  <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    <property name="mappings">
      <map>
        <entry key="/service" value-ref="RPCTestPOJO" />
      </map>
    </property>
  </bean>
```

## <a name="GWTHandler"></a>3.2 Publishing multiple beans - GWTHandler

The `GWTHandler` allows you to quickly map multiple RPC service beans to different URLs
very similar to the way Spring's `SimpleUrlHandlerMapping` maps URLs to controllers. The
mapped beans are internally wrapped into `[GWTRPCServiceExporter](#GWTRPCServiceExporter)`
instances, with the notable difference that you cannot specify a service interface in the configuration
and the service beans must implement the `RemoteService` interface (as a matter of fact there is
a workaround even for that by providing your own implementation of a `RPCServiceExporter`).

First we need the service interface from our previous example:

```java
package org.gwtwidgets.server.spring.test.server;

public interface ServiceAdd extends RemoteService{

  public int add(int a, int b);

}
```

And the corresponding implementation:

```java
package org.gwtwidgets.server.spring.test.serverimpl;

public class ServiceAddImpl implements ServiceAdd{

public int add(int a, int b) {
  return a + b;
}

}
```

Finally the mapping:

```xml
<bean id="urlMapping" class="org.gwtwidgets.server.spring.GWTHandler">

<!-- Supply here mappings between URLs and services. Services must implement the RemoteService interface but
are not otherwise restricted.-->
  <property name="mappings">
    <map>

<!-- Other mappings could follow -->
      <entry key="/add.rpc" value-ref="ServiceAdd" />
    </map>
</property>
</bean>

<!-- Declare service -->
<bean id="ServiceAdd" class="org.gwtwidgets.server.spring.test.serverimpl.ServiceAddImpl" />

```

Annotations can also be used to publish services to URLs. Beans in the current application context
featuring the `GWTRequestMapping` annotation on their service interface will be picked
up by the `GWTHandler` and mapped to the specified URL. Please note that the URL is relative
to URL which the `GWTHandler` is already servicing:

```xml
<beans>
<bean class="org.gwtwidgets.server.spring.GWTHandler"/>

<bean id="annotatedService" class="org.gwtwidgets.server.spring.test.serverimpl.AnnotatedServiceTestImpl"/>

</beans>

@GWTRequestMapping("/service")
public interface AnnotatedService extends ServiceTest{

}
```

You can optionally export annotated beans from a parent application context by enabling the
`scanParentApplicationContext` property.

A sidenote on method interceptors and AOP advice: advice applied on the `GWTHandler` is
applied on the servlet request/response level and not the method invocation level of a particular service.
In this case it is applied before/after/around the RPC request handling and not around the service method
invocation. You should carefully distinguish between advice applied on the `GWTHandler` (i.e. access
authorisation based on session attributes) and advice applied on services (i.e. logging or transaction management).

If you require a different functionality (for example with exception translation) to be
wrapped around your services than the one provided by the default `GWTRPCServiceExporter` which is used
by the `GWTHandler` you can inject your own implementation of a `RPCServiceExporter` by providing
a `RPCServiceExporterFactory`.

## <a name="GWTSpringController"></a>3.3 Extending a base class - GWTSpringController

The `GWTSpringController` is the SL's oldest yet best-performing approach towards integrating GWT with Spring
and it requires an RPC service to extend the `org.gwtwidgets.server.spring.GWTSpringController` class.
While you are usually better off with any of the previous strategies, the `GWTSpringController` is lighter
than the other implementations, relies less on reflection and consumes less processing time.

The service extends `GWTSpringController`:

```java
package org.gwtwidgets.server.spring.test.serverimpl;

public class ControllerAdd extends GWTSpringController implements ServiceAdd{

public int add(int a, int b) {
  return a+b;
}
}
```

And the mapping:

```xml
<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
<property name="mappings">
<map>
<entry key="/add.rpc" value-ref="ControllerAdd" />
</map>
</property>
</bean>

<bean id="ControllerAdd" class="org.gwtwidgets.server.spring.test.serverimpl.ControllerAdd"/>
```

## <a name="HowTo"></a>4. How to

In this chapter we will discuss common cases in a Spring web application and how the SL
deals with them - yet, since it uses the basic Spring building blocks and conventions we hope
that you will find little surprises and hardly any unexpected behaviour.

## <a name="ExceptionTranslation"></a>4.1 Exception translation

The term Exception translation in general refers to the task of converting one exception
instance to an other of a different class as the program execution passes through different application
layers which nest the original exception in an exception relevant to the current layer. Exceptions
are usually nested so that the root cause lies deep in the stack trace while the exception at the
bottom of the stack trace is an entirely different one, generated by the various program layers the
method invocation went through.

By default the `GWTHandler` and the `GWTRPCServiceExporter` unwrap the immediate root
cause of any exception and propagate it back to the client.
In this regard they behave slightly different from previous versions which would propagate the first
`SerializableException` found in the stack trace to the client, regardless how deep in the
stack trace it was.

Exception translation is important for RPC services because only `SerializableException`s are
propagated through the RPC protocol back to the client and all other exceptions will usually result in
terminating the current request with the client not getting any meaningful message. In previous versions
you could switch off exception translation, now this it possible by overriding methods in the
`GWTRPCServiceExporter`.

`SerializableException` is a checked exception and as such it does not cause
transactions in the exception-throwing service to be rolled back. This is best tackled either from
inside the service implementation (since you may want the transaction to be committed) or by declaring
the conditions under which a transaction should be rolled back by the transaction proxy - for details on
how to attain both please consult the
[Spring Reference Manual](http://static.springframework.org/spring/docs/2.0.x/reference/transaction.html#transaction-declarative).

In v0.1.4c the `GWTRPCServiceExporter` was refactored to move exception handling into
separate methods. By extending `GWTRPCServiceExporter` and overriding these methods you can
influence substantially the way target services are invoked and the way exceptions are handled.

For instance, imagine that you are trying to handle exceptions in your Spring applications in a common way.
You want to catch all exceptions before they reach the console (or the user), possibly filter out the long traces that
are often introduced by runtime class weaving (such as with CGLIB), filter out `InvocationTargetException`
traces which are appended to exceptions during reflective invocation and want to repackage these exceptions
into other, more friendly and application specific exceptions. You would do that with the Spring AOP or a
`MethodInterceptor`, catch there any exceptions, inspect them and throw new exceptions.

The exceptions you throw from within the advice would be `RuntimeException`s, because they cause
Springs transaction management to roll-back by default any transactions and because they do not violate the
signature of the invoked method. If the last statement looks puzzling, think of this scenario:

```java
class MathService {

public double divide(double a, double b){
  return a / b;
}
}
```

And an obvious advice:

```java
class DivisionByZero extends Exception{
}

class MathAdvice {

public Object doAround(Object target, Method method, Object[] args){
  double b = (double)args[1];
  if (b == null) throw new DivisionByZero();
    return method.invoke(target, args);
  }
}
```

This means trouble: although you have a `MathService` instance at hand (more precicely, an advised
proxy - but that is not the invoker's concern), calling `divide(3,0)` will throw a checked
exception, namely `DivisionByZero`. If `DivisionByZero` was a `RuntimeException`
the call would be fine, but now `divide`'s method signature contract is violated. Because the JVM
will not allow that, it will wrap the exception into an
[UndeclaredThrowableException](http://java.sun.com/j2se/1.4.2/docs/api/java/lang/reflect/UndeclaredThrowableException.html)
which is a `RuntimeException` and does not violate the method signature when thrown. But you may have
your reasons for throwing checked exceptions (i.e. because you don't want a transaction to be rolled back) the
`GWTRPCServiceExporter` can be overridden to handle exceptions. The particular case discussed can be
tackled by catching an `UndeclaredThrowableException` and propagating the enclosed exception to the
client :

```java
public class MyGWTRPCServiceExporter extends GWTRPCServiceExporter{

@Override
protected String handleUndeclaredThrowableException(Exception e, Method targetMethod, RPCRequest rpcRequest) throws Exception {
  Throwable cause = e.getCause();
  String failurePayload = RPC.encodeResponseForFailure(rpcRequest.getMethod(), cause);
  return failurePayload;
}
}
```
## <a name="ServletAPI"></a>4.2 Accessing the Servlet API from inside a service

While not 100% in tune with the MVC pattern, it is often convenient to access the servlet
container, the HTTP session or the current HTTP request from the business layer. The SL
provides several strategies to achieve this which pose a compromise in the amount of configuration
required to set up and the class dependencies introduced to the business code.

The easiest way to obtain the current HTTP request is by using the `ServletUtils` class
which provides convenience methods for accessing the `HttpServletRequest` and
`HttpServletResponse` instances. Please note that it makes use of thread local variables
and will obviously not return correct values if used in any other than the invoking thread.

A simple example:

```java
package org.gwtwidgets.server.spring.test.serverimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

public class ServiceTestPOJO {

...
public String replaceAttribute(String name, String value) {
  HttpSession session = ServletUtils.getRequest().getSession(true)
  String oldValue = (String) session.getAttribute(name);
  session.setAttribute(name, value);
  return oldValue;
}
}
```

It is also possible to inject the request/response pair in an IoC manner, when compile-time
dependency on the SL API is not desired. This approach sets the request and response to the service
as a thread local variable. Thus, the service implementation could look similar to:

```java
package org.gwtwidgets.server.spring.test.serverimpl;

public class ServiceHttpRequestTestPOJOImpl{

private static ThreadLocal`<HttpServletRequest>` servletRequest = new ThreadLocal`<HttpServletRequest>`();

private static ThreadLocal`<HttpServletResponse>` servletResponse = new ThreadLocal`<HttpServletResponse>`();

...

// These two setters...
public void setRequest(HttpServletRequest request) {
  servletRequest.set(request);
}

// ... must be public
public void setResponse(HttpServletResponse request) {
  servletResponse.set(request);
}

protected HttpServletRequest getRequest() {
  return servletRequest.get();
}

// That's a service method exposed to RPC
public String test2(String newValue) {
  HttpSession session = getRequest().getSession();
  return (String) session.getAttribute(attrName);
}

}
```

Now we have to create the bean that will perform the actual injection of the servlet request and response
and glue it to the service:

```xml
<!-- Target service bean -->
<bean id="ServiceTestHTTPTarget" class="org.gwtwidgets.server.spring.test.serverimpl.ServiceHttpRequestTestImpl" />

<!-- Create request setter -->

<bean id="requestSetter" class="org.gwtwidgets.server.spring.RequestInjection">
<property name="requestSetterName" value="setRequest"/>
<property name="responseSetterName" value="setResponse"/>
</bean>

<!-- Create Servlet API aware service -->
<bean id="ServiceTestHTTP" class="org.springframework.aop.framework.ProxyFactoryBean">
<property name="target" ref="ServiceTestHTTPTarget"/>
<property name="autodetectInterfaces" value="true"/>
<property name="interceptorNames">
<list>
<value>requestSetter</value>
</list>
</property>
</bean>
```


## <a name="HTTPHeaders"></a>4.4 HTTP Headers

Tweaking HTTP headers is a useful technique when it comes to performance tuning your GWT application. The SL
contains the `ResponseHeaderFilter` which is a servlet filter used to alter response headers.
It was inspired by [an article](http://www.onjava.com/pub/a/onjava/2004/03/03/filters.html)
written by Jason Falkner which discusses many interesting applications of HTTP headers.

`init-param` entries for the `ResponseHeaderFilter` are HTTP header names and values and are not further interpreted,
except for the special parameter `ResponseHeaderFilter.UrlPattern` which can be used to specify a regular expression which,
when specified, applies headers only to the subset of request URLs which match the provided pattern.

Some HTTP headers describe the validity period of a web resource and can be used to enable caching of resources in a browser.
In an GWT application this will usually be the `.cache.html`, the module entry HTML page, CSS and images.

To enable caching for a resource, something along the lines of

```xml
<filter>
  <filter-name>CachingFilter</filter-name>
  <filter-class>org.gwtwidgets.server.filters.ResponseHeaderFilter</filter-class>
  <init-param>
    <param-name>Expires</param-name>
    <param-value>Sun, 17 Jan 2038 19:14:07 GMT</param-value>
  </init-param>
</filter>
```

could be used in `web.xml`

A further interesting application is compressing of static resources such as scripts and static HTML pages. These do not have to be
compressed again for every request but can reside in an already compressed format on the file system. It then suffices to add the appropriate
headers to the HTTP response in order to instruct the browser to inflate the transmitted resource:

```xml
<filter>
  <filter-name>GzipFilter</filter-name>
  <filter-class>org.gwtwidgets.server.filters.ResponseHeaderFilter</filter-class>
  <init-param>
    <param-name>Content-Type</param-name>
    <param-value>text/html; charset=utf-8</param-value>
  </init-param>
  <init-param>
    <param-name>Content-Encoding</param-name>
    <param-value>gzip</param-value>
  </init-param>
  <init-param>
    <param-name>ResponseHeaderFilter.UrlPattern</param-name>
    <param-value>.*?\.html</param-value> <!-- Match only *.html URLs -->
  </init-param>
</filter>
```

## <a name="StrongNamePermutationCheck"></a>4.5 Enhance RPC security (strong name permutation check)

A few versions ago GWT's `RemoteServiceServlet` started checking certain `X-GWT-` headers
generated by the client ([more](https://groups.google.com/group/google-web-toolkit/web/security-for-gwt-applications)). As this is
a breaking change with ealier SL versions, this check can be enabled optionally in the `GWTRPCServiceExporter` (and extending classes) or `GWTHandler`
with the `shouldCheckStrongPermutationName` flag.

## <a name="ObfuscatedTypeNames"></a>4.6 Decrease RPC payload size

If you've ever inspected an RPC payload you would notice that it contains the complete class names of the DTOs used. GWT also has a not-so-well
known generator which can produce payloads without these class names, significantly reducing the payload. In order to use it you must:

*   Inherit `com.google.gwt.user.RemoteServiceObfuscateTypeNames` in your module xml
*   Use a serialization policy (many SL-users conviniently don't, so please double check)
*   Set the correct serialization flag (`GWTRPCServiceExporter.setSerializationFlags()`) on your exporter, which currently (GWT 2.1.1) is `AbstractSerializationStream.FLAG_ELIDE_TYPE_NAMES`

## <a name="SerializationPolicyProvider"></a>4.7 Do more things with Serialization Policies

As of 1.2 you can implement the SL `SerializationPolicyProvider` interface (not the GWT one!) and inject it
into your favorite flavour of RPCServiceExporters. Your implementation will then be consulted for each and every
request about the serialization policy to use.

## <a name="MultipleSerializationPolicies"></a>4.8 Use multiple serialization policies

In case you'd like to use multiple serialization policies at the same time, I strongly recommend Alex Moffat's excelent
[article](http://jectbd.com/?p=1174) on that matter which also provides ready to be used code for your use case.

## <a name="FAQ"></a>5. FAQ

## <a name="Classloading"></a>I am seeing a `java.lang.NoClassDefFoundError` although the class/jar is there!

The most common cause of this error is that not all related classes are loaded by the same class loader. If, for example, the spring library,
GWT, CGLib and your project classes are partially deployed in WEB-INF/lib and common/lib a `NoClassDefFoundError` may be thrown.
This is a configuration detail of the concrete servlet container you are using and is often solved by placing all libraries into the same location,
though that may not always be easy because of your web server's dependencies. A very good overview of how class loading is dealt with can be read
at the [Commons Logging](http://jakarta.apache.org/commons/logging/tech.html) project site.

## <a name="InvocationTargetException"></a>I am getting a `java.lang.reflect.InvocationTargetException`

`InvocationTargetException`s are envelopes that contain other exceptions - it occurs when an exception is thrown in a method
which is invoked via reflection and it is the most common exception you will see when your GWT services throw unintentionally an exception.
This is because RPC joins object methods with HTTP requests by relying on method invocation through reflection. The true cause of the exception
then lies deeper in the stack trace and is nested inside the `InvocationTargetException`. You may also want to read up on
[Exception Translation](#ExceptionTranslation) which discusses the special case of not treating `SerializableException`s
as exceptions but propagating them properly back to the client.

## <a name="java14"></a>My application runs on a 1.4 JRE and I'm getting a `ClassFormatError`

Since 0.1.5 the SL requires Java 1.5. If you have to use Java 1.4, consider using a 0.1.4 release of the SL which
includes Java 1.4 support. For this, please consult the corresponding documentation in the 0.1.4 SL.

## <a name="404"></a>RPCs to my services don't work, I'm getting a `404` but no exception in the server log

If you can rule out any other errors and you are not seeing any exceptions in the server log (let's supposed that logs are working
OK) your URL mappings may be incorrect. The SL hosts multiple RPC services under one or more Spring managed servlets, thus a good
start is to check the URL mappings in `web.xml`:

```xml
<servlet>
  <servlet-name>myRPCServices</servlet-name>
  <servlet-class>
  org.springframework.web.servlet.DispatcherServlet
  </servlet-class>
  <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
  <servlet-name>myRPCServices</servlet-name>
  <url-pattern>/myRPCServicesURL/*</url-pattern>
</servlet-mapping>
```

This maps `/myRPCServicesURL/...` to a spring servlet which is defined in `myRPCServices-servlet.xml`.
Note that the `/myRPCServicesURL` part of the URL is already 'consumed' by the servlet container and you should not duplicate it in
the subsequent Spring mappings:

```xml
<bean id="urlMapping" class="org.gwtwidgets.server.spring.GWTHandler">
<property name="mappings">
<map>
<-- Attention: We don't repeat the servlet URL, so it's not /myRPCServicesURL/service1 but just /service1 -->
<entry key="/service1" value-ref="FirstServiceBean" />
<entry key="/service2" value-ref="SecondServiceBean" />
<entry key="/service3" value-ref="ThirdServiceBean" />
...
</map>
</property>
</bean>
```

## <a name="Tests"></a>How do I run the unit tests?

Very basic stand-alone unit test can be run with `ant test`. In order to run the demo unit test web application,
first compile it with `ant war` and deploy it to a servlet container, it is then accessible under `http://myServer:port/gwt-sl`.
It runs a series of automated tests and should conclude with 'Finished tests' and no error messages.

## <a name="Hosted"></a>Why does my project not run with the hosted mode browser?

The short answer: understand and use the hosted browser's `-noserver` option.

Some background: The embedded tomcat server that comes with the hosted mode browser is intended as a simple aid
when making your first steps with GWT, so that you can focus on client development and so that you do not
have to worry about server configuration. An applications's ability to run with the hosted mode browser is not
affected by the SL, simply because the SL is a server side framework, while the browser runs the client
side portion of the application code.
Where problems do occur is when you run the browser together with the embedded tomcat server: your application
requires Spring and a mapping of URLs to the RPC services to be set up, but the embedded tomcat starts with
its own configuration instead of yours. Hence it is simpler to setup your own development server, as
you would do with every traditional web application and treat the GWT compiler output as a bunch of static
resources. For an example of such a setup run the `package` task of `build.xml` and
inspect the contents of the `target/webapp` directory or check the equivalent demo WAR in the
project's download area (sources are in `src/test`).

## <a name="CachedRPC"></a>RPCs are always returning the same objects to the client

An RPC is nothing but an HTTP request to the server - depending on the HTTP headers returned by the server's response
it can happen that the browser decides that the response can be cached. This means that future similar requests (with the same URL)
may be served from the browser's cache. The best way to avoid caching RPC responses is by modifying the HTTP headers of the response
in such a way that browser caching is disabled - for an example of how to do this consult the [HTTP Headers](#HTTPHeaders) chapter.
An alternative solution is to make sure that each request is dispatched to an unique URL, i.e. by appending to each RPC request a parameter
with a random, unique value.

Also note that the SL's RPC implementations (see chapter 3) attempt to disable response caching by default.

## <a name="NoRPCInAppContext"></a>I am getting a NullPointerException in GenericServlet

Don't export services in the application context, always export them in a `-servlet.xml` file. GWT's RPC mechanism expects
a servlet environment into which it logs information. The application context does not provide this
environment (namely the serlvet configuration), which causes `GWTRPCServiceExporter` instances
declared in the application context to lack the servlet configuration and thus procude NPEs.

## <a name="Resources"></a>6. Links and Resources

1. Project homepage [https://github.com/ggeorgovassilis/gwt-sl](https://github.com/ggeorgovassilis/gwt-sl)

2. User group [http://groups.google.com/group/gwt-sl/topics](http://groups.google.com/group/gwt-sl/topics)

3. Spring project homepage [http://spring.io/](http://spring.io/)

4. GWT homepage [http://www.gwtproject.org/](http://www.gwtproject.org/)

5. Inline GWT serialized data for reduced page load time [http://jectbd.com/?p=1174](http://jectbd.com/?p=1174)