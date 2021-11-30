package doubledeltas.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

public class MessageQueues implements Runnable {
	DataInputStream dis;
	HashMap<Byte, LinkedList<Message>> hm;
	
	public MessageQueues(DataInputStream dis) {
		this.hm = new HashMap<Byte, LinkedList<Message>>();
		this.dis = dis;
		
		new Thread(this).start();
	}

	private void enqueue(byte type, Message msg) {
		if (msg == null) return;
		synchronized (hm) {
			if (hm.get(type) == null) {
				hm.put(type, new LinkedList<Message>());
			}
			hm.get(type).add(msg);
		}
	}
	
	@Override
	public void run() {
		Message msg = null;
		try {
			while (true) {
				byte b = dis.readByte();
				switch (b) {
				case TransferCode.LOGIN:
					msg = new LoginMessage(dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.LOGIN_SUC:
					msg = new LoginSucMessage();
					break;
				case TransferCode.LOGIN_FAIL:
					msg = new LoginFailMessage(dis.readByte());
					break;
				case TransferCode.REGISTER:
					msg = new RegisterMessage(dis.readUTF(), dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.REGISTER_SUC:
					msg = new RegisterSucMessage();
					break;
				case TransferCode.REGISTER_FAIL:
					msg = new RegisterFailMessage(dis.readByte());
					break;
				case TransferCode.CONNECTION_CUT:
					msg = new ConnectionCutMessage();
					break;
				case TransferCode.ROOM_ENTER:
					msg = new RoomEnterMessage(dis.readUTF(), dis.readInt());
					break;
				case TransferCode.ROOM_ENTER_SUC:
					msg = new RoomEnterSucMessage();
					break;
				case TransferCode.ROOM_ENTER_FAIL:
					msg = new RoomEnterFailMessage(dis.readByte());
					break;
				case TransferCode.ROOM_SOMEONE_JOINED:
					msg = new RoomSomeoneJoinedMessage(dis.readUTF());
					break;
				case TransferCode.CHAT:
					msg = new ChatMessage(dis.readInt(), dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.CHAT_SUC:
					msg = new ChatSucMessage(dis.readInt(), dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.CHAT_FAIL:
					msg = new ChatFailMessage(dis.readByte());
					break;
//				case TransferCode.SEND_IMAGE:
//					msg = new SendImageMessage(dis.readUTF(), ...);
//					break;
				case TransferCode.USER_NICK_CHANGE:
					msg = new UserNickChangeMessage(dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.USER_NICK_CHANGE_SUC:
					msg = new UserNickChangeSucMessage();
					break;
				case TransferCode.USER_NICK_CHANGE_FAIL:
					msg = new UserNickChangeFailMessage(dis.readByte());
					break;
				case TransferCode.USER_PROFILE_CHANGE:
					msg = new UserProfileChangeMessage(dis.readUTF(), dis.readUTF());
					break;
				case TransferCode.USER_PROFILE_CHANGE_SUC:
					msg = new UserProfileChangeSucMessage();
					break;
				case TransferCode.USER_PROFILE_CHANGE_FAIL:
					msg = new UserProfileChangeFailMessage(dis.readByte());
					break;
				case TransferCode.ROOM_CREATE:
					msg = new RoomCreateMessage(dis.readUTF());
					break;
				case TransferCode.ROOM_CREATE_SUC:
					msg = new RoomCreateSucMessage();
					break;
				case TransferCode.ROOM_CREATE_FAIL:
					msg = new RoomCreateFailMessage(dis.readByte());
					break;
				default:
					break;
				}
				enqueue(b, msg);
			}
		}
		catch (SocketException ex) {
			msg = new ConnectionCutMessage();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * 메시지 타입이 <code>type</code>인 메시지의 입력을 기다림
	 */
	public Message waitForMessage(byte type, long timeout) {
		long startTime = System.currentTimeMillis();
		LinkedList<Message> queue;
		while (true) {
			synchronized(hm) {
				queue = hm.get(type);
				if (queue == null) continue;
				if (!queue.isEmpty())
					return queue.poll();
			}
			if (System.currentTimeMillis() - startTime > timeout)
				return null;
		}
	}
	
	public Message waitForMessage(byte type) {
		return waitForMessage(type, Long.MAX_VALUE);
	}
	
	public Message waitForMessage(long timeout) {
		long startTime = System.currentTimeMillis();
		LinkedList<Message> queue;
		while (true) {
			synchronized (hm) {
				for (byte b: hm.keySet()) {
					queue = hm.get(b);
					if (!queue.isEmpty())
						return queue.poll();
				}
			}
			if (System.currentTimeMillis() - startTime > timeout)
				return null;
		}
	}
	
	public Message waitForMessage() {
		return waitForMessage(Long.MAX_VALUE);
	}
}
