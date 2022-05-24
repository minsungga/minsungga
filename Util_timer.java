package java_sp;
import java.util.Timer;
import java.util.TimerTask;

public class TimerExam {

	public static void main(String[] args) {
		
		Timer timer = new Timer();
		
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Time-out");
			}
		};
		
		timer.schedule(timerTask, 5000);  // 5초 후, TimerTask 1회 수행

	}

}
