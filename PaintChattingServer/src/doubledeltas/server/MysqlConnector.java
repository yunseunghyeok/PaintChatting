package doubledeltas.server;

import java.sql.*;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import doubledeltas.environments.*;
import doubledeltas.utils.Logger;

public class MysqlConnector {

    private Connection conn;
    
    /**
     * MySQL ������ ����ƴ��� ���θ� ������.
     */
    public boolean isConnected() {
    	return conn != null;
    }

    /**
     * MySQL ������ ������ Connection ��ü�� ������.
     * @param address DB �ּ�
     * @param port DB ��Ʈ
     * @param dbUser MySQL ����� �̸�
     * @param dbPassword MySQL �н�����
     */
    public MysqlConnector(String address, int port, String dbUser, String dbPassword)
    {
        conn = null;

        Logger.l("MysqlConnector ���� �õ�...");
        Logger.l("DBMS Address:\t" + address + ":" + port);
        Logger.l("DBMS UserInfo:\t" + dbUser + " / " + dbPassword);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + address + ":" + port,
                    dbUser, dbPassword);
            Logger.l("MysqlConnector ���� ����!");
        } catch (ClassNotFoundException e) {
            Logger.l("MySqlConnector ���� ����: Connector J ���� ����");
            e.printStackTrace();
        } catch (CommunicationsException e) {
            Logger.l("MySqlConnector ���� ����: �ּ� ���� ����");
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            Logger.l("MySqlConnector ���� ����: DB �α׿� Ÿ�Ӿƿ�");
            e.printStackTrace();
        } catch (SQLException e) {
            Logger.l("MySqlConnector ���� ����: DB �α׿� ����");
            e.printStackTrace();
        } catch (Exception e) {
            Logger.l("MySqlConnector ���� ����: ���� �Ҹ�");
            e.printStackTrace();
        }
    }

    /**
     * MySQL ������ ������ Connection ��ü�� ������. ���� port�� MySQL �⺻���� 3306.
     * @param address DB �ּ�
     * @param dbUser MySQL ����� �̸�
     * @param dbPassword MySQL �н�����
     */
    MysqlConnector(String address, String dbUser, String dbPassword)
    {
        this(address, Environment.MYSQL_DEFAULT_PORT, dbUser, dbPassword);
    }
    
    /**
     * MySQL ������ ������ ���� ���ڵ带 ��������(select)
     * @param query ���� Query��
     * @return ���ڵ尡 �ִ� <code>ResultSet</code>
     * @throws SQLException SQL���� ������ ���� ��
     */
    public ResultSet sendQuery(String query) throws SQLException {
    	ResultSet rs = null;
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        return rs;
    }
    
    /**
     * MySQL ������ ������ ���� ���� �����Ű��(insert, update, delete)
     * @param query ���� Query��
     * @return query���� ������ ���� ���ڵ� ��
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
}
