import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.restassured.RestAssured.get;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class SomeTest {

	private static final String BEAN_TEXT = "Ejb";
//	private static final String BEAN_TEXT = "Service";

	@Deployment(testable = false)
	@SuppressWarnings("unused")
	public static WebArchive createDeployment() {

		PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
		ScopeType[] scopes = {ScopeType.COMPILE, ScopeType.TEST}; // no SYSTEM and no PROVIDED
		File[] libs = pom.importDependencies(scopes).resolve().using(TransitiveStrategy.INSTANCE).asFile();

		return ShrinkWrap.create(WebArchive.class, "service-test.war")
				.addAsLibraries(libs)
				.addPackages(true, Filters.exclude(".*Test.*"), "io.github.jorgeviana.services")
				.addClass(io.github.jorgeviana.beans.EjbBean.class)
				.addClass(io.github.jorgeviana.services.ServiceBean.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@RunAsClient
	public void testHeartbeat(@ArquillianResource URL baseURL) throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpGet httpGet = new HttpGet(baseURL.toExternalForm() + "rest/v1/heartbeat/hello");
			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				System.out.println(response);
				final int responseCode = response.getStatusLine().getStatusCode();
				assertEquals(HttpURLConnection.HTTP_OK, responseCode);

				System.out.println(response);

				assertNotNull(response.getEntity());

				// get the entity from the response and convert it into a string
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				IOUtils.copy(response.getEntity().getContent(), byteArrayOutputStream);
				assertTrue(byteArrayOutputStream.toString().contains(BEAN_TEXT));
				System.out.println(byteArrayOutputStream.toString());
			}
		}
	}


	@Test
	@RunAsClient
	public void testHeartbeatRestAssured(@ArquillianResource URL baseURL) throws IOException {

		get(baseURL.toExternalForm() + "rest/v1/heartbeat/hello")
				.then().statusCode(200)
				.and().body(containsString(BEAN_TEXT));
	}



	@ArquillianResource URL u;

	@Test
	@RunAsClient
	public void other() {
		get(u.toExternalForm() + "rest/v1/heartbeat/hello")
				.then().statusCode(200)
				.and().body(containsString(BEAN_TEXT));
	}
}
