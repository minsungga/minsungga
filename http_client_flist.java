package test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class MyClient {

	public static void main(String[] args) throws Exception, IOException {
		
		/* Q. Input 폴더의 파일 목록을 전송
		{
			"Folder": "Input",
			"Files": [
			"close_x.png",
			"config.js",
			"MyAll.txt",
			"progress_bg_center.bmp",
			"test.exe"
			]
			}
		*/
		
		// 파일목록 Read -> Json데이터 구성
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Folder", "Input");
		
		File dir = new File("./Input");
		File[] files = dir.listFiles();
		
		JsonArray jsonArray = new JsonArray();
		for(int i=0; i<files.length; i++) {			
			jsonArray.add(files[i].getName());	
		}

		jsonObject.add("Files", jsonArray);	
		
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(jsonObject);
		System.out.println(json);
			
		
		// HttpClient 준비
		HttpClient httpClient = new HttpClient();
		httpClient.start();
	
		// Request 메서드유형 및 헤더 구성
		Request request = httpClient.POST("http://127.0.0.1:8080/FILES")
								.method(HttpMethod.POST)
								.header(HttpHeader.CONTENT_TYPE, "application/json");			
		
		// Request Content (Json 포맷) 구성
		request.content(new StringContentProvider(json), "application/json;charset=UTF-8");		
		
		
		// Http 요청 및 응답 수신
		ContentResponse response = request.send();	
		
		// Response 헤더정보
		System.out.println("상태값 - " + response.getStatus() + " " + response.getReason());	// 상태값 - 200 OK
		
		// Response Content (Json 포맷) 파싱
		String strContent = response.getContentAsString();
		System.out.println("응답데이터 - " + strContent);		// 응답데이터 - {"Result":"Ok"}
		
		Map<String, String> resMap = gson.fromJson(strContent, Map.class);
		String result = resMap.get("Result");
		System.out.println(result);		// Ok
		
		// HTTP 통신 종료
		httpClient.stop();
	}
}
