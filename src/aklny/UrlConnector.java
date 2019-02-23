package aklny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UrlConnector {
	private Map<String, String> headers;
	private String url;
	private Map<String, String> params;
	private String connectionMethod = "GET";
	private int responseCode;
	public UrlConnector(String url) {
		// TODO Auto-generated constructor stub
		this.url = url;
		headers = new HashMap<>();
		params = new HashMap<>();
		
	}
	
	public String getResponse() throws IOException
	{
		 URL urlObj = new URL(url);
		 HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		 //setting request method
		 connection.setRequestMethod("GET");

		 for (String key : headers.keySet()) {
			connection.setRequestProperty(key, headers.get(key));
		 }
		
		 connection.setDoInput(true);

		  responseCode = connection.getResponseCode();
		  BufferedReader in =null;
		  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		  }
		  
		  if(in != null) {
		      in.close();
		    }
		
		return response.toString();
	}
	
	public String getConnectionMethod() {
		return connectionMethod;
	}

	public void setConnectionMethod(String connectionMethod) {
		this.connectionMethod = connectionMethod;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
}
