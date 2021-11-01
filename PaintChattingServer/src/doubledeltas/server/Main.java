package doubledeltas.server;

import java.util.Scanner;

import doubledeltas.thread.PollerThread;
import doubledeltas.utils.Logger;

public class Main {
	private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
		MysqlConnector con =
				// new MysqlConnector("localhost", "root", "smartist2!");
				// new MysqlConnector("localhost", "guest", "smartist2!");
				new MysqlConnector("192.168.0.136", "guest", "paintchat123@");

		if (!con.isConnected()) {
			Logger.l("MysqlConnector 연결 실패, 서버를 종료합니다.");
			while (!sc.hasNext()) {	}
			return;
		}

		PollerThread th = new PollerThread();
		th.start();
	}
} // Main class END