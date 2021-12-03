package doubledeltas.messages;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

import doubledeltas.environments.Environment;

/**
 * <code>DataInputStream</code>���κ��� �޽����� �Է¹޾� Ÿ�� ���� ť��(Queueing)�ϴ� ��ü
 * @author doubledeltas
 * @see doubledeltas.messages.Message
 */
public class MessageQueues implements Runnable {
	DataInputStream dis;
	HashMap<Byte, LinkedList<Message>> hm;
	
	/**
	 * <code>DataInputStream</code>���κ��� �޽����� �Է¹޾� Ÿ�� ���� ť��(Queueing)�ϴ� ��ü
	 * @param dis �޽��� �����͸� �Է¹��� <code>DataInputStream</code> ��ü
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
						buf = dis.readNBytes((int)len);
						System.out.println(fileLength);
						fileLength -= len;
						fos.write(buf);
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
	 * �޽��� Ÿ���� <code>type</code>�� �޽����� �Է��� ��ٸ�
	 * @param type �޽��� Ÿ��
	 * @param timeout ��� ���ѽð�
	 * @return ���۹��� �޽���, ���ѽð� �ʰ� �� <code>null</code>
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
	 * �޽��� Ÿ���� <code>type</code>�� �޽����� �Է��� ��ٸ�
	 * @param type �޽��� Ÿ��
	 * @return ���۹��� �޽���
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage(byte type) {
		return waitForMessage(type, Long.MAX_VALUE);
	}

	/**
	 * �ƹ� �޽����� �ԷµǱ⸦ ���Ѿ��� ��ٸ�
	 * @param timeout ��� ���ѽð�
	 * @return ���۹��� �޽���, ���ѽð� �ʰ� �� <code>null</code>
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage(long timeout) {
		long startTime = System.currentTimeMillis();
		LinkedList<Message> queue;
		while (true) {
			synchronized (hm) {
				for (byte b: hm.keySet()) {
					queue = hm.get(b);
					if (!queue.isEmpty()) {
						System.out.println("asdf" + b);
						return queue.poll();
					}
				}
			}
			if (System.currentTimeMillis() - startTime > timeout)
				return null;
		}
	}
	
	/**
	 * �ƹ� �޽����� �ԷµǱ⸦ ���Ѿ��� ��ٸ�
	 * @return ���۹��� �޽���
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage() {
		return waitForMessage(Long.MAX_VALUE);
	}
	
	/**
	 * types�� ���� �� �ϳ��� Ÿ������ �ϴ� �޽����� �ԷµǱ� ��ٸ�
	 * @param types �޽��� Ÿ�Ե�
	 * @param timeout ��� ���ѽð�
	 * @return ���۹��� �޽���, ���ѽð� �ʰ��� <code>null</code>
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage(byte[] types, long timeout) {
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
	 * <code>types</code>�� ���� �� �ϳ��� Ÿ������ �ϴ� �޽����� �ԷµǱ⸦ ���Ѿ��� ��ٸ�
	 * @return ���۹��� �޽���
	 * @see doubledeltas.messages.Message
	 */
	public Message waitForMessage(byte[] types) {
		return waitForMessage(types, Long.MAX_VALUE);
	}
}
