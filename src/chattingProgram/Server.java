package chattingProgram;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server implements Runnable {
	private ServerSocket server_socket = null;
	private Socket socket = null;
	private BufferedReader in2 = null;
	private int SERVER_PORT = 54571;
	
	//ä�ù濡 �����ϴ� ����� �ο���
	private int nUser = 0;
	Thread t[] = new Thread[10];
	
	//ä�ù� �̸�
	private String cName;
	
	public Server(int port) {  
		this.SERVER_PORT = port;
	}
	
	@Override
	public void run() {
		//������ ������ ��Ʈ�� �����ؾ�, �ٸ� ���� ���߿��� ������ �����Ͽ� ���ߴ� ������ ����
		try {	
			//���� �ÿ� �̹� ���µ� ��Ʈ�� �־ �ȿ����� ���, ���� ����� ���α׷� ����
			server_socket = new ServerSocket(SERVER_PORT);
			
		}catch(IOException e) {
			System.err.println(SERVER_PORT + " �� ������Դϴ�.");
			System.exit(-1);  //���α׷� ����
		}
		
		//ä�ù� �̸� ����
		try {
			in2 = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.print("ä�ù� �̸��� �����Ͻÿ� : ");
			cName = in2.readLine();
			
			System.out.println(cName + " ä�ù� ���� !!");

		}catch(Exception e) {
			
		}
		
		//ä�ù濡 ������ ����ڵ��� �޾Ƶ��δ�
		while(true) {
			try {
				// ���ο� ����ڸ� �޴´�
				socket = server_socket.accept();
				
				// ������ ���� 
				t[nUser] = new Thread(new Receiver(socket));
				t[nUser++].start();
				
			}catch(IOException e) {
				
			}
		}
	}
}