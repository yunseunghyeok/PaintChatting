package doubledeltas.threads.workers;

import doubledeltas.server.MysqlConnector;

import java.net.Socket;

public class RoomEnterWorker extends Thread {
    private MysqlConnector con;
    private Socket socket;
    private String id;
    private int roomid;

    public RoomEnterWorker(MysqlConnector con,
                           Socket socket,
                           String id,
                           int roomid) {
        this.con = con;
        this.socket=socket;
        this.id=id;
        this.roomid=roomid;
    }

    @Override // todo
    public void run() {
        // user 'id'의 chatting room list에 추가
        // chatting room 'roomid'에 user 'id' 추가
        // 'ROOM_ENTER_SUC' 메시지 보냄
        return;
    }
}
