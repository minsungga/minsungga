import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {

	public static void main(String[] args) {
		
		ArrayList<String> lineList = new ArrayList<String>();
		lineList.add("AAA#80");
		lineList.add("CCC#70");
		lineList.add("BBB#90");
		
//		// 디렉토리 생성	
//		File destDir = new File("./OUTFILE");
//		if (!destDir.exists()) {
//			destDir.mkdir();
//		}
		
		// 파일쓰기
		WriteFileByLine("./OUTFILE/RESULT.txt", lineList);
		
	}
	
	public static void WriteFileByLine(String fileName, ArrayList<String> lineList) {
		
		try {
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(String line : lineList) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}

}
