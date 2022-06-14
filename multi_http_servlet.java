package test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String jsonFile = "";
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// 127.0.0.1:5001/find
		// 127.0.0.1:5002/auth
		int myPort = req.getLocalPort();
		String uri = req.getRequestURI().toString();
		System.out.println(myPort + uri + "로 요청 수신");
		
		// 포트 번호에 따라 적절한 Json파일 읽기
		if (myPort == 5001) {
			jsonFile = "Proxy-1.json";
		} else if (myPort == 5002){
			jsonFile = "Proxy-2.json";
		}
		
    	// JSON파일 == 1개의 JsonElement
    	Gson rGson = new Gson();
    	JsonElement rJsonElement = rGson.fromJson(new FileReader(jsonFile), JsonElement.class);  
    	
    	// 모든 파싱은 JsonObject에서 시작한다. 
    	JsonObject rJsonObject = rJsonElement.getAsJsonObject();      	

    	
    	// 1개의 JsonObject마다 Key를 활용하여 Data 추출
    	int port = rJsonObject.get("ProxyPort").getAsInt();
//    	System.out.println("port = " + port);		
		
		
    	// JsonArray의 각 행은 JsonElement이고, 
    	Map<String, String> routeMap = new HashMap<>();
    	JsonArray rJsonArray = rJsonObject.getAsJsonArray("routes");
    	for(int i=0; i<rJsonArray.size(); i++) {
    		
    		// JsonArray의 각 요소 == JsonElement
    		JsonElement tmpJsonElement = rJsonArray.get(i);
    		
    		// 모든 파싱은 JsonObject에서 시작한다. (반복)
    		String path = tmpJsonElement.getAsJsonObject().get("path").getAsString();	
    		String url = tmpJsonElement.getAsJsonObject().get("url").getAsString();
    		routeMap.put(path, url);
    		
//    		System.out.println(path + " : " + url );        	
    	}
    	
    	
    	// 적절한 라우팅 작업 수행
    	for(String path : routeMap.keySet()) {
    		String url = routeMap.get(path);
    		if (myPort == port && uri.equalsIgnoreCase(path)) {    			
    			try {
					String body = ConnectService(url + path, res);
					
//					System.out.println("<------- Client로 최종 응답 전송: " + body);
//					res.setStatus(HttpServletResponse.SC_OK);
//					res.getWriter().println(body);	
//					res.getWriter().close();			
					
				} catch (Exception e) {
					e.printStackTrace();
				}
    			break;
    		}
    	}    	
	}
	
	
	private String ConnectService(String totPath, HttpServletResponse res) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		
		// 기본 헤더 설정
		System.out.println("\tService로 연결 요청: " + totPath + "\n");
		Request request = httpClient.POST(totPath)
								.method(HttpMethod.GET);
		
    	/*
    	 * Http Request 전송 및 Response 응답 수신
    	 */
		ContentResponse response = request.send();
		
		
		// Response 응답(헤더 + 데이터)를 버퍼에서 추출하기
		HttpFields httpFields = response.getHeaders();
		System.out.println("\tService로부터 응답 수신: " + response.getStatus() + ", " + response.getReason());
		
		Enumeration enumeration = httpFields.getFieldNames();
		while (enumeration.hasMoreElements()) {
			String name = (String)enumeration.nextElement();
			String value = httpFields.get(name);
			System.out.println("\t헤더정보 " + name + " = " + value);
		}
		
		
		String body = response.getContentAsString();
		System.out.println("\t바디정보 " + body);

		// Orginal Http로 응답 전송
		System.out.println("[ConnectService] <------- Client로 최종 응답 전송: " + body);
		res.setStatus(HttpServletResponse.SC_OK);
		res.getWriter().println(body);	
		res.getWriter().close();		
		
		httpClient.stop();
		
		return body;
	}
	

}
