package doubledeltas.messages;

import java.io.*;
import java.net.Socket;

import doubledeltas.environments.TransferCode;

public abstract class Message {
	protected byte[] bytes;
	
	public static Message translate(byte[] bytes) {
		switch (TransferCode.valueOf(Byte.toString(bytes[0]))) {
		case LOGIN: 
			return new LoginMessage(bytes);
		case LOGIN_SUC:
			return new LoginSucMessage(bytes);
		case LOGIN_FAIL:
			return new LoginFailMessage(bytes);
		case REGISTER:
			return new RegisterMessage(bytes);
		default:
			return null;
		}
	}
	
	public void send(OutputStream os) throws IOException {
		os.write(bytes);
		os.flush();
	}
	
	public void send(Socket socket) throws IOException {
		this.send(socket.getOutputStream());
	}
}