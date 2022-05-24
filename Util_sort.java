import java.util.ArrayList;
import java.util.Collections;

public class Sorting {

	public static void main(String[] args) {
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("AAA#80");
		al.add("CCC#70");
		al.add("BBB#90");
		
		// 오름차순 정렬
		Collections.sort(al);
		
//		for(String item : al) {
//			System.out.println(item);
//		}
		
		// 내림차순 정렬
		Collections.reverse(al);
		
//		for(String item : al) {
//			System.out.println(item);
//		}

	}

}
