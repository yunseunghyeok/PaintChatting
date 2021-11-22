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
			Logger.l("MysqlConnector ���� ����, ������ �����մϴ�.");
			while (!sc.hasNext()) {	}
			return;
		}
		
		ServerSocket server;
		HashMap<Integer /*ä�ù� ID*/, Vector<OutputStream> /*�¶��� ������ OS*/> hm;
		
		try {
			server = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
			hm = new HashMap<Integer, Vector<OutputStream>>();	// String: id
			
			Socket socket;
			
			while (true) {
				// ���� �Ϸ� �� ����
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