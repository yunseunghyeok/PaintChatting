package doubledeltas.server;

/*
todo:
- server-db 연결
- db에서 원하는 정보 가져오도록 query 보내기
 */

import java.sql.*;

public class Main {
    public static short MYSQL_DEFAULT_PORT = 3306;

    public static Connection getConn(String address, short port, String user, String password)
    {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + address + ":" + port,
                    user, password);
        } catch (Exception e) {}
        return conn;
    }

    public static void main(String[] args)
    {
        Connection conn = getConn("localhost", MYSQL_DEFAULT_PORT, "root", "smartist2!");
        if (conn != null) {
            System.out.printf("DB 연결 완료");
        }
        else {
            System.out.printf("DB 연결 에러");
        }
    }
}
