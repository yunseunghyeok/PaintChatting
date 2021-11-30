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
	}
	
	@Override
	public void run() {
		Logger.l(String.format("Ŭ���̾�Ʈ [%s] ����", socket.getInetAddress().toString()));		
		
		Message msg = null;
		try {
			while (true) {
				msg = qs.waitForMessage();

				if (msg instanceof LoginMessage) handle((LoginMessage)msg);
				//else
					//Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� �� �޽��� ����.", socket.getInetAddress().toString()));
				
				if (msg instanceof ConnectionCutMessage) {
					Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� ������ ������.", socket.getInetAddress().toString()));
					break;
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			this.interrupt();
		}
	}
	
	private void handle(LoginMessage msg) {
		Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� LoginMessage ����.", socket.getInetAddress().toString()));
		Logger.l(String.format("id: %s, pw: %s", msg.getID(), msg.getPassword()));
	}
	
	/**
     * ������ �α����� �õ�
     * @param id
     * @param pw
     */
    private void askLogin(String id, String pw) {
        if (!doesIDexist(id)) {
            // send "LOGIN_FAIL 1" command to client socket
            Logger.l(String.format("%s �α��� ����.", id));
            return;
        }
        if (!isPWcorrect(id, pw)) {
            // send "LOGIN_FAIL 2" command to client socket
            Logger.l(String.format("%s ��й�ȣ ����ġ.", id));
            return;
        }
        // send "LOGIN_SUC" command to client socket
        Logger.l(String.format("%s �α��� ����.", id));
        // sendUpdateQuery(������ �¶��� ���¸� TRUE��)
        return;
    }

    /**
     * ������ ȸ�������� �õ�
     * @param id
     * @param pw
     * @param nick
     */
    private void askRegistration(String id, String pw, String nick) {
        if (doesIDexist(id)) {
            // send "REGISTER_FAIL 1" command to client socket
            Logger.l(String.format("%s ȸ������ ����. ID �ߺ�.", id));
            return;
        }
        // send "REGISTER_SUC" command to client socket
        con.sendUpdateQuery(
        		"INSERT INTO user VALUES('%s', '%s', '%s', 0, '����ü');".formatted(
        				id, pw, nick)
        		);

        Logger.l(String.format("%s ȸ������ ����.", id));
        askLogin(id, pw);    // ȸ�����Ե� ������ �α���
        return;
    }
    
    private boolean doesIDexist(String id) {
        try {
            ResultSet rs = con.sendQuery(String.format(
            		"SELECT id FROM User WHERE id='%s'",
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
                    "SELECT pw FROM (SELECT id FROM User WHERE id=%s) WHERE pw=%s",
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
    private void askConnectionCut() {
		for (int roomid : hm.keySet()) {
			HashMap<String, DataOutputStream> room = hm.get(roomid);
    		for (DataOutputStream dos : room.values()) {
    			if (dos == this.dos) room.remove(dos);
    		}
		}
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.interrupt();	// END
    }
    
    /**
     * ������ ä�ù濡 �����Ϸ� ��
     * @param id
     * @param roomid
     */
    private void askRoomEnter(String id, int roomid) {
    	
    }
    
    /**
     * ������ ä���� ������
     * @param id ���� ID
     * @param roomid ä�ù� �ڵ�
     * @param msg �޽���
     * @param imgid �̹��� ���� �̸�(ID)
     */
    private void askChat(String id, int roomid, String msg, int imgid) {
    	
    }
    /*
    private void askSendImage(...) {
    
	}
    //*/
   
    /**
     * ������ �г����� �����Ϸ� �õ���
     * @param id
     * @param nick
     */
    private void askUserNickChange(String id, String nick) {
    
    }    

    /**
     * ������ ������ ������ �����Ϸ��� �õ���
     * @param id
     * @param imgid
     */
    private void askUserProfileChange(String id, int imgid) {
    	
    }
    
    
} // ServerThread END
