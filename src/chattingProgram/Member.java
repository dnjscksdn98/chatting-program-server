package chattingProgram;

import java.io.PrintWriter;

public class Member {
	private String name;
	private String ID;
	private String PW;
	
	// 현재 유저의 로그인 여부
	private boolean login;
	
	public Member(String name, String ID, String PW, boolean login) {
		this.name = name;
		this.ID = ID;
		this.PW = PW;
		this.login = login;
	}
	
	public Member(String name, String ID, String PW) {
		this.name = name;
		this.ID = ID;
		this.PW = PW;
		this.login = false;
	}
	
	public Member(String ID, String PW) {
		this.ID = ID;
		this.PW = PW;
		this.name = "";
		this.login = false;
	}
	
	public Member(String ID) {
		this.ID = ID;
		this.PW = "";
		this.name = "";
		this.login = false;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public String getPW() {
		return this.PW;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isLogin() {
		return this.login;
	}
	
	public void setLogin(boolean login) {
		this.login = login;
	}
	
	public void println(String[] msg, PrintWriter out) {
		out.println(">> " + msg[1] + " : " + msg[0]);
		out.flush();
	}
}
