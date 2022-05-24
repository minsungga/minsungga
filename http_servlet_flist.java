package test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("doGet() 호출");
		
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("MyServlet : doPost() 호출");
		
		// 0. Request Header 출력
		System.out.println("[Header] =============================");
//		User-Agent : Jetty/9.4.38.v20210224
//		Host : 127.0.0.1:8080
//		Accept-Encoding : gzip
//		Content-Length : 139
//		Content-Type : application/json		
		
		Enumeration headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = (String)headerNames.nextElement();
			String value = req.getHeader(name);
			System.out.println(name + " : " + value);
		}
		
		System.out.println();
		
		// 1. Request URI 파싱
		System.out.println("[URI] =============================");
//		URI : /FILES
//
//		FILES
		
		String uri = req.getRequestURI().toString();
		System.out.println("URI : " + uri);		// URI : /FILES
		
		String[] list = uri.split("/");
		for(String str : list) {
			System.out.println(str);
		}		
	
		System.out.println();
		
		// 2. Request Body (Json포맷) 파싱
		System.out.println("[Body] =============================");
//		폴더명: Input
//		close_x.png
//		config.js
//		MyAll.txt
//		progress_bg_center.bmp
//		test.exe		
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(req.getReader(), JsonElement.class);
		
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String folder = jsonObject.get("Folder").getAsString();
		System.out.println("폴더명: " + folder);		// 폴더명: Input
		
		JsonArray jsonArray = jsonObject.getAsJsonArray("Files");
		for(int i=0; i<jsonArray.size(); i++) {
			String file = jsonArray.get(i).getAsString();
			System.out.println(file);
		}
		
		System.out.println();
		
		
		// 3. 비즈니스 로직 수행
		// 타 서버로 HTTP 요청 및 응답 수신
			
		
		// 4. 응답 전송
		sendResponseOk(res);
		
	}
	
	
	// HTTP 응답데이터 구성 및 전송	
		public void sendResponseOk(HttpServletResponse res) throws IOException {
			// 기본 설정
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json;charset=UTF-8");
			
			// Body (Json포맷) 설정
			Gson gson = new Gson();
			Map<String, String> resMap = new HashMap<>();
			resMap.put("Result", "Ok");	
			String json = gson.toJson(resMap);		
			
			// 전송
			res.getWriter().println(json);	
			
			System.out.println("[응답데이터] ===========================");
			System.out.println("JSON포맷 응답데이터 = " + json);		// JSON포맷 응답데이터 = {"Result":"Ok"}
		}
}
