package chattingProgram;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeModule {
	
	// 현재 시간을 반환다
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm:ss");
		
		return sdf.format(new Date());
	}
}
