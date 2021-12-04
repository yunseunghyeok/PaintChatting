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

				if (msg instanceof LoginMessage)			handle((LoginMessage)msg);
				if (msg instanceof RegisterMessage)			handle((RegisterMessage)msg);
				if (msg instanceof ConnectionCutMessage)	handle((ConnectionCutMessage)msg);
				if (msg instanceof RoomEnterMessage)		handle((RoomEnterMessage)msg);
				if (msg instanceof RoomCreateMessage)		handle((RoomCreateMessage)msg);

				if (msg instanceof SendImageMessage)		handle((SendImageMessage)msg);
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
        new LoginSucMessage().send(dos);
        Logger.l(String.format("%s 로그인 성공.", msg.getID()));
        return;
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
        				roomID, "채팅방 #"+roomID
        		));
        new RoomCreateSucMessage().send(dos);
        Logger.l(String.format("%s 방 입장 성공.", msg.getID()));
    }
    
    private void handle(SendImageMessage msg) {
    	Logger.l(String.format("클라이언트 [%s]로부터 이미지 수신 성공: %s",
    			socket.getInetAddress().toString(), msg.getFileName()
    			));
    }
    
    
} // ServerThread END
