import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Msg02 {

	// 이름과 크기 제한이 있는 Queue클래스 정의
	class InnerQueue {
		String name = null;
		int limit = 0;
		Queue<String> q = new LinkedList<>();
		
		
		public InnerQueue(String name, int limit) {
			this.name = name;
			this.limit = limit;
		}
		
		public String getName() {
			return name;
		}
		
		public Boolean add(String msg) {
			if (q.size() >= limit) {
				return false;
			} else {
				q.add(msg);
				return true;
			}
		}
		
		public String poll() {
			return q.poll();
		}
	}
	
	
	public static void main(String[] args) {
		// 실행방식 : 종료없음
		
		Msg02 msg02 = new Msg02();	// 내부클래스 호출 위해 정의
		
		ArrayList<InnerQueue> iQList = new ArrayList<>();			
		
		Scanner stdIn = new Scanner(System.in);		
		
		while(true) {
			// 1. 콘솔 입력				
			String line = stdIn.nextLine();		
			String[] list = line.trim().split(" +");	// 앞/뒤 공백 제거 -> 구분자(공백)로 문자 자르기  (+는 하나 이상의 공백을 의미하는 정규식)	
	
			String cmd = list[0];
			String qName = null;			
			int qSize = 0;
			String msg = null;

			// 2. 명령어 파싱
			if ("CREATE".equalsIgnoreCase(cmd)) {
				qName = list[1];
				qSize = Integer.parseInt(list[2]);
				Boolean isQExist = false;
				
				for(InnerQueue iQ : iQList) {
					if (qName.equalsIgnoreCase(iQ.getName())) {
						System.out.println("Queue Exist");
						isQExist = true;
						break;
					} 
				}
				
				if (!isQExist) {
					iQList.add( msg02.new InnerQueue(qName, qSize) );
				}
				
			} else if ("SEND".equalsIgnoreCase(cmd)) {
				qName = list[1];
				msg = list[2];
				
				for(InnerQueue iQ : iQList) {
					if (qName.equalsIgnoreCase(iQ.getName())) {
						if (!iQ.add(msg)) {
							System.out.println("Queue Full");
						}
					}
				}
				
			} else if ("RECEIVE".equalsIgnoreCase(cmd)) {
				qName = list[1];
				
				for(InnerQueue iQ : iQList) {
					if (qName.equalsIgnoreCase(iQ.getName())) {
						String str = iQ.poll();
						if (str != null) {
							System.out.println(str);
						}
					}
				}
			}

		}
	}

}
