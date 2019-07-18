package aklny;

import com.google.gson.Gson;

public class Response {
	public String state;
	public String message;
	
	
	
	public Response() {
	}


	
	public Response(String state, String message) {
		this.state = state;
		this.message = message;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}
