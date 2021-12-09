package doubledeltas.server;

import doubledeltas.server.MysqlConnector;
import doubledeltas.environments.*;
import doubledeltas.utils.*;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PollerThread extends Thread {
    MysqlConnector con;
    ByteStringReader bsr;
    InputStream         is			= null;
    OutputStream        os			= null;
    ServerSocket        listener    = null;
    Socket              socket      = null;

    public PollerThread(MysqlConnector con) {
        this.con = con;
    }

    @Override
    public void run() {
        try {
            listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);

            Logger.l(String.format("���� ���..."));
            while(true) {
                socket = listener.accept();    // ���� ���, ���� ���� �Ϸ�

                Logger.l(String.format("Ŭ���̾�Ʈ [%s]�� ���ӵǾ����ϴ�.", socket.getInetAddress().toString()));

                is = socket.getInputStream();
                os = socket.getOutputStream();

                // ù 4����Ʈ�� �о� COMMAND/FILE ���θ� �Ǵ�.
                byte[] firstFour = new byte[4];
                is.read(firstFour);
                int code = new ByteStringReader(firstFour).readInInteger();

                if (code == 0x00000000) {
                    // ù 4����Ʈ(code)�� 000...0�̸� COMMAND
                    route();
                } else {
                    // �ƴϸ� ���� �ٿ�ε�
                    downloadFile(code);
                }
                os.close();
                is.close();
                socket.close();
                Logger.l(String.format("Ŭ���̾�Ʈ [%s] ���� ����, ���� ���...", socket.getInetAddress().toString()));
            }
        }
        catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
        finally {
            try {
                socket.close();
                listener.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.l("Ŭ���̾�Ʈ�� ��� �� ������ �߻��� ������ �ߴܵǾ����ϴ�.");
            }
            return;
        } // try-catch-finally END
    } // run method END

    private void route() {
        byte[] bytes = new byte[1024];

        try{
            is.read(bytes);
        }
        catch (Exception e) {}

        bsr.setCursor(2);	// �� 2 Byte�� Magic Number(0xC4 0xA7)�̾���
        switch (TransferCode
                .valueOf(Byte.toString(bytes[2]))) {
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
                askCutConnection();
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
        return;
    }

    /**
     * ������ ���ε��� ������ �ٿ�ε�
     * @param code
     */
    private void downloadFile(int code) {
        String  tmpName     = "$" + code + ".tmp";
        String  fileName    = code + ".png";
        File    tmpFile     = new File(Environment.FILE_DIR, tmpName);
        File    file        = new File(Environment.FILE_DIR, fileName);

        FileOutputStream fos = null;
        byte[] buffer = new byte[1024];
        int size = 0;
        int len;

        try {
            tmpFile.createNewFile();   // ������ ���� �� �� ���� �����

            fos = new FileOutputStream(tmpFile);

            Logger.l(code + " ���� �ٿ�ε� ����.");

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer);
                size += len;
            }

            tmpFile.renameTo(file);
            Logger.l(code + " ���� �ٿ�ε� ����. ũ��: " + size);
        }
        catch (IOException e) {
            e.printStackTrace();
            Logger.l(code + " ���� �ٿ�ε� ����.");
        }
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
            // send "LOGIN_FAIL 2" commacnd to client socket
            Logger.l(String.format("%s ��й�ȣ ����ġ.", id));
            return;
        }
        // send "LOGIN_SUC" command to client socket
        Logger.l(String.format("%s �α��� ����.", id));
        // sendUpdateQuery(������ �¶��� ���¸� TRUE��)
        return;
    }

    private boolean doesIDexists(String id) {
        try {
            ResultSet rs = con.sendQuery(String.format("SELECT id FROM User WHERE id=[%s]", id));
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
                    id, pw);
            );
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
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
        Logger.l(String.format("%s ȸ������ ����.", id));
        con.sendUpdateQuery("INSERT INTO " + user + "VALUES("
        		.append("'" + id + "',")
        		.append("'" + pw + "',")
        		.append("'"+ nick + "',")
        		.append("'" + 0 + "',")
        		.append("'" + ����ü + "'")
        		.append(");");
        
        askLogin(id, pw);    // ȸ�����Ե� ������ �α���
        return;
    }

    /**
     * ������ ������ ������. ���α׷��� ����Ǿ��� ���� ó��
     */
    private void askCutConnection() {
        // sendUpdateQuery(������ �¶��� ���¸� TRUE��)
    	try {
    		if(psmt != null)
    			psmt.close();
    		if(con != null)
    			con.close();
    		if(rs != null)
    			rs.close();
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * ������ ä�ù濡 �����Ϸ� ��
     * @param id
     * @param roomid
     */
    private void askRoomEnter(String id, int roomid) {
    	con.sendUpdateQuery("insert into roomid FROM user WHERE ChatID=%d", roomid);
    	con.sendUpdateQuery("insert into id FROM chatroom WHERE User=%s", id);
    	con.sendUpdateQuery("insert into id FROM chatroom-user-list WHERE User_ID=%s", id);
    	
        // user 'id'�� chatting room list�� �߰�
        // chatting room 'roomid'�� user 'id' �߰�
        // 'ROOM_ENTER_SUC' �޽��� ����
        return;
    }

    /**
     * ������ ä���� ������
     * @param id ���� ID
     * @param roomid ä�ù� �ڵ�
     * @param msg �޽���
     * @param imgid �̹��� ���� �̸�(ID)
     */
    private void askChat(String id, int roomid, String msg, int imgid) {
    	long timestamp = System.currentTimeMillis();
    	con.sendUpdateQuery("INSERT INTO " + chatroom-chatlogtable_id + "VALUES("
        		.append("'" + roomid + "',")
        		.append("'" + id + "',")
        		.append("'"+ msg + "',")
        		.append("'" + imgid + "',")
        		.append("'" + timestamp + "'")
        		.append(");");
        // ä�ù� chatid�� msg, imgid ���
        // �̹��� (imgid).png ���� ����
        // CHAT_SUC roomid, NICK, msg, FONT, imgid ����
    }

    /**
     * ������ �г����� �����Ϸ� �õ���
     * @param id
     * @param nick
     */
    private void askUserNickChange(String id, String nick) {
    	con.sendUpdateQuery("UPDATE" + con.sendQuery("SELECT User_ID FROM user WHERE User_ID=" + id) + " set User_NickName=%s", nick);
    
    }

    /**
     * ������ ������ ������ �����Ϸ��� �õ���
     * @param id
     * @param imgid
     */
    private void askUserProfileChange(String id, int imgid) {
    	con.sendUpdateQuery("UPDATE" + con.sendQuery("SELECT User_ID FROM user WHERE User_ID=" + id) + "set Profile_Picture=%s", imgid);
    	
    }
} // MyThread END
