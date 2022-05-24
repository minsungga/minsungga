package test;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("doGet() 호출");
		
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("MyServlet : doPost() 호출");
		/* 
		 * 출력화면
		 	MyServlet : doPost() 호출
			URI : /CREATE/PLAY
			split 결과 = 
			split 결과 = CREATE
			split 결과 = PLAY
			
			----------------> Requst Header
			user_define_req_header :: AAA
			User-Agent :: Jetty/9.4.38.v20210224
			Host :: 127.0.0.1:8080
			Accept-Encoding :: gzip
			Content-Length :: 25
			Content-Type :: application/json;charset=UTF-8
			
			----------------> Requst Body
			req_body = 12345
			
			Response Body <----------------
			{
			  "res_body": "ok"
			}
			
			MyServlet : doPost() 호출
			URI : /CREATE/PLAY
			split 결과 = 
			split 결과 = CREATE
			split 결과 = PLAY
			
			----------------> Requst Header
			user_define_req_header :: AAA
			User-Agent :: Jetty/9.4.38.v20210224
			Host :: 127.0.0.1:8080
			Accept-Encoding :: gzip
			Content-Length :: 25
			Content-Type :: application/json;charset=UTF-8
			
			----------------> Requst Body
			req_body = 12345
			
			Response Body <----------------
			{
			  "res_body": "ok"
			}		 
		 */
		
		/*
		 * Request 수신
		 */
		// URI 파싱
		String uri = req.getRequestURI().toString();
		System.out.println("URI : " + uri);
		
		String[] list = uri.split("/");
		for(String str : list) {
			System.out.println("split 결과 = " + str);
		}	
		System.out.println();
		
		
		// 전체 헤더 파싱
		System.out.println("----------------> Requst Header");
		Enumeration enumeration = req.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String name = (String)enumeration.nextElement();
			String value = req.getHeader(name);
			System.out.println(name + " :: " + value);
		}		
		System.out.println();		

		
		// body (json포맷) 파싱
		System.out.println("----------------> Requst Body");
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(req.getReader(), JsonElement.class);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String str1 = jsonObject.get("req_body").getAsString();
		System.out.println("req_body = " + str1 + "\n");
		

		/*
		 * 비즈니스 로직 수행
		 */
		
		
		
		/*
		 * Response 전송
		 */
		sendResponseOk(res);
		
	}
	
	
	// HTTP 응답데이터 구성 및 전송	
		public void sendResponseOk(HttpServletResponse res) throws IOException {
			// 기본 헤더 설정
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json;charset=UTF-8");
			
			// 사용자 정의 헤더 설정
			res.setHeader("User_Define_Res_Header", "BBB");
			
			// body (json포맷) 설정
	    	JsonObject jsonObject = new JsonObject();    	
	    	jsonObject.addProperty("res_body", "ok");
	    	
	    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    	String jsonStr = gson.toJson(jsonObject);
			
			// 전송
			res.getWriter().println(jsonStr);	
			
			System.out.println("Response Body <----------------");
			System.out.println(jsonStr + "\n");
		}
}
