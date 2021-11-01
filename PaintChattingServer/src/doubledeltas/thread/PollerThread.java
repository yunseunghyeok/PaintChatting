package doubledeltas.thread;

import doubledeltas.server.CommandRouter;
import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PollerThread extends Thread {
    @Override
    public void run() {
        InputStream         is			= null;
        OutputStream        os			= null;
        ServerSocket        listener    = null;
        Socket              socket      = null;
        CommandRouter       router      = new CommandRouter();
        byte[]              buffer      = new byte[10 * (1 << 20)]; // 10 Mibi bytes

        while (true) {
            try {
                listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);
                socket = listener.accept();	// 연결 대기, 이후 연결 완료

                Logger.l(String.format("클라이언트 [{}]가 접속되었습니다.", socket.getInetAddress().toString()));

                is = socket.getInputStream();
                os = socket.getOutputStream();

                is.read(buffer);
                router.route(buffer);

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
            }
        } // while-loop END
    } // run method END
} // MyThread END
