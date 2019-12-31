package chattingProgram;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.JSONObject;

public class User {
	Socket socket = null;
	BufferedReader in = null;
	BufferedReader in2 = null;
	PrintWriter out = null;
	
	// DB�� ������ ��ü
	MembersDB DBmodule = new MembersDB();
	
	// ������ ȸ������ ����Ʈ
	ArrayList<Member> members = new ArrayList<>();
	
	// DB�� ����Ǿ� �ִ� ȸ�� ������ �о�ͼ� ȸ�� ����Ʈ�� ����
	public User() {
		members = DBmodule.loadMembers();
	}
	
	// ȸ�����߿��� ���� ������ ��������
	private Member getMember(Member member) {
		Member currentMember = null;
		
		for(Member m : members) {
			if(m.getID().equals(member.getID())) {
				currentMember = m;
			}
		}
		
		return currentMember;
	}
	
	// Ŭ���̾�Ʈ�κ��� �о�� �޽����� ������ üũ�ϴ� �Լ�
	public void checkMessage(Socket socket, String msg) {
		try {
			this.socket = socket;
			JSONObject message = new JSONObject(msg);
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// ȸ������
			if(message.getString("command").equals("register")) {
				doRegister(message.getString("name"), message.getString("id"), message.getString("passwd"));
			}
			
			// �α���
			else if(message.getString("command").equals("login")){
				doLogin(message.getString("id"), message.getString("passwd"));
			}
			
			// �α׾ƿ�
			else if(message.getString("command").equals("logout")) {
				doLogout(message.getString("id"));
			}
			
			// ä�� �޽���
			else {
				printMessage(message.toString());
			}
			
		}catch(Exception e) {
			
		}
	}
	
	//ȸ������ ����� ��ȯ�ϴ� �Լ�
	public void doRegister(String name, String id, String passwd) {
		Member newMember = new Member(name, id, passwd);
		int result = checkRegister(newMember);
		
		JSONObject res = new JSONObject();
		res.put("command", "register");
		
		//ȸ�����Կ� ����
		if(result == 1) {
			res.put("result", true);
			res.put("message", "ȸ�����Կ� �����Ͽ����ϴ�.");
			
			out.println(res.toString());
			out.flush();
			
			//������ ȸ�� ����Ʈ�� ��ü �߰�
			members.add(newMember);
			DBmodule.addMember(newMember);
		}
		//ȸ�����Կ� ����
		else {
			res.put("result", false);
			res.put("message", "�̹� �����ϴ� ID�Դϴ�.");
			out.println(res.toString());
			out.flush();
		}
	}
	
	// ȸ������ ���
	public static int REGISTER_ERROR = 0;
	public static int REGISTER_OK = 1;
	
	// ȸ�������� �������� üũ�ϴ� �Լ�
	public int checkRegister(Member member) {
		int result = REGISTER_OK;
		
		for(Member m : members) {
			// ���� ȸ���� �߿� �̹� �Է��� ID�� ����ϴ� ȸ���� ������ ȸ������ ����
			if(m.getID().equals(member.getID())) {
				result = REGISTER_ERROR;
				break;
			}
		}
		
		return result;
	}
	
	// �α��� ���
	public static int ID_ERROR = 0;
	public static int PW_ERROR = 1;
	public static int ALREADY_LOGIN = 2;
	public static int LOGIN_OK = 3;
	
	// �α��� ����� ��ȯ�ϴ� �Լ�
	public void doLogin(String id, String passwd) {
		int result = checkLogin(new Member(id, passwd));
		
		JSONObject res = new JSONObject();
		res.put("command", "login");
		
		// ���̵� ����
		if(result == 0) {
			res.put("result", false);
			res.put("message", "ID�� �߸� �Է��Ͽ����ϴ�.");
			out.println(res);
			out.flush();
		}
		
		// ��й�ȣ ����
		else if(result == 1) {
			res.put("result", false);
			res.put("message", "PW�� �߸� �Է��Ͽ����ϴ�.");
			out.println(res);
			out.flush();
		}
		
		// �̹� �α��ε� ȸ��
		else if(result == 2) {
			res.put("result", false);
			res.put("message", "�̹� �α��ε� �����Դϴ�.");
			out.println(res);
			out.flush();
		}
		
		// �α��ο� ����
		else {
			res.put("result", true);
			res.put("message", "�α��ο� �����Ͽ����ϴ�.");
			out.println(res);
			out.flush();
			
			// �α��� ���¸� true�� ����
			Member currentMember = getMember(new Member(id, passwd));
			currentMember.setLogin(true);
			
			System.out.printf("%s ���� �濡 �����Ͽ����ϴ�.\n", currentMember.getID());
		}
	}
	
	// �α����� �������� üũ�ϴ� �Լ�
	public int checkLogin(Member member) {
		int result = ID_ERROR;
		
		//ȸ�� ������� üũ
		for(Member m : members) {
			if(m.getID().equals(member.getID())) {
				if(m.getPW().equals(member.getPW())) {
					if(m.isLogin())
						result = ALREADY_LOGIN;
					else {
						result = LOGIN_OK;
						break;
					}
				}
				else
					result = PW_ERROR;
			}
			else
				result = ID_ERROR;
		}
		
		return result;
	}
	
	// �α��� �Ǿ� �ִ� ȸ���� �α׾ƿ� ���ִ� �Լ�
	public void doLogout(String id) {
		Member currentMember = getMember(new Member(id));
		int result = checkLogin(currentMember);
		
		JSONObject res = new JSONObject();
		res.put("command", "logout");
		
		// �α����� ���ִ� ������ ���
		if(result == 2) {
			res.put("result", true);
			res.put("message", "�α׾ƿ� �Ͽ����ϴ�.");
			
			out.println(res);
			out.flush();
			
			currentMember.setLogin(false);
			System.out.printf("%s ���� ���� �������ϴ�.\n", currentMember.getID());
		}
		// �α����� ������ ���� ������ ���
		else {
			res.put("result", false);
			res.put("message", "�α����� ������ ���� �����Դϴ�.");
		}
	}
	
	// �������� �Է��� ä���� ������ ���
	public void printMessage(String msg) {
		JSONObject message = new JSONObject(msg);
		
		System.out.printf(">> %s : %s   %s\n", 
				message.getString("id"), message.getString("message"), TimeModule.getCurrentTime());
	}
}
