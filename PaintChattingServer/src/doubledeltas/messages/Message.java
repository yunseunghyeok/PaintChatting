package doubledeltas.messages;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public abstract class Message {
	protected byte type;
	
	public static final Message translate(DataInputStream dis) throws SocketException {
		return null;
		
	}
	
	protected void send(DataOutputStream dos) throws IOException {
		dos.writeByte(this.type);
		// write more data...
	}
	
	public void send(Socket socket) throws IOException {
		this.send(new DataOutputStream(socket.getOutputStream()));
	}
}

interface Broadcastable {
	public void send(DataOutputStream os) throws IOException;
	public default void broadcast(
			HashMap<Integer, HashMap<String, DataOutputStream>> hm,
			int roomid
			) throws Exception
	{
		HashMap<String, DataOutputStream> room = null; 
		for (int id : hm.keySet()) {
			if (id == roomid) {
				room = hm.get(id);
				break;
			}
		}
		if (room == null) throw new Exception("Room ID Å½»ö ½ÇÆÐ");
		
		for (String id : room.keySet()) {
			send(room.get(id));
		}
	}
}

interface ClientRecievable {}

interface ServerRecievable {}