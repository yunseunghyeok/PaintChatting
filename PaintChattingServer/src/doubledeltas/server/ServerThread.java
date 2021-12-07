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

import doubledeltas.environments.Environment;
import doubledeltas.messages.*;
import doubledeltas.utils.Logger;

public class ServerThread extends Thread {
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

				if		(msg instanceof LoginMessage)				handle((LoginMessage)msg);
				else if (msg instanceof RegisterMessage)			handle((RegisterMessage)msg);
				else if (msg instanceof ConnectionCutMessage)		handle((ConnectionCutMessage)msg);
				else if (msg instanceof RoomEnterMessage)			handle((RoomEnterMessage)msg);
				else if (msg instanceof SendImageMessage)			handle((SendImageMessage)msg);
				else if (msg instanceof ChatMessage)				handle((ChatMessage)msg);
				else if (msg instanceof UserNickChangeMessage)		handle((UserNickChangeMessage)msg);
				else if (msg instanceof UserProfileChangeMessage)	handle((UserProfileChangeMessage)msg);
				else if (msg instanceof RoomCreateMessage)			handle((RoomCreateMessage)msg);
				else if (msg instanceof UserFontChangeMessage)		handle((UserFontChangeMessage)msg);
				else {
					
				}
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

	
	private boolean doesRoomIDExist(int roomID) {
	    try {
	        ResultSet rs = con.sendQuery(String.format(
	        		"SELECT room_id FROM chatroom WHERE room_id='%d'",
	                roomID
	                ));
	        return rs.next();
	    }
	    catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}

	
	private boolean isUserInRoom(int roomID, String userID) {
		try {
			ResultSet rs = con.sendQuery(String.format(
					"SELECT room_id FROM room_user_map WHERE room_id='%s' AND user_id='%s'",
					roomID, userID
					));
			return rs.next();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	/**
     * �α����� �õ�, ���� �� <code>LoginFailMessage</code>�� ������, ���� �� <code>LoginSucMessage</code>�� ������.
     * @param msg �α��� �Ű������� ���Ե� LoginMessage ��ü
     * @throws IOException
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
        try {
        	ResultSet rs = con.sendQuery(String.format(
            		"SELECT room_id FROM room_user_map WHERE user_id='%s'",
            		msg.getID()
            		));
            rs.last();
            int cnt = rs.getRow();
            String[] rooms = new String[cnt];
            rs.beforeFirst();
            for(int i=0; i<cnt; i++) {
            	rs.next();
            	rooms[i] = rs.getString("room_id");
            }
            new LoginSucMessage(rooms).send(dos);
            Logger.l(String.format("%s �α��� ����.", msg.getID()));
            return;
        }
        catch (SQLException ex) {
        	new LoginFailMessage(LoginFailMessage.UNKNOWN).send(dos);
            Logger.l(String.format("%s �α��� ����: SQL ����", msg.getID()));
            ex.printStackTrace();
        }
        
    }

    
    /**
     * ������ ȸ�������� �õ�
     * @param id
     * @param pw
     * @param nick
     * @throws IOException
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
        new RegisterSucMessage().send(dos);
        Logger.l(String.format("%s ȸ������ ����.", msg.getID()));
        handle(new LoginMessage(msg.getID(), msg.getPassword()));    // ȸ�����Ե� ������ �α���
        return;
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
     * ������ ä�ù濡 �����Ϸ� ��
     * @param msg RoomEnterMessage �޽���
     * @throws IOException
     */
    private void handle(RoomEnterMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ä�ù� ���� ��û", socket.getInetAddress().toString()));
    	if (!doesRoomIDExist(msg.getRoomID())) {
    		new RoomEnterFailMessage(RoomEnterFailMessage.NO_MATCHED_ROOM).send(dos);
        	Logger.l(String.format("%s ä�ù� ���� ����: �� �������� ����", msg.getUserID()));
        	return;
    	}
    	if (isUserInRoom(msg.getRoomID(), msg.getUserID())) {
    		new RoomEnterFailMessage(RoomEnterFailMessage.ALREADY_JOINED).send(dos);
        	Logger.l(String.format("%s ä�ù� ���� ����: �̹� �濡 ����", msg.getUserID()));
        	return;
    	}
        con.sendUpdateQuery(String.format(
        		"INSERT INTO room_user_map VALUES('%d', '%s');",
        				msg.getRoomID(), msg.getUserID()
        		));
        new RoomEnterSucMessage().send(dos);
        try {
            new RoomSomeoneJoinedMessage(msg.getUserID()).broadcast(hm, msg.getRoomID());
        }
        catch (Exception ex) {
        	Logger.l("ROOM_SOMEONE_JOINED ���� ����");
        }
        Logger.l(String.format("%s �� ���� ����.", msg.getUserID()));
    }
    
    private void handle(SendImageMessage msg) {
		Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� �̹��� ���� ����: %s",
				socket.getInetAddress().toString(), msg.getFileName()
				));
	}

    
	/**
     * ���۹��� ä�ÿ� ���� ���� ä�ù濡 CHAT_SUC �޽����� �����
     * @param msg ���۵� CHAT �޽��� ��ü
     */
    private void handle(ChatMessage msg) throws IOException {
    	ResultSet rs;
    	String text = msg.getText();
    	String font = null;
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ä�� ��û: %s",
    			socket.getInetAddress().toString(),
    			(text.length() > 80) ? text.substring(0, 80) + " ..." : text));
		try {
			// find font
			rs = con.sendQuery(String.format(
					"SELECT font FROM user WHERE id='%s'",
					msg.getUserID()
					));
			if (!rs.next()) {
				new ChatFailMessage(ChatFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s ä�� ó�� ����: ���� �˻� ����", msg.getUserID()));
				return;
			}
			font = rs.getString("font");
			
			// log chat
			int row = con.sendUpdateQuery(String.format(
					"INSERT INTO chat_log VALUES (%d, %s, %s, %s, %s)",
					System.currentTimeMillis(),
					msg.getUserID(),
					msg.getRoomID(),
					msg.getText(),
					msg.getImageFileName()
					));
			if (row != 1) {
	        	Logger.l(String.format("%s ä�� �α� ����", msg.getUserID()));
			}
		}
		catch (SQLException e) {
			new ChatFailMessage(ChatFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s ä�� ó�� ����: SQL ���� �߻�", msg.getUserID()));
			e.printStackTrace();
		}
		
    	if (msg.getImageFileName() != null) {
    		File file = new File(Environment.FILE_DIR + "\\" + msg.getImageFileName());
    		try {
        		new SendImageMessage(msg.getImageFileName(), file).broadcast(hm, msg.getRoomID());
    		}
    		catch (Exception ex) {
    	        Logger.l(String.format("%d ���� ���� ����.", msg.getRoomID()));
    		}
    	}
    	try {
			new ChatSucMessage(msg.getRoomID(), msg.getUserID(), msg.getText(), font, msg.getImageFileName())
				.broadcast(hm, msg.getRoomID());
		} catch (Exception ex) {
			Logger.l("�� Ž�� ����, ChatMessage ��� ����.");
			ex.printStackTrace();
		}
        Logger.l(String.format("%s ä�� ó�� �Ϸ�.", msg.getUserID()));
    }
    
    
	/**
     * USER_NICK_CHANGE�� ���� user�� nick �÷��� UPDATE �õ��ϰ� ����� ����
     * @param msg ���۵� USER_NICK_CHANGE �޽��� ��ü
     */
    private void handle(UserNickChangeMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� �г��� ���� ��û", socket.getInetAddress().toString()));
    	String oldnick = null;
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT nick FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserNickChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s �г��� ���� ����: ���̵� �������� ����", msg.getID()));
	        	return;
			}
			oldnick = rs.getString("nick");
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserNickChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s �г��� ���� ����: SQL ���� �߻�", msg.getID()));
			ex.printStackTrace();
		}
    	// if old nick == new nick, resend FAIL message with SAME_NICK reason
    	if (msg.getNewNickname().equals(oldnick)) {
    		new UserNickChangeFailMessage(UserNickChangeFailMessage.SAME_NICK).send(dos);
        	Logger.l(String.format("%s �г��� ���� ����: ���� �г���", msg.getID()));
        	return;
    	}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET nick='%s' WHERE id='%s'",
    			msg.getNewNickname(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s �г��� ���� ����: %s -> %s",
        		msg.getID(), oldnick, msg.getNewNickname()
        		));
    }
    
    
	/**
     * USER_PROFILE_CHANGE�� ���� user�� picture �÷��� UPDATE �õ��ϰ� ����� ����
     * @param msg ���۵� USER_PROFILE_CHANGE �޽��� ��ü
     */
    private void handle(UserProfileChangeMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ���� ���� ��û", socket.getInetAddress().toString()));
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT picture FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserProfileChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s ���� ���� ����: ���̵� �������� ����", msg.getID()));
	        	return;
			}
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserProfileChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s ���� ���� ����: SQL ���� �߻�", msg.getID()));
			ex.printStackTrace();
		}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET nick='%s' WHERE id='%s'",
    			msg.getFileName(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s �г��� ���� ����: -> %s",
        		msg.getID(), msg.getFileName()
        		));
    }
    
    
    /**
     * ä�ù��� ����, 8�ڸ��� ������ ���ڸ� ID�� �Ͽ� ä�ù��� �����Ѵ�.
     * @throws IOException 
     */
    private void handle(RoomCreateMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ä�ù� ���� ��û", socket.getInetAddress().toString()));
    	int roomID;
    	do {
    		roomID = (int)(Math.random() * (100_000_000 - 10_000_000)) + 10_000_000;
    	} while (doesRoomIDExist(roomID));

        con.sendUpdateQuery(String.format(
        		"INSERT INTO chatroom VALUES('%s', '%s', '%s', NULL);",
        				roomID, "ä�ù� #" + roomID
        		));
        new RoomCreateSucMessage().send(dos);
        Logger.l(String.format("%s �� ���� ����.", msg.getID()));
        // ù ����
        handle(new RoomEnterMessage(msg.getID(), roomID));
    }
    
    
    /**
     * USER_NICK_CHANGE�� ���� user�� font �÷��� UPDATE �õ��ϰ� ����� ����
     * @param msg ���۵� USER_FONT_CHANGE �޽��� ��ü
     */
    private void handle(UserFontChangeMessage msg) throws IOException {
    	Logger.l(String.format("Ŭ���̾�Ʈ [%s]�κ��� ��Ʈ ���� ��û", socket.getInetAddress().toString()));
    	String oldfont = null;
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT font FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserFontChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s �г��� ���� ����: ���̵� �������� ����", msg.getID()));
	        	return;
			}
			oldfont = rs.getString("font");
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserFontChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s �г��� ���� ����: SQL ���� �߻�", msg.getID()));
			ex.printStackTrace();
		}
    	// if old font == new font, resend FAIL message with SAME_FONT reason
    	if (msg.getNewFont().equals(oldfont)) {
    		new UserNickChangeFailMessage(UserFontChangeFailMessage.SAME_FONT).send(dos);
        	Logger.l(String.format("%s �г��� ���� ����: ���� �г���", msg.getID()));
        	return;
    	}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET font='%s' WHERE id='%s'",
    			msg.getNewFont(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s ��Ʈ ���� ����: %s -> %s",
        		msg.getID(), oldfont, msg.getNewFont()
        		));
    }
    
} // ServerThread END
