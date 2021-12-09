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
			Logger.l("MysqlConnector 연결 실패, 서버를 종료합니다.");
			return;
		}
		
		HashMap<Integer /*채팅방 ID*/, HashMap<String /*아이디*/, DataOutputStream /*온라인 유저의 OS*/>> hm;
		
		try {
			server = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
			hm = new HashMap<Integer, HashMap<String, DataOutputStream>>();	// String: id
			
			Socket socket;
			
			/////////////// 채팅방 불러오기 ////////////////
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
			Logger.l(String.format("%d개의 채팅방 로딩 성공!", cnt));

			Logger.l("로딩 완료!");
			
			//////////// server thread 생성 ////////////
			while (true) {
				// 연결 완료 후 진행
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