package chattingProgram;

public class ServerMain {
	public static void main(String[] args) {
		// ������ �����ϴ� �����带 ���� & ����
		Thread t = new Thread(new Server(54571));
		t.start();
	}
}
