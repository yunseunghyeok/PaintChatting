package doubledeltas.server;

/*
todo:
- server-db 연결
- db에서 원하는 정보 가져오도록 query 보내기
 */

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // MySQL 드라이버 로드
            Connection conn = DriverManager.getConnection("jdbc:mysql:"
                    + "localhost:3306/sampledb", "root", "password"
                    );
            System.out.println("DB 연결 완료");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 에러");
        } catch (SQLException e) {
            System.out.println("DB 연결 에러");
        }
    }
}
