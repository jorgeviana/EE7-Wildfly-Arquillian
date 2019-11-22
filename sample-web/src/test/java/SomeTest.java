import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.TransitiveStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

//@RunWith(Arquillian.class)
public class SomeTest {

//	@Deployment(testable = false)
//	@SuppressWarnings("unused")
//	public static WebArchive createDeployment() {
//
//		PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
//		ScopeType[] scopes = {ScopeType.COMPILE}; // no SYSTEM and no PROVIDED
//		File[] libs = pom.importDependencies(scopes).resolve().using(TransitiveStrategy.INSTANCE).asFile();
//
//		return ShrinkWrap.create(WebArchive.class, "service-test.war")
//				.addAsLibraries(libs)
//				.addPackages(true, Filters.exclude(".*Test.*"), "com.hotjoe")
//				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//	}

//	@Test
//	@RunAsClient
//	public void testHeartbeat(@ArquillianResource URL baseURL) throws IOException {
//	}


	@Test
	public void name() {
	}
}
