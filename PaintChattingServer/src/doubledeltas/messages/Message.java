package doubledeltas.messages;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import doubledeltas.environments.TransferCode;
import doubledeltas.server.ServerThread;

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
		case REGISTER_SUC:
			return new RegisterSucMessage(bytes);
		case REGISTER_FAIL:
			return new RegisterFailMessage(bytes);
		case CONNECTION_CUT:
			return new ConnectionCutMessage(bytes);
		case ROOM_ENTER:
			return new RoomEnterMessage(bytes);
		case ROOM_ENTER_SUC:
			return new RoomEnterSucMessage(bytes);
		case ROOM_ENTER_FAIL:
			return new RoomEnterFailMessage(bytes);
		case ROOM_SOMEONE_JOINED:
			return new RoomSomeoneJoinedMessage(bytes);
		case CHAT:
			return new ChatMessage(bytes);
		case CHAT_SUC:
			return new ChatSucMessage(bytes);
		case CHAT_FAIL:
			return new ChatFailMessage(bytes);
//		case SEND_IMAGE:
//			return new SendImageMessage(bytes);
		case USER_NICK_CHANGE:
			return new UserNickChangeMessage(bytes);
		case USER_NICK_CHANGE_SUC:
			return new UserNickChangeSucMessage(bytes);
		case USER_NICK_CHANGE_FAIL:
			return new UserNickChangeFailMessage(bytes);
		case USER_PROFILE_CHANGE:
			return new UserProfileChangeMessage(bytes);
		case USER_PROFILE_CHANGE_SUC:
			return new UserProfileChangeSucMessage(bytes);
		case USER_PROFILE_CHANGE_FAIL:
			return new UserProfileChangeFailMessage(bytes);
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

interface Broadcastable {
	public void send(OutputStream os) throws IOException;
	public default void broadcast(
			HashMap<Integer, HashMap<String, OutputStream>> hm,
			int roomid
			) throws IOException
	{
		HashMap<String, OutputStream> room = null; 
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