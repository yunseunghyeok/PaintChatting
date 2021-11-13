package doubledeltas.threads.workers;

import doubledeltas.server.MysqlConnector;
import doubledeltas.utils.Logger;

import java.net.Socket;

public class RegistrationWorker extends LoginWorker {
    private String nick;

    public RegistrationWorker(MysqlConnector con,
                              Socket socket,
                              String id, String pw, String nick) {
        super(con, socket, id, pw);
        this.nick = nick;
    }

    @Override
    public void run() {
        if (doesIDexists()) {
            // send "REGISTER_FAIL 1" command to client socket
            Logger.l(String.format("{} 회원가입 실패. ID 중복.", id));
            return;
        }
        // send "REGISTER_SUC" command to client socket
        Logger.l(String.format("{} 회원가입 성공.", id));
        // sendUpdateQuery("INSERT INTO User (id, pw, nick, ...) VALUES ({}, {}, {}, ...")

        super.run();    // 회원가입된 정보로 로그인
        return;
    }
}
