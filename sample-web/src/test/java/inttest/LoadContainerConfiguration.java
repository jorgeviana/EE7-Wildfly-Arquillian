package inttest;

import org.jboss.arquillian.config.descriptor.api.ContainerDef;
import org.jboss.arquillian.config.descriptor.api.ProtocolDef;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import static java.lang.String.valueOf;


// https://github.com/kaiwinter/testcontainers-examples/tree/master/wildfly-mariadb
// https://github.com/kaiwinter/testcontainers-examples/blob/master/wildfly-mariadb/src/test/java/com/github/kaiwinter/testsupport/arquillian/WildflyMariaDBDockerExtension.java
// https://github.com/kaiwinter/testcontainers-examples/blob/master/wildfly-mariadb/src/test/resources/arquillian.xml


// do we need to setup admin credentials in the image? Then create our own image!
// https://github.com/kaiwinter/wildfly10-mariadb/blob/master/Dockerfile

public class LoadContainerConfiguration {


	private static final String DOCKER_IMAGE = "jboss/wildfly";
	private static final int WILDFLY_HTTP_PORT = 8080;
	private static final int WILDFLY_MANAGEMENT_PORT = 9990;


	public void registerInstance(@Observes ContainerRegistry registry, ServiceLoader serviceLoader) {


		//RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent

		new GenericContainer(
				new ImageFromDockerfile()
						.withDockerfileFromBuilder(builder ->
								builder
										.from(DOCKER_IMAGE)
										.run("/opt/jboss/wildfly/bin/add-user.sh admin Admin#007 --silent")
										.cmd("/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0")
										.build()))
				.withExposedPorts(80);

		System.out.println("Before starting");
		GenericContainer dockerContainer = new GenericContainer(DOCKER_IMAGE)
				.withExposedPorts(WILDFLY_HTTP_PORT
						, WILDFLY_MANAGEMENT_PORT
				)
//				.withNetwork(Network.SHARED)
				// waiting for container to be ready, perhaps using a port instead of logs
				// this is not working now anyway as it is not possible to connect to container
				// possibly due to no admin (management) user configured
//				.waitingFor(
//						Wait.forLogMessage(".*Admin console listening on.*", 1))
				.waitingFor(
//						Wait.forHttp("/")
//								.forStatusCodeMatching(it -> it >= 200 && it < 400)
						Wait.forHttp("/")
								.forPort(WILDFLY_HTTP_PORT)
								.forStatusCodeMatching(it -> it >= 200 && it < 400)
				)
				;
		dockerContainer.start();
		System.out.println("After starting");

		System.out.println(dockerContainer.getBinds());




		Integer wildflyHttpPort = dockerContainer.getMappedPort(WILDFLY_HTTP_PORT);
		Integer wildflyManagementPort = dockerContainer.getMappedPort(WILDFLY_MANAGEMENT_PORT);
		String containerIpAddress = dockerContainer.getContainerIpAddress();
		System.out.println(wildflyHttpPort);
		System.out.println(wildflyManagementPort);
		System.out.println(containerIpAddress);

		Container arquillianContainer = getArquillianContainer(registry);
		ContainerDef containerConfiguration = arquillianContainer.getContainerConfiguration();
		containerConfiguration.property("managementAddress", containerIpAddress);
		containerConfiguration.property("managementPort", valueOf(wildflyManagementPort));

		// create this in own docker file !?
		containerConfiguration.property("username", "admin");
		containerConfiguration.property("password", "Admin#007");

		ProtocolDef protocolConfiguration = arquillianContainer
				.getProtocolConfiguration(new ProtocolDescription(ServletProtocolDefinition.NAME));
		protocolConfiguration.property(ServletProtocolDefinition.HOST_KEY, containerIpAddress);
		protocolConfiguration.property(ServletProtocolDefinition.PORT_KEY, valueOf(wildflyHttpPort));
	}

	private Container getArquillianContainer(ContainerRegistry registry) {
		return registry.getContainers().iterator().next();
	}


	public static final class ServletProtocolDefinition {
		/** Arquillian's name for the Servlet protocol. */
		public static final String NAME = "Servlet 3.0";

		/** The IP of the remote Wildfly server. */
		public static final String HOST_KEY = "host";

		/** The IP of the remote Wildfly (which is 8080 by default. */
		public static final String PORT_KEY = "port";
	}
}
