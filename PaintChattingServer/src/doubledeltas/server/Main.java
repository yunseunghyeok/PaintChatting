package doubledeltas.server;

import doubledeltas.environments.*;

import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import doubledeltas.utils.Logger;

public class Main {
	private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
		MysqlConnector con =
				new MysqlConnector("localhost", "root", "smartist2!");
				// new MysqlConnector("192.168.0.136", "guest", "paintchat123@");

		if (!con.isConnected()) {
			Logger.l("MysqlConnector 연결 실패, 서버를 종료합니다.");
			while (!sc.hasNext()) {	}
			return;
		}
		
		ServerSocket server;
		HashMap<Integer /*채팅방 ID*/, Vector<OutputStream> /*온라인 유저의 OS*/> hm;
		
		try {
			server = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
			hm = new HashMap<Integer, Vector<OutputStream>>();	// String: id
			
			Socket socket;
			
			while (true) {
				// 연결 완료 후 진행
				socket = server.accept();
				if (socket != null) {
					Thread th = new ServerThread(con, socket, hm);
					th.start();
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
} // Main class END