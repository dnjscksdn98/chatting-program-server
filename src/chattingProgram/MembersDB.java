package chattingProgram;

import java.sql.*;
import java.util.ArrayList;

public class MembersDB {
	private String url;
	private String query;
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public MembersDB() {
		try {
			this.url = "jdbc:sqlserver://210.115.229.77:2433;DatabaseName=20185143";
			this.query = "select name, id, pw from members";
			con = DriverManager.getConnection(this.url, "20185143", "dnjscksdn98@");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(query);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public ArrayList<Member> loadMembers(){
		ArrayList<Member> loadedMembers = new ArrayList<>();
		
		try {
			rs.beforeFirst();
			while(rs.next()) {
				String name = rs.getString(1);
				String id = rs.getString(2);
				String pw = rs.getString(3);
				
				loadedMembers.add(new Member(name, id, pw));
			}
			
		}catch(Exception e) {
			loadedMembers.clear();
			e.printStackTrace();
		}
		
		return loadedMembers;
	}
	
	public void addMember(Member member) {
		try {
			rs.moveToInsertRow();
			rs.updateString("name", member.getName());
			rs.updateString("id", member.getID());
			rs.updateString("pw", member.getPW());
			rs.insertRow();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
