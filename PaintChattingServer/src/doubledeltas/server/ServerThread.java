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
		Logger.l(String.format("클라이언트 [%s] 접속", socket.getInetAddress().toString()));		
		
		Message msg = null;
		try {
			while (isRunning) {
				msg = qs.waitForMessage();

				if (msg instanceof LoginMessage)			handle((LoginMessage)msg);
				if (msg instanceof RegisterMessage)			handle((RegisterMessage)msg);
				if (msg instanceof ConnectionCutMessage)	handle((ConnectionCutMessage)msg);
				
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

	/**
     * 로그인을 시도, 실패 시 <code>LoginFailMessage</code>를 보내고, 성공 시 <code>LoginSucMessage</code>를 보낸다.
     * @param msg 로그인 매개변수가 포함된 LoginMessage 객체
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

        Logger.l(String.format("%s 회원가입 성공.", msg.getID()));
        handle(new LoginMessage(msg.getID(), msg.getPassword()));    // 회원가입된 정보로 로그인
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
     * 채팅방을 생성, 8자리의 무작위 숫자를 ID로 하여 채팅방을 생성한다.
     */
    private void handle(RoomCreateMessage msg) {
    	Logger.l(String.format("클라이언트 [%s]로부터 채팅방 생성 요청", socket.getInetAddress().toString()));
    	int roomID;
    	do {
    		roomID = (int)(Math.random() * (100_000_000 - 10_000_000)) + 10_000_000;
    	} while (doesRoomIDExist(roomID));

        con.sendUpdateQuery(String.format(
        		"INSERT INTO chatroom VALUES('%s', '%s', '%s', NULL);",
        				roomID, "채팅방 #"+roomID
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
     * 유저가 채팅방에 입장하려 함
     * @param id
     * @param roomid
     */
    private void handle(RoomEnterMessage msg) {
    	Logger.l(String.format("클라이언트 [%s]로부터 채팅방 입장 요청", socket.getInetAddress().toString()));
    	
    }
    
    private void handle(SendImageMessage msg) {
    	Logger.l(String.format("클라이언트 [%s]가 이미지 전송 성공: %s",
    			socket.getInetAddress().toString(), msg.getFileName()
    			));
    }
    
    
} // ServerThread END
