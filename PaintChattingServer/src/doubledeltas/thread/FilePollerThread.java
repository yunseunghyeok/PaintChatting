package doubledeltas.thread;

import doubledeltas.server.CommandRouter;
import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

import java.io.IOException;
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
        while (true) {
            try {
                listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);

                Logger.l(String.format("연결 대기"));

                socket = listener.accept();	// 연결 대기, 이후 연결 완료

                Logger.l(String.format("FT - 클라이언트 [{}]가 접속되었습니다.", socket.getInetAddress().toString()));

                is = socket.getInputStream();
                os = socket.getOutputStream();

                is.read(buffer);

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
                    Logger.l("FT - 클라이언트와 통신 중 오류가 발생해 연결이 중단되었습니다.");
                }
                return;
            }
        } // while-loop END
    } // run method END
} // MyThread END
