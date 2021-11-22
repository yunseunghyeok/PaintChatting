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
		Logger.l(String.format("Ŭ���̾�Ʈ [%s] ����", socket.getInetAddress().toString()));
		
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
     * ������ �α����� �õ�
     * @param id
     * @param pw
     */
    private void askLogin(String id, String pw) {
        if (!doesIDexists(id)) {
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
        if (doesIDexists(id)) {
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
     * ������ ������ ������. Ŭ���̾�Ʈ�� ����Ǿ��� ���� ó��
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
