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
	
	//��� ����ڰ� ���� User Ŭ������ ��� �� �� �ֵ��� ����
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
				// ����ڷκ��� �Էµ� �޽����� �о�´�
				String msg = in.readLine();
				
				// �о�� �޽����� ���� JSONObject ��ü ����
				JSONObject data = new JSONObject(msg);
							
				// � ������ �޽������� üũ
				user.checkMessage(socket, data.toString());
			}
		}catch(Exception e) {
			
		}
	}
}