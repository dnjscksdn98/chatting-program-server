package chattingProgram;

public class ServerMain {
	public static void main(String[] args) {
		// 서버를 생성하는 스레드를 생성 & 실행
		Thread t = new Thread(new Server(54571));
		t.start();
	}
}
