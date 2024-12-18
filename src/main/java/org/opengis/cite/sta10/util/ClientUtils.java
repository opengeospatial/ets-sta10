package org.opengis.cite.sta10.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import org.opengis.cite.sta10.ReusableEntityFilter;
import org.w3c.dom.Document;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * Provides various utility methods for creating and configuring HTTP client components.
 */
public class ClientUtils {

	private static final Logger LOGGER = Logger.getLogger(ClientUtils.class.getName());

	/**
	 * Builds a client component for interacting with HTTP endpoints. The client will
	 * automatically redirect to the URI declared in 3xx responses. The connection timeout
	 * is 10 s. Request and response messages may be logged to a JDK logger (in the
	 * namespace "com.sun.jersey.api.client").
	 * @return A Client component.
	 */
	public static Client buildClient() {
		ClientConfig config = new ClientConfig();
		config.property(ClientProperties.FOLLOW_REDIRECTS, true);
		config.property(ClientProperties.CONNECT_TIMEOUT, 10000);
		config.register(new LoggingFeature(LOGGER, Level.ALL, LoggingFeature.Verbosity.PAYLOAD_ANY, 5000));
		Client client = ClientBuilder.newClient(config);
		client.register(new ReusableEntityFilter());
		return client;
	}

	/**
	 * Constructs a client component that uses a specified web proxy. Proxy authentication
	 * is not supported. Configuring the client to use an intercepting proxy can be useful
	 * when debugging a test.
	 * @param proxyHost The host name or IP address of the proxy server.
	 * @param proxyPort The port number of the proxy listener.
	 * @return A Client component that submits requests through a web proxy.
	 */
	public static Client buildClientWithProxy(final String proxyHost, final int proxyPort) {
		ClientConfig config = new ClientConfig();
		config.connectorProvider(new ApacheConnectorProvider());
		config.register(new LoggingFeature(LOGGER, Level.ALL, LoggingFeature.Verbosity.PAYLOAD_ANY, 5000));
		SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
		config.property(ClientProperties.PROXY_URI, proxy);
		config.property(ClientProperties.FOLLOW_REDIRECTS, true);
		config.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, Level.ALL);
		Client client = ClientBuilder.newClient(config);
		client.register(new ReusableEntityFilter());
		return client;
	}

	/**
	 * Builds an HTTP request message that uses the GET method.
	 * @param endpoint A URI indicating the target resource.
	 * @param qryParams A Map containing query parameters (may be null);
	 * @param mediaTypes A list of acceptable media types; if not specified, the Accept
	 * header is omitted.
	 * @return A ClientRequest object.
	 */
	public static Response buildGetRequest(URI endpoint, Map<String, String> qryParams, MediaType... mediaTypes) {
		UriBuilder uriBuilder = UriBuilder.fromUri(endpoint);
		if (null != qryParams) {
			for (Map.Entry<String, String> param : qryParams.entrySet()) {
				uriBuilder.queryParam(param.getKey(), param.getValue());
			}
		}
		URI uri = uriBuilder.build();
		WebTarget target = buildClient().target(uri);
		Builder reqBuilder = target.request();
		if (null != mediaTypes && mediaTypes.length > 0) {
			reqBuilder = reqBuilder.accept(mediaTypes);
		}
		Invocation req = reqBuilder.buildGet();
		return req.invoke();
	}

	/**
	 * Creates a copy of the given MediaType object but without any parameters.
	 * @param mediaType A MediaType descriptor.
	 * @return A new (immutable) MediaType object having the same type and subtype.
	 */
	public static MediaType removeParameters(MediaType mediaType) {
		return new MediaType(mediaType.getType(), mediaType.getSubtype());
	}

	/**
	 * Obtains the (XML) response entity as a JAXP Source object and resets the entity
	 * input stream for subsequent reads.
	 * @param response A representation of an HTTP response message.
	 * @param targetURI The target URI from which the entity was retrieved (may be null).
	 * @return A Source to read the entity from; its system identifier is set using the
	 * given targetURI value (this may be used to resolve any relative URIs found in the
	 * source).
	 */
	public static Source getResponseEntityAsSource(Response response, String targetURI) {
		Source source = response.readEntity(DOMSource.class);
		if (null != targetURI && !targetURI.isEmpty()) {
			source.setSystemId(targetURI);
		}
		return source;
	}

	/**
	 * Obtains the (XML) response entity as a DOM Document and resets the entity input
	 * stream for subsequent reads.
	 * @param response A representation of an HTTP response message.
	 * @param targetURI The target URI from which the entity was retrieved (may be null).
	 * @return A Document representing the entity; its base URI is set using the given
	 * targetURI value (this may be used to resolve any relative URIs found in the
	 * document).
	 */
	public static Document getResponseEntityAsDocument(Response response, String targetURI) {
		DOMSource domSource = (DOMSource) getResponseEntityAsSource(response, targetURI);
		Document entityDoc = (Document) domSource.getNode();
		entityDoc.setDocumentURI(domSource.getSystemId());
		return entityDoc;
	}

}
