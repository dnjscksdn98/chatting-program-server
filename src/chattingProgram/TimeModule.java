package chattingProgram;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeModule {
	
	// ���� �ð��� ��ȯ��
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm:ss");
		
		return sdf.format(new Date());
	}
}
