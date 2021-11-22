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
import java.util.Vector;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.Logger;

public class ServerThread extends Thread {
	private MysqlConnector con;
	private Socket socket;
	private HashMap<Integer, Vector<OutputStream>> hm;
	private InputStream is;
	private OutputStream os;
	private ByteStringReader bsr;
	
	public ServerThread(MysqlConnector con, Socket socket, HashMap<Integer, Vector<OutputStream>> hm) {
		this.con = con;
		this.socket = socket;
		this.hm = hm;
	}
	
	@Override
	public void run() {
		Logger.l(String.format("클라이언트 [%s] 접속", socket.getInetAddress().toString()));
		
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		route();
	}
	
	private void route() {
        bsr.setCursor(0);
        try {
        	switch (TransferCode.valueOf(
            		Integer.toString(is.read())
            		)) {
                case LOGIN: {
                    String  id		= bsr.readInString(45);
                    String  pw		= bsr.readInString(45);
                    askLogin(id, pw);
                    break;
                }
                case REGISTER: {
                    String  id		= bsr.readInString(45);
                    String  pw		= bsr.readInString(45);
                    String  nick 	= bsr.readInString(45);
                    askRegistration(id, pw, nick);
                    break;
                }
                case CONNECTION_CUT:
                    askConnectionCut();
                    break;
                case ROOM_ENTER: {
                    String  id		= bsr.readInString(45);
                    int     roomid	= bsr.readInInteger(4);
                    askRoomEnter(id, roomid);
                    break;
                }
                case CHAT: {
                    String  id		= bsr.readInString(45);
                    int     roomid	= bsr.readInInteger(4);
                    String  msg		= bsr.readInString(1024);
                    int     imgid	= bsr.readInInteger(4);
                    askChat(id, roomid, msg, imgid);
                    break;
                }
                case USER_NICK_CHANGE: {
                    String  id		= bsr.readInString(45);
                    String  nick	= bsr.readInString(45);
                    askUserNickChange(id, nick);
                    break;
                }
                case USER_PROFILE_CHANGE: {
                    String  id		= bsr.readInString(45);
                    int     imgid	= bsr.readInInteger(4);
                    askUserProfileChange(id, imgid);
                    break;
                }
                default:
                    break;
            }
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        return;
    }
	
	/**
     * 유저가 로그인을 시도
     * @param id
     * @param pw
     */
    private void askLogin(String id, String pw) {
        if (!doesIDexists(id)) {
            // send "LOGIN_FAIL 1" command to client socket
            Logger.l(String.format("%s 로그인 실패.", id));
            return;
        }
        if (!isPWcorrect(id, pw)) {
            // send "LOGIN_FAIL 2" command to client socket
            Logger.l(String.format("%s 비밀번호 불일치.", id));
            return;
        }
        // send "LOGIN_SUC" command to client socket
        Logger.l(String.format("%s 로그인 성공.", id));
        // sendUpdateQuery(유저의 온라인 상태를 TRUE로)
        return;
    }

    /**
     * 유저가 회원가입을 시도
     * @param id
     * @param pw
     * @param nick
     */
    private void askRegistration(String id, String pw, String nick) {
        if (doesIDexists(id)) {
            // send "REGISTER_FAIL 1" command to client socket
            Logger.l(String.format("%s 회원가입 실패. ID 중복.", id));
            return;
        }
        // send "REGISTER_SUC" command to client socket
        con.sendUpdateQuery(
        		"INSERT INTO user VALUES('%s', '%s', '%s', 0, '굴림체');".formatted(
        				id, pw, nick)
        		);

        Logger.l(String.format("%s 회원가입 성공.", id));
        askLogin(id, pw);    // 회원가입된 정보로 로그인
        return;
    }
    
    private boolean doesIDexists(String id) {
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
     * 유저가 연결을 종료함. 클라이언트가 종료되었을 때의 처리
     */
    private void askConnectionCut() {
		for (int room : hm.keySet()) {
			Vector<OutputStream> oss = hm.get(room);
    		for (OutputStream os : oss) {	// hm.get(room): OS vector
    			if (os == this.os) {
    				oss.remove(os);
    			}
    		}
		}
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.interrupt();	// ServerThread INTERRUPT
    }
    
    /**
     * 유저가 채팅방에 입장하려 함
     * @param id
     * @param roomid
     */
    private void askRoomEnter(String id, int roomid) {
    	
    }
    
    /**
     * 유저가 채팅을 전송함
     * @param id 유저 ID
     * @param roomid 채팅방 코드
     * @param msg 메시지
     * @param imgid 이미지 파일 이름(ID)
     */
    private void askChat(String id, int roomid, String msg, int imgid) {
    	
    }
    /*
    private void askSendImage(...) {
    
	}
    //*/
   
    /**
     * 유저가 닉네임을 변경하려 시도함
     * @param id
     * @param nick
     */
    private void askUserNickChange(String id, String nick) {
    
    }    

    /**
     * 유저가 프로필 사진을 변경하려고 시도함
     * @param id
     * @param imgid
     */
    private void askUserProfileChange(String id, int imgid) {
    	
    }
    
    
} // ServerThread END
