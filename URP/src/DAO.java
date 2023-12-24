import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
	public final static String MYSQL = "com.mysql.cj.jdbc.Driver";
	public static Connection conn = null;
	public static Connection SetConnection(String db, String id, String pw){
		String conn_url;
		conn_url = "jdbc:mysql://localhost:3306/" + db;
		
		try {
			conn = DriverManager.getConnection(conn_url, id, pw);
			System.out.println("db연결성공");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public static String ParamToString(String[] param)
	{
		if (param == null)
		{
			return null;
		}
		
		String proc = "";
		proc += "(";
		for(int i=0; i<param.length; i++){
			if(i!=0) proc+=",";
			proc += param[i];
		}	
		proc += ");";
		return proc;
	}
	public static ResultSet StudentLogin(String[] param){ // {id, pw}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call StudentLogin";
				proc += ParamToString(param);
				ResultSet rs = stmt.executeQuery(proc);
				
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet ProfessorLogin(String[] param){ // {id, pw}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call ProfessorLogin";
				proc += ParamToString(param);
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet GetStudentTimeTable(int studentId) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call GetStudentTimeTable(?)}");
	            cstmt.setInt(1, studentId);

	            cstmt.execute();
	            return cstmt.getResultSet();
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static ResultSet GetDepartmentID(String[] param){ // {department}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call GetDepartmentID";
				proc += ParamToString(param);
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet GetAdvisorID(int param){ // {departmentid}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call GetAdvisorID";
				String pa = "(" + param + ")";
				proc += pa;
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static void SignUp(String sname, String id, String pw, int departmentid, String birthdate, int advisorid){ 
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call SignUp";
				String pa = "('" + sname + "','" + id + "','" + pw + "'," + departmentid
						+ ",'" + birthdate + "'," + advisorid + ")";
				proc += pa;
				stmt.executeQuery(proc);
			}
		}
		catch(Exception e){}
	}
	public static ResultSet IDduplication(String[] param){ // {id}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call IDduplication";
				proc += ParamToString(param);
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet GetStudent(int sid){ // {SID}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call GetStudent";
				String pa = "(" + sid + ")";
				proc += pa;
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet ChangeStudent(int sid, String sname, 
			String id, String pw, String birthdate){ // {SID, sname, id, pw, birthdate}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call ChangeStudent";
				String pa = "(" + sid + ",'" + sname + "','" + id + "','" + pw + "','" + birthdate + "')";
				proc += pa;
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){return null;}
	}
	public static ResultSet GetClassTime(int pid){ // {PID}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call GetClassTime";
				String pa = "(" + pid + ")";
				proc += pa;
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e) {return null;}
	}
	public static void CreateClass(String name, int pid, String room, String start, 
			String end, String day, String grade){ 
		// {classname, PID, classroom, classstarttime, classendtime, classdayoftheweek, grades}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call CreateClass";
				String pa = "('" + name + "'," + pid + ",'" + room + "','" + start + "','" + end
						+ "','" + day + "'," + grade + ")";
				proc += pa;
				stmt.executeQuery(proc);
			}
		}
		catch(Exception e){}
	}
	public static ResultSet GetProfessorClass(int pid){ // {PID}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call GetProfessorClass";
				String pa = "(" + pid + ")";
				proc += pa;
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e) {return null;}
	}
	public static void UpdateClass(int cid, String name, String room, String start, 
			String end, String day, String grade){ 
		// {CID, classname, classroom, classstarttime, classendtime, classdayoftheweek, grades}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call UpdateClass";
				String pa = "(" + cid + ",'" + name + "','" + room + "','" + start + "','" + end
						+ "','" + day + "'," + grade + ")";
				proc += pa;
				stmt.executeQuery(proc);
			}
		}
		catch(Exception e){}
	}
	public static void DeleteClass(int cid){ // {CID}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call DeleteClass";
				String pa = "(" + cid + ")";
				proc += pa;
				stmt.executeQuery(proc);
			}
			else{}
		}
		catch(Exception e) {}
	}
	
	public static ResultSet GetClassList() {
	    try {
	        if (conn != null) {
	            Statement stmt = conn.createStatement();
	            String proc = "CALL GetClassList()";
	            return stmt.executeQuery(proc);
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static void RegisterClass(int sid, int cid) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call RegisterClass(?, ?)}");
	            cstmt.setInt(1, sid);
	            cstmt.setInt(2, cid);
	            cstmt.executeUpdate();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static String CancelClass(int studentId, int classId) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call CancelClass(?, ?)}");
	            cstmt.setInt(1, studentId);
	            cstmt.setInt(2, classId);

	            cstmt.execute();

	            ResultSet rs = cstmt.getResultSet();
	            if (rs.next()) {
	                return rs.getString("Status");
	            }
	        }
	        return "Error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error";
	    }
	}
	public static ResultSet GetStudentCurrentClasses(int studentId) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call GetStudentCurrentClasses(?)}");
	            cstmt.setInt(1, studentId);

	            cstmt.execute();
	            return cstmt.getResultSet();
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public static ResultSet GetStudentGrades(int studentId) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call GetStudentGrades(?)}");
	            cstmt.setInt(1, studentId);

	            cstmt.execute();
	            return cstmt.getResultSet();
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public static void UpdateStudentGrade(int profId, int classId, int studentId, float newScore) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call UpdateStudentGrade(?, ?, ?, ?)}");
	            cstmt.setInt(1, profId);
	            cstmt.setInt(2, classId);
	            cstmt.setInt(3, studentId);
	            cstmt.setFloat(4, newScore);

	            cstmt.executeUpdate();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static ResultSet GetClassStudents(int classId) {
	    try {
	        if (conn != null) {
	            CallableStatement cstmt = conn.prepareCall("{call GetClassStudents(?)}");
	            cstmt.setInt(1, classId);

	            cstmt.execute();
	            return cstmt.getResultSet();
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}



