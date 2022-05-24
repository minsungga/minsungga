package test;

import java.util.ArrayList;

public class MyQueue {
	String name = null;
	int limit = 0;
	ArrayList<MyMessage> arrList = new ArrayList<>();
	
	
	public MyQueue(String name, int limit) {
		this.name = name;
		this.limit = limit;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean offer(String msg) {
		if (arrList.size() >= limit) {
			return false;
		} else {
			arrList.add(new MyMessage("M" + String.valueOf(arrList.size()+1), msg));
			return true;
		}
	}
	
	public MyMessage peek() {
		if (arrList.size() != 0) {
			MyMessage myMsg = arrList.get(0);
			if (myMsg.isUsed == false) {
				myMsg.isUsed = true;
				return myMsg;
			}
		} 

		return null;
	}
	
	public boolean remove(String id) {		
		for (int i=0; i < arrList.size(); i++) {
			if (id.equalsIgnoreCase(arrList.get(i).id)) {
				arrList.remove(i);
				return true;
			}
		}		
		return false;
	}
	
	public boolean recover(String id) {
		for (int i=0; i < arrList.size(); i++) {
			if (id.equalsIgnoreCase(arrList.get(i).id)) {
				arrList.get(i).isUsed = false;
				return true;
			}
		}		
		return false;
	}
	
}
