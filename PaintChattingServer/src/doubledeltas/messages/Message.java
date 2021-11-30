package doubledeltas.messages;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public abstract class Message {
	protected byte type;
	
	public static final Message translate(DataInputStream dis) throws SocketException {
		try {		
			byte b = dis.readByte();
			switch (b) {
			case TransferCode.LOGIN:
				return new LoginMessage(dis.readUTF(), dis.readUTF());
			case TransferCode.LOGIN_SUC:
				return new LoginSucMessage();
			case TransferCode.LOGIN_FAIL:
				return new LoginFailMessage(dis.readByte());
			case TransferCode.REGISTER:
				return new RegisterMessage(dis.readUTF(), dis.readUTF(), dis.readUTF());
			case TransferCode.REGISTER_SUC:
				return new RegisterSucMessage();
			case TransferCode.REGISTER_FAIL:
				return new RegisterFailMessage(dis.readByte());
			case TransferCode.CONNECTION_CUT:
				return new ConnectionCutMessage();
			case TransferCode.ROOM_ENTER:
				return new RoomEnterMessage(dis.readUTF(), dis.readInt());
			case TransferCode.ROOM_ENTER_SUC:
				return new RoomEnterSucMessage();
			case TransferCode.ROOM_ENTER_FAIL:
				return new RoomEnterFailMessage(dis.readByte());
			case TransferCode.ROOM_SOMEONE_JOINED:
				return new RoomSomeoneJoinedMessage(dis.readUTF());
			case TransferCode.CHAT:
				return new ChatMessage(dis.readInt(), dis.readUTF(), dis.readUTF());
			case TransferCode.CHAT_SUC:
				return new ChatSucMessage(dis.readInt(), dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readUTF());
			case TransferCode.CHAT_FAIL:
				return new ChatFailMessage(dis.readByte());
//			case TransferCode.SEND_IMAGE:
//				return new SendImageMessage(dis.readUTF(), ...);
			case TransferCode.USER_NICK_CHANGE:
				return new UserNickChangeMessage(dis.readUTF(), dis.readUTF());
			case TransferCode.USER_NICK_CHANGE_SUC:
				return new UserNickChangeSucMessage();
			case TransferCode.USER_NICK_CHANGE_FAIL:
				return new UserNickChangeFailMessage(dis.readByte());
			case TransferCode.USER_PROFILE_CHANGE:
				return new UserProfileChangeMessage(dis.readUTF(), dis.readUTF());
			case TransferCode.USER_PROFILE_CHANGE_SUC:
				return new UserProfileChangeSucMessage();
			case TransferCode.USER_PROFILE_CHANGE_FAIL:
				return new UserProfileChangeFailMessage(dis.readByte());
			case TransferCode.ROOM_CREATE:
				return new RoomCreateMessage(dis.readUTF());
			case TransferCode.ROOM_CREATE_SUC:
				return new RoomCreateSucMessage();
			case TransferCode.ROOM_CREATE_FAIL:
				return new RoomCreateFailMessage(dis.readByte());
			default:
				return null;
			}
		}
		catch (SocketException ex) {
			return new ConnectionCutMessage();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	protected void send(DataOutputStream dos) throws IOException {
		dos.write(this.type);
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