/*
 * https://it-dolphin.tistory.com/entry/JavaTCP-%EC%86%8C%EC%BC%93%ED%86%B5%EC%8B%A0-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EB%A7%8C%EB%93%A4%EA%B8%B01N
 * 참고
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
		Logger.l(String.format("클라이언트 [%s] 접속", socket.getInetAddress().toString()));		
		
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

	    	Logger.l(String.format("클라이언트 [%s]와 연결 종료.", socket.getInetAddress().toString()));
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
     * 로그인을 시도, 실패 시 <code>LoginFailMessage</code>를 보내고, 성공 시 <code>LoginSucMessage</code>를 보낸다.
     * @param msg 로그인 매개변수가 포함된 LoginMessage 객체
     * @throws IOException
     */
    private void handle(LoginMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 로그인 시도", socket.getInetAddress().toString()));
        if (!doesIDexist(msg.getID())) {
        	new LoginFailMessage(LoginFailMessage.NO_ID_FOUND).send(dos);
            Logger.l(String.format("%s 로그인 실패: ID 미발견", msg.getID()));
            return;
        }
        if (!isPWcorrect(msg.getID(), msg.getPassword())) {
        	new LoginFailMessage(LoginFailMessage.PASSWORD_WRONG).send(dos);
            Logger.l(String.format("%s 로그인 실패: 비밀번호 불일치.", msg.getID()));
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
            Logger.l(String.format("%s 로그인 성공.", msg.getID()));
            return;
        }
        catch (SQLException ex) {
        	new LoginFailMessage(LoginFailMessage.UNKNOWN).send(dos);
            Logger.l(String.format("%s 로그인 실패: SQL 오류", msg.getID()));
            ex.printStackTrace();
        }
        
    }

    
    /**
     * 유저가 회원가입을 시도
     * @param id
     * @param pw
     * @param nick
     * @throws IOException
     */
    private void handle(RegisterMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 회원가입 시도", socket.getInetAddress().toString()));
        if (doesIDexist(msg.getID())) {
            new RegisterFailMessage(RegisterFailMessage.DUPLICATED_ID).send(dos);
            Logger.l(String.format("%s 회원가입 실패: ID 중복.", msg.getID()));
            return;
        }
        con.sendUpdateQuery(String.format(
        		"INSERT INTO user VALUES('%s', '%s', '%s', 0, '굴림체');",
        				msg.getID(), msg.getPassword(), msg.getNickname()
        		));
        new RegisterSucMessage().send(dos);
        Logger.l(String.format("%s 회원가입 성공.", msg.getID()));
        handle(new LoginMessage(msg.getID(), msg.getPassword()));    // 회원가입된 정보로 로그인
        return;
    }
    
    
    /**
     * 유저가 연결을 종료함. 클라이언트가 종료되었을 때의 처리
     */
    private void handle(ConnectionCutMessage msg) {
    	Logger.l(String.format("클라이언트 [%s]가 연결 종료 요청", socket.getInetAddress().toString()));
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
		isRunning = false;	// whlie-loop 종료
    }
    
    
    /**
     * 유저가 채팅방에 입장하려 함
     * @param msg RoomEnterMessage 메시지
     * @throws IOException
     */
    private void handle(RoomEnterMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 채팅방 입장 요청", socket.getInetAddress().toString()));
    	if (!doesRoomIDExist(msg.getRoomID())) {
    		new RoomEnterFailMessage(RoomEnterFailMessage.NO_MATCHED_ROOM).send(dos);
        	Logger.l(String.format("%s 채팅방 입장 실패: 방 존재하지 않음", msg.getUserID()));
        	return;
    	}
    	if (isUserInRoom(msg.getRoomID(), msg.getUserID())) {
    		new RoomEnterFailMessage(RoomEnterFailMessage.ALREADY_JOINED).send(dos);
        	Logger.l(String.format("%s 채팅방 입장 실패: 이미 방에 있음", msg.getUserID()));
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
        	Logger.l("ROOM_SOMEONE_JOINED 전송 실패");
        }
        Logger.l(String.format("%s 방 입장 성공.", msg.getUserID()));
    }
    
    private void handle(SendImageMessage msg) {
		Logger.l(String.format("클라이언트 [%s]로부터 이미지 수신 성공: %s",
				socket.getInetAddress().toString(), msg.getFileName()
				));
	}

    
	/**
     * 전송받은 채팅에 대해 같은 채팅방에 CHAT_SUC 메시지를 방송함
     * @param msg 전송된 CHAT 메시지 객체
     */
    private void handle(ChatMessage msg) throws IOException {
    	ResultSet rs;
    	String text = msg.getText();
    	String font = null;
    	Logger.l(String.format("클라이언트 [%s]로부터 채팅 요청: %s",
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
	        	Logger.l(String.format("%s 채팅 처리 실패: 유저 검색 실패", msg.getUserID()));
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
	        	Logger.l(String.format("%s 채팅 로그 오류", msg.getUserID()));
			}
		}
		catch (SQLException e) {
			new ChatFailMessage(ChatFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s 채팅 처리 실패: SQL 예외 발생", msg.getUserID()));
			e.printStackTrace();
		}
		
    	if (msg.getImageFileName() != null) {
    		File file = new File(Environment.FILE_DIR + "\\" + msg.getImageFileName());
    		try {
        		new SendImageMessage(msg.getImageFileName(), file).broadcast(hm, msg.getRoomID());
    		}
    		catch (Exception ex) {
    	        Logger.l(String.format("%d 파일 전송 실패.", msg.getRoomID()));
    		}
    	}
    	try {
			new ChatSucMessage(msg.getRoomID(), msg.getUserID(), msg.getText(), font, msg.getImageFileName())
				.broadcast(hm, msg.getRoomID());
		} catch (Exception ex) {
			Logger.l("방 탐색 오류, ChatMessage 방송 실패.");
			ex.printStackTrace();
		}
        Logger.l(String.format("%s 채팅 처리 완료.", msg.getUserID()));
    }
    
    
	/**
     * USER_NICK_CHANGE에 따라 user의 nick 컬럼을 UPDATE 시도하고 결과를 전송
     * @param msg 전송된 USER_NICK_CHANGE 메시지 객체
     */
    private void handle(UserNickChangeMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 닉네임 변경 요청", socket.getInetAddress().toString()));
    	String oldnick = null;
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT nick FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserNickChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s 닉네임 변경 실패: 아이디 존재하지 않음", msg.getID()));
	        	return;
			}
			oldnick = rs.getString("nick");
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserNickChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s 닉네임 변경 실패: SQL 예외 발생", msg.getID()));
			ex.printStackTrace();
		}
    	// if old nick == new nick, resend FAIL message with SAME_NICK reason
    	if (msg.getNewNickname().equals(oldnick)) {
    		new UserNickChangeFailMessage(UserNickChangeFailMessage.SAME_NICK).send(dos);
        	Logger.l(String.format("%s 닉네임 변경 실패: 같은 닉네임", msg.getID()));
        	return;
    	}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET nick='%s' WHERE id='%s'",
    			msg.getNewNickname(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s 닉네임 변경 성공: %s -> %s",
        		msg.getID(), oldnick, msg.getNewNickname()
        		));
    }
    
    
	/**
     * USER_PROFILE_CHANGE에 따라 user의 picture 컬럼을 UPDATE 시도하고 결과를 전송
     * @param msg 전송된 USER_PROFILE_CHANGE 메시지 객체
     */
    private void handle(UserProfileChangeMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 프사 변경 요청", socket.getInetAddress().toString()));
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT picture FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserProfileChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s 프사 변경 실패: 아이디 존재하지 않음", msg.getID()));
	        	return;
			}
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserProfileChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s 프사 변경 실패: SQL 예외 발생", msg.getID()));
			ex.printStackTrace();
		}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET nick='%s' WHERE id='%s'",
    			msg.getFileName(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s 닉네임 변경 성공: -> %s",
        		msg.getID(), msg.getFileName()
        		));
    }
    
    
    /**
     * 채팅방을 생성, 8자리의 무작위 숫자를 ID로 하여 채팅방을 생성한다.
     * @throws IOException 
     */
    private void handle(RoomCreateMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 채팅방 생성 요청", socket.getInetAddress().toString()));
    	int roomID;
    	do {
    		roomID = (int)(Math.random() * (100_000_000 - 10_000_000)) + 10_000_000;
    	} while (doesRoomIDExist(roomID));

        con.sendUpdateQuery(String.format(
        		"INSERT INTO chatroom VALUES('%s', '%s', '%s', NULL);",
        				roomID, "채팅방 #" + roomID
        		));
        new RoomCreateSucMessage().send(dos);
        Logger.l(String.format("%s 방 입장 성공.", msg.getID()));
        // 첫 접속
        handle(new RoomEnterMessage(msg.getID(), roomID));
    }
    
    
    /**
     * USER_NICK_CHANGE에 따라 user의 font 컬럼을 UPDATE 시도하고 결과를 전송
     * @param msg 전송된 USER_FONT_CHANGE 메시지 객체
     */
    private void handle(UserFontChangeMessage msg) throws IOException {
    	Logger.l(String.format("클라이언트 [%s]로부터 폰트 변경 요청", socket.getInetAddress().toString()));
    	String oldfont = null;
    	ResultSet rs;
    	try {
			rs = con.sendQuery(String.format(
					"SELECT font FROM user WHERE id='%s'",
					msg.getID()
					));
			if (!rs.next()) {
	    		new RoomEnterFailMessage(UserFontChangeFailMessage.UNKNOWN).send(dos);
	        	Logger.l(String.format("%s 닉네임 변경 실패: 아이디 존재하지 않음", msg.getID()));
	        	return;
			}
			oldfont = rs.getString("font");
		} catch (SQLException ex) {
			new UserNickChangeFailMessage(UserFontChangeFailMessage.UNKNOWN).send(dos);
        	Logger.l(String.format("%s 닉네임 변경 실패: SQL 예외 발생", msg.getID()));
			ex.printStackTrace();
		}
    	// if old font == new font, resend FAIL message with SAME_FONT reason
    	if (msg.getNewFont().equals(oldfont)) {
    		new UserNickChangeFailMessage(UserFontChangeFailMessage.SAME_FONT).send(dos);
        	Logger.l(String.format("%s 닉네임 변경 실패: 같은 닉네임", msg.getID()));
        	return;
    	}
    	// else, success
    	con.sendUpdateQuery(String.format(
    			"UPDATE user SET font='%s' WHERE id='%s'",
    			msg.getNewFont(), msg.getID()
    			));
    	new UserNickChangeSucMessage().send(dos);
        Logger.l(String.format("%s 폰트 변경 성공: %s -> %s",
        		msg.getID(), oldfont, msg.getNewFont()
        		));
    }
    
} // ServerThread END
