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
	
	// DB와 연동할 객체
	MembersDB DBmodule = new MembersDB();
	
	// 가입한 회원들의 리스트
	ArrayList<Member> members = new ArrayList<>();
	
	// DB에 저장되어 있는 회원 정보를 읽어와서 회원 리스트에 저장
	public User() {
		members = DBmodule.loadMembers();
	}
	
	// 회원들중에서 현재 유저를 가져오기
	private Member getMember(Member member) {
		Member currentMember = null;
		
		for(Member m : members) {
			if(m.getID().equals(member.getID())) {
				currentMember = m;
			}
		}
		
		return currentMember;
	}
	
	// 클라이언트로부터 읽어온 메시지의 유형을 체크하는 함수
	public void checkMessage(Socket socket, String msg) {
		try {
			this.socket = socket;
			JSONObject message = new JSONObject(msg);
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// 회원가입
			if(message.getString("command").equals("register")) {
				doRegister(message.getString("name"), message.getString("id"), message.getString("passwd"));
			}
			
			// 로그인
			else if(message.getString("command").equals("login")){
				doLogin(message.getString("id"), message.getString("passwd"));
			}
			
			// 로그아웃
			else if(message.getString("command").equals("logout")) {
				doLogout(message.getString("id"));
			}
			
			// 채팅 메시지
			else {
				printMessage(message.toString());
			}
			
		}catch(Exception e) {
			
		}
	}
	
	//회원가입 결과를 반환하는 함수
	public void doRegister(String name, String id, String passwd) {
		Member newMember = new Member(name, id, passwd);
		int result = checkRegister(newMember);
		
		JSONObject res = new JSONObject();
		res.put("command", "register");
		
		//회원가입에 성공
		if(result == 1) {
			res.put("result", true);
			res.put("message", "회원가입에 성공하였습니다.");
			
			out.println(res.toString());
			out.flush();
			
			//가입한 회원 리스트에 객체 추가
			members.add(newMember);
			DBmodule.addMember(newMember);
		}
		//회원가입에 실패
		else {
			res.put("result", false);
			res.put("message", "이미 존재하는 ID입니다.");
			out.println(res.toString());
			out.flush();
		}
	}
	
	// 회원가입 상수
	public static int REGISTER_ERROR = 0;
	public static int REGISTER_OK = 1;
	
	// 회원가입이 가능한지 체크하는 함수
	public int checkRegister(Member member) {
		int result = REGISTER_OK;
		
		for(Member m : members) {
			// 만약 회원들 중에 이미 입력한 ID를 사용하는 회원이 있으면 회원가입 실패
			if(m.getID().equals(member.getID())) {
				result = REGISTER_ERROR;
				break;
			}
		}
		
		return result;
	}
	
	// 로그인 상수
	public static int ID_ERROR = 0;
	public static int PW_ERROR = 1;
	public static int ALREADY_LOGIN = 2;
	public static int LOGIN_OK = 3;
	
	// 로그인 결과를 반환하는 함수
	public void doLogin(String id, String passwd) {
		int result = checkLogin(new Member(id, passwd));
		
		JSONObject res = new JSONObject();
		res.put("command", "login");
		
		// 아이디 에러
		if(result == 0) {
			res.put("result", false);
			res.put("message", "ID를 잘못 입력하였습니다.");
			out.println(res);
			out.flush();
		}
		
		// 비밀번호 에러
		else if(result == 1) {
			res.put("result", false);
			res.put("message", "PW를 잘못 입력하였습니다.");
			out.println(res);
			out.flush();
		}
		
		// 이미 로그인된 회원
		else if(result == 2) {
			res.put("result", false);
			res.put("message", "이미 로그인된 유저입니다.");
			out.println(res);
			out.flush();
		}
		
		// 로그인에 성공
		else {
			res.put("result", true);
			res.put("message", "로그인에 성공하였습니다.");
			out.println(res);
			out.flush();
			
			// 로기인 상태를 true로 설정
			Member currentMember = getMember(new Member(id, passwd));
			currentMember.setLogin(true);
			
			System.out.printf("%s 님이 방에 입장하였습니다.\n", currentMember.getID());
		}
	}
	
	// 로그인이 가능한지 체크하는 함수
	public int checkLogin(Member member) {
		int result = ID_ERROR;
		
		//회원 멤버인지 체크
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
	
	// 로그인 되어 있는 회원을 로그아웃 해주는 함수
	public void doLogout(String id) {
		Member currentMember = getMember(new Member(id));
		int result = checkLogin(currentMember);
		
		JSONObject res = new JSONObject();
		res.put("command", "logout");
		
		// 로그인이 되있는 유저일 경우
		if(result == 2) {
			res.put("result", true);
			res.put("message", "로그아웃 하였습니다.");
			
			out.println(res);
			out.flush();
			
			currentMember.setLogin(false);
			System.out.printf("%s 님이 방을 나갔습니다.\n", currentMember.getID());
		}
		// 로그인이 되있지 않은 유저일 경우
		else {
			res.put("result", false);
			res.put("message", "로그인이 되있지 않은 유저입니다.");
		}
	}
	
	// 유저들이 입력한 채팅을 서버에 출력
	public void printMessage(String msg) {
		JSONObject message = new JSONObject(msg);
		
		System.out.printf(">> %s : %s   %s\n", 
				message.getString("id"), message.getString("message"), TimeModule.getCurrentTime());
	}
}
