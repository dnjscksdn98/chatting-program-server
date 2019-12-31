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
	
	//채팅방에 참여하는 사용자 인원수
	private int nUser = 0;
	Thread t[] = new Thread[10];
	
	//채팅방 이름
	private String cName;
	
	public Server(int port) {  
		this.SERVER_PORT = port;
	}
	
	@Override
	public void run() {
		//스레드 내에서 포트를 오픈해야, 다른 행위 도중에도 오픈이 가능하여 멈추는 현상이 없음
		try {	
			//오픈 시에 이미 오픈된 포트가 있어서 안열리는 경우, 오류 출력후 프로그램 종료
			server_socket = new ServerSocket(SERVER_PORT);
			
		}catch(IOException e) {
			System.err.println(SERVER_PORT + " 가 사용중입니다.");
			System.exit(-1);  //프로그램 종료
		}
		
		//채팅방 이름 설정
		try {
			in2 = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.print("채팅방 이름을 설정하시오 : ");
			cName = in2.readLine();
			
			System.out.println(cName + " 채팅방 오픈 !!");

		}catch(Exception e) {
			
		}
		
		//채팅방에 들어오는 사용자들을 받아들인다
		while(true) {
			try {
				// 새로운 사용자를 받는다
				socket = server_socket.accept();
				
				// 스레드 실행 
				t[nUser] = new Thread(new Receiver(socket));
				t[nUser++].start();
				
			}catch(IOException e) {
				
			}
		}
	}
}