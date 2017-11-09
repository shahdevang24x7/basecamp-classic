package api.basecamp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/***
 * Base for building out BaseCamp Entities
 * 
 * Includes Basic REST Operations with Authentication
 * 
 * @author jondavidjohn
 */
abstract class BaseCampEntity {

	// --- Authentication information
	private BCAuth auth;

	protected BaseCampEntity(BCAuth auth) {
		this.auth = auth;
	}

	/**
	 * Prepare connection and set authorization details from BCAuth
	 * 
	 * @author devang.shah
	 * 
	 * @param connection
	 * @param request
	 * @param requestMethod
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection prepareConnection(String request, String requestMethod) throws IOException {
		URL url = new URL(auth.getBaseUrl() + request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		Decoder.BASE64Encoder enc = new Decoder.BASE64Encoder();
		if (auth.hasToken()) {
			String token = auth.getTokenKey();
			String encodedAuthorization = enc.encode(token.getBytes());
			connection.setRequestProperty("Authorization", "Client-ID " + encodedAuthorization);
		} else {
			String userpassword = auth.getUsername() + ":" + auth.getPassword();
			String encodedAuthorization = enc.encode(userpassword.getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
		}
		connection.setRequestProperty("Content-type", "application/xml");
		connection.setRequestProperty("Accept", "application/xml");
		return connection;
	}

	// --- Base REST interaction methods

	/***
	 * GET HTTP Operation
	 * 
	 * @param request
	 *            Request URI
	 * @return Element Root Element of XML Response
	 */
	protected Element get(String request) {
		HttpURLConnection connection = null;

		Element rootElement = null;

		try {
			connection = prepareConnection(request, "GET");

			InputStream responseStream = connection.getInputStream();

			// --- Parse XML response InputStream into DOM

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(responseStream);
			rootElement = doc.getDocumentElement();

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return rootElement;
	}

	/***
	 * POST HTTP Operation
	 * 
	 * @param request
	 *            Request URI
	 * @param POST
	 *            Data in String form
	 * @return int ID of inserted (posted) element
	 */
	protected int post(String request, String requestData) {

		HttpURLConnection connection = null;

		int itemID = 0;

		try {
			connection = prepareConnection(request, "POST");

			// --- Send POST data
			connection.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(requestData);
			wr.flush();
			wr.close();

			String responseHeader = connection.getHeaderField(0);
			String[] headerArray = responseHeader.split(" ");
			int responseCode = Integer.parseInt(headerArray[1]);
			if (responseCode == 201) {
				String locationURL = connection.getHeaderField("Location");
				String[] locationArray = locationURL.split("/");
				itemID = Integer.parseInt(locationArray[locationArray.length - 1].replace(".xml", ""));
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return itemID;
	}

	/***
	 * PUT HTTP OPERATION
	 * 
	 * @param request
	 *            Request URI
	 * @param POST
	 *            Data in String form
	 * @return Boolean if operation was successful
	 */
	protected boolean put(String request, String requestData) {

		HttpURLConnection connection = null;

		boolean response = false;

		try {
			connection = prepareConnection(request, "PUT");

			// --- Send POST data
			connection.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(requestData);
			wr.flush();
			wr.close();

			String responseHeader = connection.getHeaderField(0);
			String[] headerArray = responseHeader.split(" ");
			int responseCode = Integer.parseInt(headerArray[1]);
			if (responseCode == 200) {
				response = true;
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return response;
	}

	/***
	 * DELETE HTTP OPERATION
	 * 
	 * @param request
	 *            Request URI
	 * @return Boolean success
	 */
	protected boolean delete(String request) {

		HttpURLConnection connection = null;

		boolean response = false;

		try {
			connection = prepareConnection(request, "DELETE");

			String responseHeader = connection.getHeaderField(0);
			String[] headerArray = responseHeader.split(" ");
			int responseCode = Integer.parseInt(headerArray[1]);
			if (responseCode == 200) {
				response = true;
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return response;
	}

}
