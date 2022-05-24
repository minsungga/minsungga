package java_sp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ExecProgram3 {

	public static void main(String[] args) throws IOException, InterruptedException {
		String output = getProcessOutput(Arrays.asList("add_2sec.exe", "2", "3"));	//  프로세스 실행 커맨드
		System.out.println(output);
	}
	
	public static String getProcessOutput(List<String> cmdList) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(cmdList);
		Process process = builder.start();				// 프로세스 실행
		InputStream psout = process.getInputStream();	// 출력 가져오기
		
		byte[] buffer = new byte[1024];
		psout.read(buffer);					// 읽은 바이트를 buffer에 저장한 후, 읽은 데이터 수를 리턴함
		return (new String(buffer));
	}

}
