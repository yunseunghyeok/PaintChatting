package doubledeltas.server;

import java.sql.*;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

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
     * @param address DB 주소
     * @param port DB 포트
     * @param dbUser MySQL 사용자 이름
     * @param dbPassword MySQL 패스워드
     */
    public MysqlConnector(String address, int port, String dbUser, String dbPassword)
    {
        conn = null;

        Logger.l("MysqlConnector 연결 시도...");
        Logger.l("DBMS Address:\t" + address + ":" + port);
        Logger.l("DBMS UserInfo:\t" + dbUser + " / " + dbPassword);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + address + ":" + port,
                    dbUser, dbPassword);
            Logger.l("MysqlConnector 연결 성공!");
        } catch (ClassNotFoundException e) {
            Logger.l("MySqlConnector 연결 실패: Connector J 연결 실패");
            e.printStackTrace();
        } catch (CommunicationsException e) {
            Logger.l("MySqlConnector 연결 실패: 주소 연결 실패");
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            Logger.l("MySqlConnector 연결 실패: DB 로그온 타임아웃");
            e.printStackTrace();
        } catch (SQLException e) {
            Logger.l("MySqlConnector 연결 실패: DB 로그온 실패");
            e.printStackTrace();
        }
    }

    /**
     * MySQL 서버와 연결해 Connection 객체를 가져옴. 연결 port는 MySQL 기본값인 3306.
     * @param address DB 주소
     * @param dbUser MySQL 사용자 이름
     * @param dbPassword MySQL 패스워드
     */
    MysqlConnector(String address, String dbUser, String dbPassword)
    {
        this(address, Environment.MYSQL_DEFAULT_PORT, dbUser, dbPassword);
    }
    
    /**
     * MySQL 서버에 쿼리를 보내 레코드를 가져오기(select)
     * @param query 보낼 Query문
     * @return 레코드가 있는 <code>ResultSet</code>
     * @throws SQLException SQL문에 오류가 있을 때
     */
    public ResultSet sendQuery(String query) throws SQLException {
    	ResultSet rs = null;
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return affectedRows;
    }
<<<<<<< HEAD
    
    /**
     * ID 유뮤 확인
     * @param id	확인할 ID
     */
    public boolean doesIDExist(String id) {
    	String driver = "";
    	String url = "";
    	String sql = "SELECT ID" + "FROM user_list WHERE User_ID = ";
    	
    	conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	try {
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url);
    		pstmt = conn.prepareStatement(sql + "'" + id + "'");
    		rs = pstmt.executeQuery();
    		
    		if(rs.getString("User_ID") == id) {
    			return false;
    		}
    		else {
    			return true;
    		}
    	}
    	catech(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    /**
     * 로그인 시도, 성공 시 
     * @param id
     * @param pw
     * @return User 객체, 실패시 null
     */
    public User login(String id, String pw) {
    	String Nick, Font, ProfilePicture;
    	int ChatID, num = 0;
    	
    	String driver = "";
    	String url = "";
    	String sql = "SELECT ID" + "FROM user_list WHERE User_ID = ";
    	
    	conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	try {
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url);
    		pstmt = conn.prepareStatement(sql + "'" + id + "'");
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()) {
    			Nick = rs.getString("User_NickName");
    			Font = rs.getString("Font");
    			ProfilePicture = rs.getString("Profile_Picture");
    			ChatID = rs.getInt("ChatID");
    			//정보 보내기
    		}
    	}
    	catech(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    
=======
>>>>>>> doubledeltas
}
