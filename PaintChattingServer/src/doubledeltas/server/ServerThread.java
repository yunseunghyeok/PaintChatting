/*
 * https://it-dolphin.tistory.com/entry/JavaTCP-%EC%86%8C%EC%BC%93%ED%86%B5%EC%8B%A0-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EB%A7%8C%EB%93%A4%EA%B8%B01N
 * ����
 */

package doubledeltas.server;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import doubledeltas.messages.*;
import doubledeltas.utils.Logger;

public class ServerThread extends Thread {
	private static final int BUF_SIZE = 8192;
	
	private MysqlConnector con;
	private Socket socket;
	private HashMap<Integer, HashMap<String, DataOutputStream>> hm;
	private DataInputStream dis;
	private DataOutputStream dos;
	private MessageQueues qs;
	private boolean isRunning;
	
	public ServerThread(MysqlConnector con, Socket socket,
			HashMap<Integer, HashMap<String, DataOutputStream>> hm) {
		this.con = con;
		this.socket = socket;
		this.hm = hm;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			qs = new MessageQueues(dis);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.isRunning = true;
	}
	
	@Override
	public void run() {
		Logger.l(String.format("Ŭ���̾�Ʈ [%s] ����", socket.getInetAddress().toString()));		
		
		Message msg = null;
		try {
			while (isRunning) {
				msg = qs.waitForMessage();

				if (msg instanceof LoginMessage)			handle((LoginMessage)msg);
				if (msg instanceof RegisterMessage)			handle((RegisterMessage)msg);
				if (msg instanceof ConnectionCutMessage)	handle((ConnectionCutMessage)msg);
				
				if (msg instanceof SendImageMessage)		handle((SendImageMessage)msg);
			}

	    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� ���� ����.", socket.getInetAddress().toString()));
	    	this.interrupt();	// END
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			this.interrupt();
		}
	}

	/**
     * �α����� �õ�, ���� �� <code>LoginFailMessage</code>�� ������, ���� �� <code>LoginSucMessage</code>�� ������.
     * @param msg �α��� �Ű������� ���Ե� LoginMessage ��ü
     */
    private void handle(LoginMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� �α��� �õ�", socket.getInetAddress().toString()));
        if (!doesIDexist(msg.getID())) {
        	new LoginFailMessage(LoginFailMessage.NO_ID_FOUND).send(dos);
            Logger.l(String.format("%s �α��� ����: ID �̹߰�", msg.getID()));
            return;
        }
        if (!isPWcorrect(msg.getID(), msg.getPassword())) {
        	new LoginFailMessage(LoginFailMessage.PASSWORD_WRONG).send(dos);
            Logger.l(String.format("%s �α��� ����: ��й�ȣ ����ġ.", msg.getID()));
            return;
        }
        new LoginSucMessage().send(dos);
        Logger.l(String.format("%s �α��� ����.", msg.getID()));
        return;
    }

    /**
     * ������ ȸ�������� �õ�
     * @param id
     * @param pw
     * @param nick
     */
    private void handle(RegisterMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ȸ������ �õ�", socket.getInetAddress().toString()));
        if (doesIDexist(msg.getID())) {
            new RegisterFailMessage(RegisterFailMessage.DUPLICATED_ID).send(dos);
            Logger.l(String.format("%s ȸ������ ����: ID �ߺ�.", msg.getID()));
            return;
        }
        con.sendUpdateQuery(String.format(
        		"INSERT INTO user VALUES('%s', '%s', '%s', 0, '����ü');",
        				msg.getID(), msg.getPassword(), msg.getNickname()
        		));

        Logger.l(String.format("%s ȸ������ ����.", msg.getID()));
        handle(new LoginMessage(msg.getID(), msg.getPassword()));    // ȸ�����Ե� ������ �α���
        return;
    }
    
    private boolean doesIDexist(String id) {
        try {
            ResultSet rs = con.sendQuery(String.format(
            		"SELECT id FROM user WHERE id='%s'",
            		id));
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isPWcorrect(String id, String pw) {
        try {
            ResultSet rs = con.sendQuery(String.format(
            		"SELECT id FROM User WHERE id='%s' AND pw='%s'",
                    id, pw)
            );
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * ������ ������ ������. Ŭ���̾�Ʈ�� ����Ǿ��� ���� ó��
     */
    private void handle(ConnectionCutMessage msg) {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� ���� ���� ��û", socket.getInetAddress().toString()));
		for (int roomid : hm.keySet()) {
			HashMap<String, DataOutputStream> room = hm.get(roomid);
    		for (String id : room.keySet()) {
    			if (room.get(id) == this.dos) room.remove(id);
    		}
		}
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isRunning = false;	// whlie-loop ����
    }
    
    /**
     * ä�ù��� ����, 8�ڸ��� ������ ���ڸ� ID�� �Ͽ� ä�ù��� �����Ѵ�.
     */
    private void handle(RoomCreateMessage msg) {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ä�ù� ���� ��û", socket.getInetAddress().toString()));
    	int roomID;
    	do {
    		roomID = (int)(Math.random() * (100_000_000 - 10_000_000)) + 10_000_000;
    	} while (doesRoomIDExist(roomID));

        con.sendUpdateQuery(String.format(
        		"INSERT INTO chatroom VALUES('%s', '%s', '%s', NULL);",
        				roomID, "ä�ù� #"+roomID
        		));
    }
    
    private boolean doesRoomIDExist(int roomID) {
        try {
            ResultSet rs = con.sendQuery(String.format(
            		"SELECT ChatID FROM chatroom WHERE id='%d'",
                    roomID)
            );
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * ������ ä�ù濡 �����Ϸ� ��
     * @param id
     * @param roomid
     */
    private void handle(RoomEnterMessage msg) {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ä�ù� ���� ��û", socket.getInetAddress().toString()));
    	
    }
    
    private void handle(SendImageMessage msg) {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� �̹��� ���� ����: %s",
    			socket.getInetAddress().toString(), msg.getFileName()
    			));
    }
    
    
} // ServerThread END
