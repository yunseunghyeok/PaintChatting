package doubledeltas.thread;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FilePollerThread extends Thread {
    @Override
    public void run() {
        InputStream     is			= null;
        OutputStream    os			= null;
        ServerSocket    listener    = null;
        Socket          socket      = null;
        byte[]          buffer      = new byte[32768];

    }
}
