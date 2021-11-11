package doubledeltas.threads.workers;

import doubledeltas.server.MysqlConnector;

import java.awt.*;
import java.net.Socket;

public class ChatWorker extends Thread {
    private MysqlConnector con;
    private Socket socket;
    private int roomid, imgid;
    private String id, msg;

    public ChatWorker(MysqlConnector con, Socket socket,
                      String id, int roomid, String msg, int imgid) {
        this.con=con;
        this.socket=socket;
        this.id=id; //user id
        this.roomid=roomid;
        this.msg=msg;
        this.img=img;
    }

    @Override
    public void run() {
        // 채팅방 chatid에 msg, img 기록
        // 이미지 (imgid).png 파일 전송
        // CHAT_SUC roomid, NICK, msg, FONT, imgid 전송
    }
}
