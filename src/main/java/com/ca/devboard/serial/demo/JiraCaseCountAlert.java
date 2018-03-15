package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialIO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class JiraCaseCountAlert
{
	private static final Logger LOG = Logger.getLogger(JiraCaseCountAlert.class.getName());
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
	private static final String JIRA_API_SEARCH_PATH = "https://jira.arbfund.com/rest/api/2/search";
	private static final String AUTH_HEADER = buildBasicAuthHeader(System.getenv("CWUSER"), System.getenv("CWPASS"));

	public static void runDemo(SerialIO serialIo) throws InterruptedException
	{
		while (true)
		{
			int numUnassignedOpenCases = getNumUnassignedOpenCases("LABS");
			int numUnassignedCriticalCases = getNumUnassignedCriticalCases("LABS");
			System.out.printf("%d unassigned open cases. %d unassigned criticals.%n", numUnassignedOpenCases, numUnassignedCriticalCases);

			try
			{
				serialIo.sendAlert(0, numUnassignedOpenCases);
				serialIo.sendAlert(1, numUnassignedCriticalCases);
			}
			catch (IOException ex)
			{
				LOG.log(Level.SEVERE, null, ex);
			}
			Thread.sleep(5000);
		}
	}

	public static int getNumUnassignedOpenCases(String projectName)
	{
		return runQuery(String.format("project=%s AND status in (PROPOSED, Open, Ready, Acknowledged) AND assignee=EMPTY", projectName));
	}

	public static int getNumUnassignedCriticalCases(String projectName)
	{
		return runQuery(String.format("project=%s AND status in (PROPOSED, Open, Ready, Acknowledged) AND priority = Critical AND assignee=EMPTY", projectName));
	}

	private static int runQuery(String jqlQuery)
	{
		String url;
		try
		{
			url = String.format("%s?jql=%s&maxcount=0", JIRA_API_SEARCH_PATH, URLEncoder.encode(jqlQuery, "UTF-8"));
		}
		catch (UnsupportedEncodingException ex)
		{
			LOG.log(Level.SEVERE, null, ex);
			throw new RuntimeException("URL encoding not supported", ex);
		}
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
		try(CloseableHttpResponse response = HTTP_CLIENT.execute(getRequest))
		{
			Map<String, Object> data = MAPPER.readValue(response.getEntity().getContent(), new TypeReference<Map<String, Object>>(){});
			return (int)data.get("total");
		}
		catch (IOException ex)
		{
			LOG.log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	public static String buildBasicAuthHeader(String username, String password)
	{
		if (username == null || username.isEmpty())
			throw new IllegalArgumentException("username must be provided");
		if (password == null || password.isEmpty())
			throw new IllegalArgumentException("password must be provided");
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
		return "Basic " + new String(encodedAuth);
	}
}
