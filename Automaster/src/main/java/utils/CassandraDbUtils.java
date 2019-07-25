package utils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraDbUtils {
	
public void printRecords(Session session, String cqlStatement) {
		
		for (Row row : session.execute(cqlStatement)) {
		  System.out.println(row.toString());
		}
		session.close();
	}
	
	public ResultSet executeStatementAndReturnResultSet(Session session, String cqlStatement) {
		return session.execute(cqlStatement);
	}
	
	public Session createSession(String serverIP, String username, String password, int port, String keySpace) {
		Cluster cluster = Cluster.builder().addContactPoints(serverIP).withCredentials(username, password).withPort(port).build();
		Session session = cluster.connect(keySpace);
		return session;
	}

}
