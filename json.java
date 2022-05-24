package test;

import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MyJson {

	public static void main(String[] args) {
        FileWriter writer=null;
        try {
             
        	/********************************************
        	 * Read File: gson.txt 
	        	 {
	  				"company": "AAA",
	  				"address": "seoul",
	  				"newEmployees": [
	    				{
	      					"name": "kim",
	      					"age": "29",
	      					"isNew": true
	    				},
	    				{
	      					"name": "park",
	      					"age": "27",
	      					"isNew": true
	    				},
	    				{
	      					"name": "lee",
	      					"age": "26",
	      					"isNew": true
	    				}
	  				]
				}
        	 */

        	/********************************************
        	 * JsonObject => { {}, {}, [{}, {}, {}] }
        	 * JsonArray => [{}, {}, {}]
        	 * JsonElement => {}
        	 */
        	
        	// JSON파일 == 1개의 JsonElement
        	Gson rGson = new Gson();
        	JsonElement rJsonElement = rGson.fromJson(new FileReader("./INPUT/gson.txt"), JsonElement.class);  
        	
        	// 모든 파싱은 JsonObject에서 시작한다. 
        	JsonObject rJsonObject = rJsonElement.getAsJsonObject();      	

        	
        	// 1개의 JsonObject마다 Key를 활용하여 Data 추출
        	String str1 = rJsonObject.get("company").getAsString();
        	System.out.println("company = " + str1);		// company = AAA
        	
        	String str2 = rJsonObject.get("address").getAsString();
        	System.out.println("address = " + str2);		// address = seoul
        	
        	
        	// JsonArray의 각 행은 JsonElement이고, 
        	JsonArray rJsonArray = rJsonObject.getAsJsonArray("newEmployees");
        	for(int i=0; i<rJsonArray.size(); i++) {
        		
        		// JsonArray의 각 요소 == JsonElement
        		JsonElement tmpJsonElement = rJsonArray.get(i);
        		
        		// 모든 파싱은 JsonObject에서 시작한다. (반복)
        		String name = tmpJsonElement.getAsJsonObject().get("name").getAsString();	
        		String age = tmpJsonElement.getAsJsonObject().get("age").getAsString();
        		boolean isNew = tmpJsonElement.getAsJsonObject().get("isNew").getAsBoolean();
        		
        		System.out.println(name + " : " + age + " : " + isNew);        	// 	kim : 29 : true
        	}
        	
        	
        	
        	
        	/********************************************
        	 * Write File: new_gson2.txt 
				{
				  "company": "BBB",
				  "address": "Busan",
				  "newEmployees": [
				    {
				      "name": "alice",
				      "age": "20",
				      "isNew": true
				    },
				    {
				      "name": "brown",
				      "age": "25",
				      "isNew": false
				    },
				    {
				      "name": "ami",
				      "age": "30",
				      "isNew": true
				    }
				  ]
				}
        	 */
        	
        	Gson wGson = new GsonBuilder().setPrettyPrinting().create();
        	JsonObject wJsonObject = new JsonObject();
        	
        	wJsonObject.addProperty("company", "BBB");
        	wJsonObject.addProperty("address", "Busan");
        	
        	
        	
        	String[] nameList = {"alice", "brown", "ami"};
        	String[] ageList = {"20", "25", "30"};
        	boolean[] isNewList = {true, false, true};
        	
        	JsonArray wJsonArray = new JsonArray();
        	for(int i=0; i<3; i++) {
        		JsonObject tmpJsonObject = new JsonObject();
        		tmpJsonObject.addProperty("name", nameList[i]);
        		tmpJsonObject.addProperty("age", ageList[i]);
        		tmpJsonObject.addProperty("isNew", isNewList[i]);
        		wJsonArray.add(tmpJsonObject);
        	}        	
        	wJsonObject.add("newEmployees", wJsonArray);
        	
        	
        	String json = wGson.toJson(wJsonObject);
        	System.out.println(json);
        	
        	
       		writer = new FileWriter("./OUTPUT/new_gson2.json");
        	writer.write(json);
       		writer.close();        	
        	
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
	}
}
