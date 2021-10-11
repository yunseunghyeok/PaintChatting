package doubledeltas.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

public class Main {
	private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
    	MysqlConnector	con	= new MysqlConnector("localhost", "root", "smartist2!");
        
        if (!con.isConnected()) {
        	Logger.l("MysqlConnector 연결 실패, 서버를 종료합니다.");
        	while (!sc.hasNext()) {}
        	return;
        }
        
    	MyThread th = new MyThread();
		th.start();
    }

	private static class MyThread extends Thread {
		@Override
		public void run() {
			BufferedReader	in			= null;
			BufferedWriter	out			= null;
			ServerSocket	listener	= null;
			Socket			socket		= null;
			CommandParser	parser		= new CommandParser();
			
			while (true) {
				try {
    				listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
    				socket = listener.accept();	// 연결 대기, 이후 연결 완료
    				System.out.print(socket.getInetAddress().toString() + "\tsaid ");
    				
    				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    				
    				String inMsg = in.readLine();
    				System.out.println(inMsg);
    				
    				parser.parse(inMsg);
    			}
    			catch (IOException ex) {
    				System.out.print(ex.getMessage());
    			}
    			finally {
    				try {
    					socket.close();
    					listener.close();
    				}
    				catch (Exception ex) {
						ex.printStackTrace();
    					Logger.l("클라이언트와 통신 중 오류가 발생해 연결이 중단되었습니다.");
    				}
    			}
			} // while-loop END
		} // run method END
    } // MyThread END
    
} // Main class END