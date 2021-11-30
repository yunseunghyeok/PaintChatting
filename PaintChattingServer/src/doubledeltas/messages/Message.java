package doubledeltas.messages;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import doubledeltas.environments.TransferCode;
import doubledeltas.server.ServerThread;
import doubledeltas.utils.Logger;
import doubledeltas.messages.*;

public abstract class Message {
	protected byte[] bytes;
	
	public static final Message translate(InputStream is) throws SocketException {
		try {		
			int b = is.read();
			byte[] bytes;
			switch ((byte)b) {
			case TransferCode.LOGIN:
				bytes = new byte[LoginMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new LoginMessage(bytes);
				
			case TransferCode.LOGIN_SUC:
				bytes = new byte[LoginSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new LoginSucMessage(bytes);
				
			case TransferCode.LOGIN_FAIL:
				bytes = new byte[LoginFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new LoginFailMessage(bytes);
				
			case TransferCode.REGISTER:
				bytes = new byte[RegisterMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				is.read(bytes);
				return new RegisterMessage(bytes);
				
			case TransferCode.REGISTER_SUC:
				bytes = new byte[RegisterSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				is.read(bytes);
				return new RegisterSucMessage(bytes);
				
			case TransferCode.REGISTER_FAIL:
				bytes = new byte[RegisterFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				is.read(bytes);
				return new RegisterFailMessage(bytes);
				
			case TransferCode.CONNECTION_CUT:
				bytes = new byte[ConnectionCutMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new ConnectionCutMessage(bytes);
				
			case TransferCode.ROOM_ENTER:
				bytes = new byte[RoomEnterMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomEnterMessage(bytes);
				
			case TransferCode.ROOM_ENTER_SUC:
				bytes = new byte[RoomEnterSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomEnterSucMessage(bytes);
				
			case TransferCode.ROOM_ENTER_FAIL:
				bytes = new byte[RoomEnterFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomEnterFailMessage(bytes);
				
			case TransferCode.ROOM_SOMEONE_JOINED:
				bytes = new byte[RoomSomeoneJoinedMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomSomeoneJoinedMessage(bytes);
				
			case TransferCode.CHAT:
				bytes = new byte[ChatMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new ChatMessage(bytes);
				
			case TransferCode.CHAT_SUC:
				bytes = new byte[ChatSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new ChatSucMessage(bytes);
				
			case TransferCode.CHAT_FAIL:
				bytes = new byte[RoomCreateMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new ChatFailMessage(bytes);
				
//			case TransferCode.SEND_IMAGE:
//				bytes = new byte[SendImageMessage.MSG_SIZE];
//				bytes[0] = (byte) b;
//				is.read(bytes, 1, bytes.length-1);
//				return new SendImageMessage(bytes);
				
			case TransferCode.USER_NICK_CHANGE:
				bytes = new byte[UserNickChangeMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserNickChangeMessage(bytes);
				
			case TransferCode.USER_NICK_CHANGE_SUC:
				bytes = new byte[RoomCreateMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserNickChangeSucMessage(bytes);
				
			case TransferCode.USER_NICK_CHANGE_FAIL:
				bytes = new byte[UserNickChangeFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserNickChangeFailMessage(bytes);
				
			case TransferCode.USER_PROFILE_CHANGE:
				bytes = new byte[UserProfileChangeMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserProfileChangeMessage(bytes);
				
			case TransferCode.USER_PROFILE_CHANGE_SUC:
				bytes = new byte[UserProfileChangeSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserProfileChangeSucMessage(bytes);
				
			case TransferCode.USER_PROFILE_CHANGE_FAIL:
				bytes = new byte[UserProfileChangeFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new UserProfileChangeFailMessage(bytes);
				
			case TransferCode.ROOM_CREATE:
				bytes = new byte[RoomCreateMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomCreateMessage(bytes);				

			case TransferCode.ROOM_CREATE_SUC:
				bytes = new byte[RoomCreateSucMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomCreateSucMessage(bytes);

			case TransferCode.ROOM_CREATE_FAIL:
				bytes = new byte[RoomCreateFailMessage.MSG_SIZE];
				bytes[0] = (byte) b;
				is.read(bytes, 1, bytes.length-1);
				return new RoomCreateFailMessage(bytes);
				
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
			) throws Exception
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