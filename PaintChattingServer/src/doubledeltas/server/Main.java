package doubledeltas.server;

import doubledeltas.environments.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import doubledeltas.utils.Logger;

public class Main {
	private static Scanner sc = new Scanner(System.in);
	private static Thread th;
	private static ServerSocket server;

    public static void main(String[] args) {
		
		MysqlConnector con =
				new MysqlConnector("localhost", "root", "smartist2!");
				// new MysqlConnector("192.168.0.136", "guest", "paintchat123@");

		if (!con.isConnected()) {
			Logger.l("MysqlConnector ���� ����, ������ �����մϴ�.");
			return;
		}
		
		HashMap<Integer /*ä�ù� ID*/, HashMap<String /*���̵�*/, DataOutputStream /*�¶��� ������ OS*/>> hm;
		
		try {
			server = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
			hm = new HashMap<Integer, HashMap<String, DataOutputStream>>();	// String: id
			
			Socket socket;
			
			/////////////// ä�ù� �ҷ����� ////////////////
			int roomid;
			String userid;
			ResultSet rs;
			int cnt = 0;
			rs = con.sendQuery(
					"SELECT id FROM chatroom");
			while (rs.next()) {
				roomid = rs.getInt("id");
				hm.put(roomid, new HashMap<String, DataOutputStream>());
				cnt++;
			}
			Logger.l(String.format("%d���� ä�ù� �ε� ����!", cnt));

			Logger.l("�ε� �Ϸ�!");
			
			//////////// server thread ���� ////////////
			while (true) {
				// ���� �Ϸ� �� ����
				socket = server.accept();
				if (socket != null) {
					Thread th = new ServerThread(con, socket, hm);
					th.start();
				}
			}
			//////////////////////////////////////////
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
} // Main class END