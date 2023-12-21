import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
	public final static String MYSQL = "com.mysql.cj.jdbc.Driver";
	public static Connection conn = null;
	public static void SetConnection(String db, String id, String pw){
		String conn_url;
		conn_url = "jdbc:mysql://localhost:3306/" + db;
		
		try {
			conn = DriverManager.getConnection(conn_url, id, pw);
			System.out.println("db연결성공");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ResultSet StudentLogin(String[] param){ // {id, pw}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call StudentLogin";
				proc += ParamToString(param);
				System.out.println(proc);
				ResultSet rs = stmt.executeQuery(proc);
				
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){
			return null;
		}
	}
	public static ResultSet ProfessorLogin(String[] param){ // {id, pw}
		try	{
			if(conn != null){
				Statement stmt = conn.createStatement();
				String proc = "call ProfessorLogin";
				proc += ParamToString(param);
				System.out.println(proc);
				ResultSet rs = stmt.executeQuery(proc);
				return rs;
			}
			else{
				return null;
			}
		}
		catch(Exception e){
			return null;
		}
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
	
	
}