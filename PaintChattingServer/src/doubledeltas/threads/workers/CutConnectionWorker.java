package doubledeltas.threads.workers;

import java.net.Socket;

public class CutConnectionWorker extends Thread {
    private Socket socket;

    public CutConnectionWorker(Socket socket) {
        this.socket = socket;
    }
}
