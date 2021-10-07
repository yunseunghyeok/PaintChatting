package doubledeltas.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import doubledeltas.constants.Environment;

public class MysqlConnector {

    private Connection conn;
    
    /**
     * MySQL 서버와 연결됐는지 여부를 가져옴.
     */
    public boolean isConnected() {
    	return conn != null;
    }

    /**
     * MySQL 서버와 연결해 Connection 객체를 가져옴.
     * @param address DB address
     * @param port DB port
     * @param dbUser MySQL username
     * @param dbPassword MySQL password
     * @return
     */
    public MysqlConnector(String address, short port, String dbUser, String dbPassword)
    {
        conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + address + ":" + port,
                    dbUser, dbPassword);
        } catch (Exception e) {}
    }

    /**
     * MySQL 서버와 연결해 Connection 객체를 가져옴. 연결 port는 MySQL 기본값인 3306.
     * @param address DB address
     * @param dbUser MySQL username
     * @param dbPassword MySQL password
     * @return
     */
    MysqlConnector(String address, String dbUser, String dbPassword)
    {
        this(address, Environment.MYSQL_DEFAULT_PORT, dbUser, dbPassword);
    }
    
    /**
     * MySQL 서버에 쿼리를 보내 레코드를 가져오기(select)
     * @param query 보낼 Query문
     * @return 레코드가 있는 ResultSet
     */
    public ResultSet sendQuery(String query) {
    	ResultSet rs = null;
    	try {
        	Statement stmt = conn.createStatement();
        	rs = stmt.executeQuery(query);
    	}
    	catch (SQLException ex) {}
    	return rs;
    }
    
    /**
     * MySQL 서버에 쿼리를 보내 값을 변경시키기(insert, update, delete)
     * @param query 보낼 Query문
     * @return query문에 영향을 받은 레코드 수
     */
    public int sendUpdateQuery(String query) {
    	int affectedRows = 0;
    	try {
        	Statement stmt = conn.createStatement();
        	affectedRows = stmt.executeUpdate(query);
    	}
    	catch (SQLException ex) {}
    	return affectedRows;
    }
    
    // todo
}
