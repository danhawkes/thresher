package co.arcs.groove.thresher;

import java.io.UnsupportedEncodingException;

import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.StringEntity;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Joiner;

/**
 * Builder class for API requests that depend on a valid {@link Session}.
 */
abstract class RequestBuilder {

	final String method;
	final boolean secure;

	RequestBuilder(String method, boolean secure) {
		this.method = method;
		this.secure = secure;
	}

	HttpPost build(Session session) {
		// Build JSON payload
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ObjectNode rootNode = nodeFactory.objectNode();
		{
			// Header
			ObjectNode header = nodeFactory.objectNode();
			header.put("client", "mobileshark");
			header.put("clientRevision", "20120830");
			header.put("country", session.country);
			header.put("privacy", 0);
			header.put("session", session.phpSession);
			header.put("token", signRequest(method, session.commsToken));
			header.put("uuid", session.uuid);
			rootNode.put("header", header);

			// Method
			rootNode.put("method", method);

			// Parameters
			ObjectNode parameters = nodeFactory.objectNode();
			populateParameters(session, parameters);
			if (parameters.size() > 0) {
				rootNode.put("parameters", parameters);
			}
		}

		// Build request object
		String url = (secure ? "https" : "http") + "://" + Client.DOMAIN + "/more.php#" + method;
		HttpPost httpRequest = new HttpPost(url);
		try {
			httpRequest.setEntity(new StringEntity(rootNode.toString()));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return httpRequest;
	}

	static String signRequest(String method, String commsToken) {
		String salt = "gooeyFlubber";
		String rand = Utils.randHexChars(6);
		String s = Joiner.on(':').join(method, commsToken, salt, rand);
		return rand + DigestUtils.shaHex(s);
	}

	abstract void populateParameters(Session session, ObjectNode parameters);

}