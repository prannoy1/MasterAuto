package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class SQLDbUtils {	
	final static Logger logger = Logger.getLogger(SQLDbUtils.class);

	String driverClassName;
	String url;
	String userName;
	String password;
	
	public String executeQueryWhichReturnSingleValue(String query) throws ClassNotFoundException, SQLException {		
		logger.info("query: "+query);
		logger.info("driverClassName: "+driverClassName);
		logger.info("url: "+url);
		logger.info("userName: "+userName);
		logger.info("password: "+password);

		Connection con = createConnection(driverClassName, url, userName, password);  
		ResultSet rs = executeSelectQuery(query, con);  
		String value=null;
		if(rs.next()) {
			value = rs.getString(1);
		}  
		con.close();  
		logger.info("value: "+value);
		return value;
		}
	
	public ArrayList<String> executeQueryWhichReturnOneDimensionalArraylist(String query) throws ClassNotFoundException, SQLException {	
		logger.info("query: "+query);
		logger.info("driverClassName: "+driverClassName);
		logger.info("url: "+url);
		logger.info("userName: "+userName);
		logger.info("password: "+password);

		Connection con = createConnection(driverClassName, url, userName, password);  
		ResultSet rs = executeSelectQuery(query, con);  
		String value=null;
		ArrayList<String> list=new ArrayList<String>();  
		while(rs.next()) {
		value = rs.getString(1);
		list.add(value);
		}  
		con.close();  
		logger.info("value: "+value);
		return list;
		}
	
	private static ResultSet executeSelectQuery(String query, Connection con) throws SQLException {
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery(query);
		return rs;
	}
	private static Connection createConnection(String driverClassName, String url, String userName, String password)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverClassName);  
		Connection con=DriverManager.getConnection(url,userName,password);
		return con;
	}
	public void initialize(String driverClassName, String url, String userName, String password) {
		this.driverClassName=driverClassName;
		this.url=url;
		this.userName=userName;
		this.password=password;
	}
	
	
	public ResultSet executeQueryWhichReturnMultipleValue(String query) throws ClassNotFoundException, SQLException {
		Connection con = createConnection(driverClassName, url, userName, password);  
		return  executeSelectQuery(query, con); 
	}

	public int executeUpdate(String query) throws ClassNotFoundException, SQLException {	
		logger.info("query: "+query);
		logger.info("driverClassName: "+driverClassName);
		logger.info("url: "+url);
		logger.info("userName: "+userName);
		logger.info("password: "+password);

		Connection con = createConnection(driverClassName, url, userName, password);  
		int updateCount = executeUpdate(query, con);  
		return updateCount;
	}
	
	private static int executeUpdate(String query, Connection con) throws SQLException {
		Statement stmt=con.createStatement();  
		int updateCount=stmt.executeUpdate(query);
		return updateCount;
	}
}
