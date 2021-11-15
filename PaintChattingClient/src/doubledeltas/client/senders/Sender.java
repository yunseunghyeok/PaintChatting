package doubledeltas.client.senders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import doubledeltas.environments.Environment;
import doubledeltas.environments.TransferCode;

public abstract class Sender extends Thread {
	OutputStream os = null;
	Socket		 socket = null;
	byte[]		 msg = null;
	
	@Override
	public void run() {
		try {
			// 서버에 연결
			socket = new Socket(Environment.SERVER_IP_ADDRESS, Environment.CLIENT_TO_SERVER_PORT);
			os = socket.getOutputStream();
			os.write(msg);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
