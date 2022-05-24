import java.io.File;

public class SearchFileList {

	public static void main(String[] args) {

		File dir = new File("./INFILE");			
		File[] fList = dir.listFiles();
			
		for(File file : fList) {				
			if (file.isDirectory()) {
				System.out.println("directory : " + file.getName());
			
			} else {
				System.out.println("file : " + file.getName());
			}			
		}
		
	} 

}
