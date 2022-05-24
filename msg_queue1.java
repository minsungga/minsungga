import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Msg01 {

	public static void main(String[] args) {
		// 실행방식 : 종료없음
		
		Scanner stdIn = new Scanner(System.in);
		Queue<String> queue = new LinkedList<>();		
		
		while(true) {
			// 1. 콘솔 입력				
			String line = stdIn.nextLine();		
			String[] list = line.trim().split(" +");	// 앞/뒤 공백 제거 -> 구분자(공백)로 문자 자르기  (+는 하나 이상의 공백을 의미하는 정규식)	
	
			String cmd = null;
			String msg = null;
			switch(list.length) {
			case 1 : 
				cmd = list[0]; 
				break;
			case 2:				
				cmd = list[0];
				msg = list[1];
				break;
			} 
			
			
			// 2. 명령어 종류 파싱		
			if ("SEND".equalsIgnoreCase(cmd)) {				
				queue.offer(msg);	// Queue에 저장
				
			} else if ("RECEIVE".equalsIgnoreCase(cmd)) {
				String tmp = queue.poll();	// Queue에서 제거
				System.out.println(tmp);	
				
			} else {
				System.out.println("error : invalid cmd");
			}
		}
	}

}
