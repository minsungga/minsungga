import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {

	public static void main(String[] args) {
		
		ArrayList<String> lineList = null;
		lineList = ReadFileByLine("./INFILE/LOCATION.txt");
				
		for(String line : lineList) {
			System.out.println(line);
		}
		
	}
	
	public static ArrayList<String> ReadFileByLine(String fileName) {
		
		ArrayList<String> result = new ArrayList<String>();
		String line = null;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			br.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;		
	}

}
