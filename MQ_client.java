package test;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;


public class MyClient {

	public static void main(String[] args) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
	
		// Request 메서드유형 및 헤더 구성
		Request request = httpClient.POST("http://127.0.0.1:8080/CREATE/PLAY")
								.method(HttpMethod.POST)
								.header(HttpHeader.CONTENT_TYPE, "application/json");		
								
		// Request Body (Json 포맷) 구성
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<>();
		map.put("QueueSize", "2");	
		
		String jsonStr = gson.toJson(map);	
		request.content(new StringContentProvider(jsonStr), "application/json;charset=UTF-8");		
		
		
		// Http 요청 및 응답 수신
		ContentResponse response = request.send();	
//		System.out.println(response.getStatus() + " " + response.getReason());
		
		// Response Body (Json 포맷) 파싱
		Map<String, String> resMap = gson.fromJson(response.getContentAsString(), Map.class);
		String result = resMap.get("Result");
		System.out.println(result);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		request = httpClient.POST("http://127.0.0.1:8080/SEND/PLAY")
				.method(HttpMethod.POST)
				.header(HttpHeader.CONTENT_TYPE, "application/json");		
				
		// Request Body (Json 포맷) 구성
		map.clear();
		map = new HashMap<>();
		map.put("Message", "HELLO");
		
		jsonStr = gson.toJson(map);		
		request.content(new StringContentProvider(jsonStr), "application/json;charset=UTF-8");		
		
		// Http 요청 및 응답 수신
		response = request.send();	
		
		// Response Body (Json 포맷) 파싱
		resMap.clear();
		resMap = gson.fromJson(response.getContentAsString(), Map.class);
		result = resMap.get("Result");
		System.out.println(result);	
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		for(int i= 0; i<2; i++) {
			
			request = httpClient.POST("http://127.0.0.1:8080/RECEIVE/PLAY")
					.method(HttpMethod.GET)
					.header(HttpHeader.CONTENT_TYPE, "application/json");		
			
			// Body 없음
			// Http 요청 및 응답 수신
			response = request.send();	
			
			// Response Body (Json 포맷) 파싱
			resMap.clear();
			resMap = gson.fromJson(response.getContentAsString(), Map.class);
			result = resMap.get("Result");
			
			if ("Ok".equalsIgnoreCase(result)) {
				System.out.println(result + ", " + resMap.get("MessageID") + ", " + resMap.get("Message"));
			} else {
				System.out.println(result);
			}
		}
	}
}
