import java.io.File;

public class SearchSubDirectory {

	public static void main(String[] args) {
		SearchDir("./INFILE");
	}
	
	public static void SearchDir(String path) {
		File dir = new File(path);
		File[] fList = dir.listFiles();
		
		for(File file : fList) {
			if (file.isDirectory()) {
				SearchDir(file.getPath());
			
			} else {
				System.out.println("file : " + file.getName());
			}			
		}
	}

}
