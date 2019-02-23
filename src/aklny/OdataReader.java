package aklny;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OdataReader {
	public List<String> getDefaultEmployeesUsernames() {
		// limiting 20 only for a performance ATM
		UrlConnector connector = new UrlConnector(Constants.BASE_URL + "User?$select=username&$top=20");
		connector.getHeaders().put("APIKey", Credentials.API_KEY);
		connector.getHeaders().put("Content-Type", "application/json");
		connector.getHeaders().put("Accept", "application/json");
		try {
			JSONObject obj = new JSONObject(connector.getResponse());
			JSONArray arr = obj.getJSONObject("d").getJSONArray("results");
			// can increase performance by returning just json array :0
			ArrayList<String> strings = new ArrayList<>();
			for (int i = 0; i < arr.length(); i++)
			{
			    strings.add(arr.getJSONObject(i).getString("username"));
			}
			return strings;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
}
