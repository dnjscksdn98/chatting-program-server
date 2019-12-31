package chattingProgram;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

public class Receiver implements Runnable {
	Socket socket = null;
	BufferedReader in = null;
	BufferedReader in2 = null;
	PrintWriter out = null;
	
	//모든 사용자가 같은 User 클래스를 사용 할 수 있도록 선언
	User user = new User();
	
	public Receiver(Socket socket) {
		try {
			this.socket = socket;
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		}catch(IOException e) {
			
		}
	}
	
	@Override
	public void run() {	
		try {
			while(true) {
				// 사용자로부터 입력된 메시지를 읽어온다
				String msg = in.readLine();
				
				// 읽어온 메시지에 대해 JSONObject 객체 생성
				JSONObject data = new JSONObject(msg);
							
				// 어떤 형태의 메시지인지 체크
				user.checkMessage(socket, data.toString());
			}
		}catch(Exception e) {
			
		}
	}
}