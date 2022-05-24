package test;

import java.util.Enumeration;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class MyClient {

	public static void main(String[] args) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
	
		/*
		 * Request 설정
		 */
		// 기본 헤더 설정
		Request request = httpClient.POST("http://127.0.0.1:8080/CREATE/PLAY")
								.method(HttpMethod.POST)
								.header(HttpHeader.CONTENT_TYPE, "application/json;charset=UTF-8");	
		
		// 사용자 정의 헤더 설정
		request.header("user_define_req_header", "AAA");
		
    	// body 설정 (json 포맷)
    	JsonObject reqJsonObject = new JsonObject();    	
    	reqJsonObject.addProperty("req_body", "12345");
    	
    	Gson reqGson = new GsonBuilder().setPrettyPrinting().create();
    	String reqJsonStr = reqGson.toJson(reqJsonObject);
    	request.content(new StringContentProvider(reqJsonStr));
		
//    	Request body ---------> 
//    	{
//    	  "req_body": "12345"
//    	}
    	System.out.println("Request body ---------> ");
    	System.out.println(reqJsonStr + "\n");
    	
    	
    	
    	/*
    	 * Response 응답 수신
    	 */
		ContentResponse response = request.send();	
		
		// 헤더 전체 파싱
//		<--------- Response Header
//		Server = Jetty(9.4.38.v20210224)
//		Content-Length = 24
//		User_Define_Res_Header = BBB
//		Date = Tue, 24 May 2022 07:46:55 GMT
//		Content-Type = application/json;charset=UTF-8
		
		System.out.println("<--------- Response Header");
		HttpFields httpFields = response.getHeaders();
		Enumeration enumeration = httpFields.getFieldNames();
		while (enumeration.hasMoreElements()) {
			String name = (String)enumeration.nextElement();
			String value = httpFields.get(name);
			System.out.println(name + " = " + value);
		}		
//		System.out.println(response.getStatus() + " " + response.getReason());		

		// body 파싱 (json 포맷)
//		<--------- Response Body
//		res_body = ok
//		---------------------- 
		
		System.out.println("\n<--------- Response Body");		
		Gson resGson = new Gson();
		JsonElement resJsonElement = resGson.fromJson(response.getContentAsString(), JsonElement.class);
		JsonObject resJsonObject = resJsonElement.getAsJsonObject();
		String str1 = resJsonObject.get("res_body").getAsString();
		System.out.println("res_body = " + str1);
		System.out.println("---------------------- ");
	
		
		httpClient.stop();
	}
}
