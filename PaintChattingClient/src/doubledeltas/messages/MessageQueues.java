package doubledeltas.messages;

import java.io.DataInputStream;
import java.io.File;
<<<<<<< HEAD
import java.io.FileInputStream;
=======
>>>>>>> hakfe
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

import doubledeltas.environments.Environment;

/**
 * <code>DataInputStream</code>으로부터 메시지를 입력받아 타입 별로 큐잉(Queueing)하는 객체
 * @author doubledeltas
 * @see doubledeltas.messages.Message
 */
public class MessageQueues implements Runnable {
	DataInputStream dis;
	HashMap<Byte, LinkedList<Message>> hm;
	
	/**
	 * <code>DataInputStream</code>으로부터 메시지를 입력받아 타입 별로 큐잉(Queueing)하는 객체
	 * @param dis 메시지 데이터를 입력받은 <code>DataInputStream</code> 객체
	 * @see doubledeltas.messages.Message
	 */
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
					int cnt = dis.readInt();
					String[] rooms = new String[cnt];
					for (int i=0; i<cnt; i++)	rooms[i] = dis.readUTF();
					msg = new LoginSucMessage(rooms);
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
				case TransferCode.SEND_IMAGE:
					String fileName = dis.readUTF();
					long fileLength = dis.readLong();
					String filePath = Environment.FILE_DIR + "\\" + fileName;
					File file = new File(filePath);
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buf;
					long len;
					if (!file.exists()) {
						file.createNewFile();
					}
					while ((len = Math.min(4096L, fileLength)) > 0) {
						buf = new byte[(int) len];
						dis.read(buf);
						fos.write(buf);
						fileLength -= len;
					}
					fos.close();
					msg = new SendImageMessage(fileName, file);
					break;
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
				} // switch-end
				enqueue(b, msg);
			}
		}
		catch (SocketException ex) {
			enqueue(TransferCode.CONNECTION_CUT, new ConnectionCutMessage());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 메시지 타입이 <code>type</code>인 메시지의 입력을 기다림
	 * @param type 메시지 타입
	 * @param timeout 대기 제한시간
	 * @return 전송받은 메시지, 제한시간 초과 시 <code>null</code>
	 * @see doubledeltas.messages.Message
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

	/**
	 * 메시지 타입이 <code>type</code>인 메시지의 입력을 기다림
	 * @param type 메시지 타입
	 * @return 전송받은 메시지
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage(byte type) {
		return waitForMessage(type, Long.MAX_VALUE);
	}

	/**
	 * 아무 메시지나 입력되기를 제한없이 기다림
	 * @param timeout 대기 제한시간
	 * @return 전송받은 메시지, 제한시간 초과 시 <code>null</code>
	 * @see doubledeltas.messages.Message
	 */
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
	
	/**
	 * 아무 메시지나 입력되기를 제한없이 기다림
	 * @return 전송받은 메시지
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage() {
		return waitForMessage(Long.MAX_VALUE);
	}
	
	/**
	 * types의 원소 중 하나를 타입으로 하는 메시지가 입력되기 기다림
	 * @param types 메시지 타입들
	 * @param timeout 대기 제한시간
	 * @return 전송받은 메시지, 제한시간 초과시 <code>null</code>
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForAnyMessage(byte[] types, long timeout) {
		long startTime = System.currentTimeMillis();
		LinkedList<Message> queue;
		while (true) {
			synchronized(hm) {
				for (byte type: types) {
					queue = hm.get(type);
					if (queue == null) continue;
					if (!queue.isEmpty())
						return queue.poll();
				}
			}
			if (System.currentTimeMillis() - startTime > timeout)
				return null;
		}
	}

	/**
	 * <code>types</code>의 원소 중 하나를 타입으로 하는 메시지가 입력되기를 제한없이 기다림
	 * @return 전송받은 메시지
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForAnyMessage(byte[] types) {
		return waitForAnyMessage(types, Long.MAX_VALUE);
	}
	
	/**
	 * types의 원소를 타입으로 하는 모든 메시지가 입력되기 기다림
	 * @param types	메시지 타입들
	 * @param timeout 대기 제한시간
	 * @return 타입과 메시지가 대응된 해시맵, 시간 초과 시 null.
	 */
	public HashMap<Byte, Message> waitForAllMessages(byte[] types, long timeout) {
		long startTime = System.currentTimeMillis();
		HashMap<Byte, Message> res = new HashMap<Byte, Message>();
		LinkedList<Message> queue;
		int cnt = 0;
		while (true) {
			for(byte type: types) {
				if (res.get(type) != null) continue;
				queue = hm.get(type);
				if (queue == null) continue;
				if (!queue.isEmpty()) {
					res.put(type, queue.poll());
					if (cnt == types.length) return res;
					else cnt++;
				}
			}
			if (System.currentTimeMillis() - startTime > timeout)
				return null;
		}
	}
	/**
	 * types의 원소를 타입으로 하는 모든 메시지가 입력되기를 제한없이 기다림
	 * @param types	메시지 타입들
	 * @return 타입과 메시지가 대응된 해시맵
	 */
	public HashMap<Byte, Message> waitForAllMessages(byte[] types) {
		return waitForAllMessages(types, Long.MAX_VALUE);
	}
}
