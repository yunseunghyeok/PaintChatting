package doubledeltas.threads.workers;

import java.net.Socket;

public class CutConnectionWorker extends Thread {
    private Socket socket;

    public CutConnectionWorker(Socket socket) {
        this.socket = socket;
    }

    @Override //todo
    public void run() {
        // sendUpdateQuery(유저의 온라인 상태를 TRUE로)
    }
}
