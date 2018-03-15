package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialIO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class GraphiteAlert
{
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

	public static void run(SerialIO serialIo, boolean mocked) throws IOException, InterruptedException
	{
		GraphiteAlert graphiteLoader = new GraphiteAlert();
		while (true)
		{
			GraphiteMetric graphiteMetric = graphiteLoader.getLatestDatabaseCpu(mocked);
			System.out.println("Database " + graphiteMetric.getTarget() + " CPU: " + graphiteMetric.getValue());
			serialIo.sendAlert(0, (int) graphiteMetric.getValue());
			Thread.sleep(2000);
		}
	}

	private static GraphiteMetric getLatestDatabaseCpu(boolean mocked)
	{
		String host = mocked ? "dev-ip-integ-mockshine-app1:8084/graphite" : "graphite";
		String targetConfig = "highestCurrent(SQLServers.Production.%7Bboi-recondb%7D.OS.Processor%3APercentTotal%2C1)";
		String url = "http://" + host + "/render?from=-2mins&until=now"
				+ "&target=" + targetConfig
				+ ".OS.Processor%3APercentTotal&format=json";

		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet))
		{
			List<Map<String, Object>> data = MAPPER.readValue(response.getEntity().getContent(), new TypeReference<List<Map<String, Object>>>()
			{
			});
			if (data.isEmpty())
			{
				return null;
			}

			String target = (String) data.get(0).get("target");
			List<List<Object>> datapoints = (List<List<Object>>) data.get(0).get("datapoints");
			for (int i = datapoints.size() - 1; i >= 0; i--)
			{
				List<Object> row = datapoints.get(i);
				if (!row.isEmpty() && row.get(0) instanceof Number)
				{
					return new GraphiteMetric(((Number) row.get(0)).doubleValue(), target);
				}
			}

			return null;
		}
		catch (Exception ex)
		{
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private static class GraphiteMetric
	{
		private final double value;
		private final String target;

		public GraphiteMetric(double value, String target)
		{
			this.value = value;
			this.target = target;
		}

		public double getValue()
		{
			return value;
		}

		public String getTarget()
		{
			return target;
		}
	}
}
