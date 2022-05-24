package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ArrayList<MyQueue> mQList = new ArrayList<>();
	
	// GET 메서드 처리
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("MyServlet : doGet() 호출");
		
		// 1. URI 파싱
		String uri = req.getRequestURI().toString();
		
		String[] list = uri.split("/");
		String cmd = list[1];
		String qName = list[2];
		
		if ("RECEIVE".equalsIgnoreCase(cmd)) {
			MyMessage myMsg = null;
			
//			System.out.println("큐 개수 = " + mQList.size());
			
			for(MyQueue mQ : mQList) {
				
//				System.out.println(qName + " " + mQ.getName());
				
				if (qName.equalsIgnoreCase(mQ.getName())) {
					myMsg = mQ.peek();
					break;
				}
			}
			
			if (myMsg != null) {
				sendResponseMsg(res, myMsg.id, myMsg.msg);
			} else {
				sendResponseNoMsg(res);
			}
		}		
	}
	
	// POST 메서드 처리
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		System.out.println("MyServlet : doPost() 호출");
		
		
		// 1. URI 파싱
		String uri = req.getRequestURI().toString();
		System.out.println("uri : " + uri);
		
		String[] list = uri.split("/");
//		for(String str : list) {
//			System.out.println(str);
//		}
		
		String cmd = list[1];
		String qName = list[2];
		String msgId = null;
		if (list.length == 4) {
			msgId = list[3];
		}
		
//		BufferedReader br = req.getReader();
//		String line = "";
//		for(int i=0; (line = br.readLine()) != null; i++) {
//			System.out.println(line);
//		}
		
		// 2. Request Body (Json포맷) 파싱
		Gson gson = new Gson();
		Map<String, String> reqMap = gson.fromJson(req.getReader(), Map.class);
		
		
		// 3. 비즈니스 로직
		if ("CREATE".equalsIgnoreCase(cmd)) {
			int qSize = Integer.parseInt(reqMap.get("QueueSize"));
			boolean isQExist = false;
			
			for(MyQueue mQ : mQList ) {
				if (qName.equalsIgnoreCase(mQ.getName())) {
					isQExist = true;
					break;
				}
			}
						
			if (isQExist) {
				sendResponseQExist(res);				
			} else {
				mQList.add(new MyQueue(qName, qSize));
				sendResponseOk(res);
			}
		} 
		
		else if ("SEND".equalsIgnoreCase(cmd)) {
			String message = reqMap.get("Message");
			
			for (MyQueue mQ : mQList) {
				if (qName.equalsIgnoreCase(mQ.getName())) {
					if (mQ.offer(message)) {
						sendResponseOk(res);
					} else {
						sendResponseQFull(res);
					}
					break;
				}
			}
		}
		
		else if ("ACK".equalsIgnoreCase(cmd)) {
			for (MyQueue mQ : mQList) {
				if (qName.equalsIgnoreCase(mQ.getName())) {
					
					if (mQ.remove(msgId)) {
						sendResponseOk(res);
					}					
				}
			}
		}
		
		else if ("FAIL".equalsIgnoreCase(cmd)) {
			for (MyQueue mQ : mQList) {
				if (qName.equalsIgnoreCase(mQ.getName())) {
					
					if (mQ.recover(msgId)) {
						sendResponseOk(res);
					}
				}
			}
		}
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
		System.out.println("JSON = " + json);
	}
	
	public void sendResponseQExist(HttpServletResponse res) throws IOException {
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();
		Map<String, String> resMap = new HashMap<>();
		resMap.put("Result", "Queue Exist");
		String json = gson.toJson(resMap);
		
		res.getWriter().println(json);
		System.out.println("JSON = " + json);		
	}
	
	public void sendResponseQFull(HttpServletResponse res) throws IOException {
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();
		Map<String, String> resMap = new HashMap<>();
		resMap.put("Result", "Queue Full");
		String json = gson.toJson(resMap);
		
		res.getWriter().println(json);
		System.out.println("JSON = " + json);		
	}
	
	public void sendResponseMsg(HttpServletResponse res, String msgId, String msg) throws IOException {
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();	
		Map<String, String> resMap = new HashMap<>();
		resMap.put("Result", "Ok");
		resMap.put("MessageID", msgId);
		resMap.put("Message", msg);
		String json = gson.toJson(resMap);
				
		res.getWriter().println(json);	
		System.out.println("JSON = " + json);
	}
	
	public void sendResponseNoMsg(HttpServletResponse res) throws IOException {
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();
		Map<String, String> resMap = new HashMap<>();
		resMap.put("Result", "No Message");
		String json = gson.toJson(resMap);		
		
		res.getWriter().println(json);		
		System.out.println("JSON = " + json);
	}
		
}
