package doubledeltas.threads.workers;

import doubledeltas.server.MysqlConnector;
import doubledeltas.utils.Logger;

import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWorker extends Thread {
    protected MysqlConnector con;
    protected Socket socket;
    protected String id, pw;

    public LoginWorker(MysqlConnector con, Socket socket, String id, String pw) {
        this.con = con;
        this.socket = socket;
        this.id = id;
        this.pw = pw;
    }

    protected boolean doesIDexists() {
        try {
            ResultSet rs = con.sendQuery(String.format("SELECT id FROM User WHERE id={}", id));
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isPWcorrect() {
        try {
            ResultSet rs = con.sendQuery(String.format(
                    "SELECT pw FROM (SELECT id FROM User WHERE id={}) WHERE pw={}",
                    id, pw)
            );
            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // todo: 로그인

    /**
     * 로그인 작업
     */
    @Override
    public void run() {
        if (!doesIDexists()) {
            // send "LOGIN_FAIL 1" command to client socket
            Logger.l(String.format("{} 로그인 실패.", id));
            return;
        }
        if (!isPWcorrect()) {
            // send "LOGIN_FAIL 2" command to client socket
            Logger.l(String.format("{} 비밀번호 불일치.", id));
            return;
        }
        // send "LOGIN_SUC" command to client socket
        Logger.l(String.format("{} 로그인 성공.", id));
        // sendUpdateQuery(유저의 온라인 상태를 TRUE로)
        return;
    }

}
