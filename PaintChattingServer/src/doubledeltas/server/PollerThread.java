package doubledeltas.server;

import doubledeltas.server.MysqlConnector;
import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;
import doubledeltas.utils.TransferCode;

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
    Thread              th          = null;

    public PollerThread(MysqlConnector con) {
        this.con = con;
    }

    @Override
    public void run() {
        try {
            listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);

            Logger.l(String.format("연결 대기..."));
            while(true) {
                socket = listener.accept();    // 연결 대기, 이후 연결 완료

                Logger.l(String.format("클라이언트 [%s]가 접속되었습니다.", socket.getInetAddress().toString()));

                is = socket.getInputStream();
                os = socket.getOutputStream();

                // 첫 4바이트를 읽어 COMMAND/FILE 여부를 판단.
                byte[] firstFour = new byte[4];
                is.read(firstFour);
                int code = new ByteStringReader(firstFour).readInInteger();

                if (code == 0x00000000) {
                    // 첫 4바이트(code)가 000...0이면 COMMAND
                    route();
                } else {
                    // 아니면 파일 다운로드
                    downloadFile(code);
                }
                os.close();
                is.close();
                socket.close();
                Logger.l(String.format("클라이언트 [%s] 연결 종료, 연결 대기...", socket.getInetAddress().toString()));
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
                Logger.l("클라이언트와 통신 중 오류가 발생해 연결이 중단되었습니다.");
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

        bsr.setCursor(2);	// 앞 2 Byte는 Magic Number(0xC4 0xA7)이었음
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
     * 유저가 업로드한 파일을 다운로드
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
            tmpFile.createNewFile();   // 파일이 없을 때 새 파일 만들기

            fos = new FileOutputStream(tmpFile);

            Logger.l(code + " 파일 다운로드 시작.");

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer);
                size += len;
            }

            tmpFile.renameTo(file);
            Logger.l(code + " 파일 다운로드 종료. 크기: " + size);
        }
        catch (IOException e) {
            e.printStackTrace();
            Logger.l(code + " 파일 다운로드 실패.");
        }
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
            // send "LOGIN_FAIL 2" commacnd to client socket
            Logger.l(String.format("%s 비밀번호 불일치.", id));
            return;
        }
        // send "LOGIN_SUC" command to client socket
        Logger.l(String.format("%s 로그인 성공.", id));
        // sendUpdateQuery(유저의 온라인 상태를 TRUE로)
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
        Logger.l(String.format("%s 회원가입 성공.", id));
        // sendUpdateQuery("INSERT INTO User (id, pw, nick, ...) VALUES ({}, {}, {}, ...")

        askLogin(id, pw);    // 회원가입된 정보로 로그인
        return;
    }

    /**
     * 유저가 연결을 종료함. 프로그램이 종료되었을 때의 처리
     */
    private void askCutConnection() {
        // sendUpdateQuery(유저의 온라인 상태를 TRUE로)
    }

    /**
     * 유저가 채팅방에 입장하려 함
     * @param id
     * @param roomid
     */
    private void askRoomEnter(String id, int roomid) {
        // user 'id'의 chatting room list에 추가
        // chatting room 'roomid'에 user 'id' 추가
        // 'ROOM_ENTER_SUC' 메시지 보냄
        return;
    }

    /**
     * 유저가 채팅을 전송함
     * @param id
     * @param roomid
     * @param msg
     * @param imgid
     */
    private void askChat(String id, int roomid, String msg, int imgid) {
        // 채팅방 chatid에 msg, img 기록
        // 이미지 (imgid).png 파일 전송
        // CHAT_SUC roomid, NICK, msg, FONT, imgid 전송
    }

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
} // MyThread END
