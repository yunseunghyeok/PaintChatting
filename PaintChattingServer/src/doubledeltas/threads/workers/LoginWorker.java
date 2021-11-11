package doubledeltas.threads.workers;

import doubledeltas.server.MysqlConnector;

public class LoginWorker extends Thread {
    MysqlConnector con;
    String id, pw;

    public LoginWorker(MysqlConnector con, String id, String pw) {
        this.con = con;
        this.id = id;
        this.pw = pw;
    }

    @Override
    public void run() {

    }
}
