package doubledeltas.threads;

import doubledeltas.server.MysqlConnector;
import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PollerThread extends Thread {
    MysqlConnector con;
    public PollerThread(MysqlConnector con) {
        this.con = con;
    }

    @Override
    public void run() {
        InputStream         is			= null;
        OutputStream        os			= null;
        ServerSocket        listener    = null;
        Socket              socket      = null;
        Thread              th          = null;

        try {
            listener = new ServerSocket(Environment.CLIENT_TO_SERVER_PORT);

            Logger.l(String.format("연결 대기"));
            while(true) {
                socket = listener.accept();    // 연결 대기, 이후 연결 완료

                Logger.l(String.format("클라이언트 [{}]가 접속되었습니다.", socket.getInetAddress().toString()));

                is = socket.getInputStream();
                os = socket.getOutputStream();

                // 첫 4바이트를 읽어 COMMAND/FILE 여부를 판단.
                byte[] firstFour = new byte[4];
                is.read(firstFour);
                int code = new ByteStringReader(firstFour).readInInteger();

                if (code == 0x00000000) {
                    // 첫 4바이트(code)가 000...0이면 COMMAND
                    th = new RouterThread(this, con, socket);
                    th.start();
                } else {
                    // 아닐 경우 code를 식별코드로 하는 파일 다운로드
                    th = new FileDownloadThread(this, socket, code);
                    th.start();
                }
                this.wait();
                // FileDownloadThread or RouterThread에 의해 notify됨
                socket.close();
                Logger.l(String.format("클라이언트 [{}] 연결 종료, 연결 대기", socket.getInetAddress().toString()));
            }
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
        } // try-catch-finally END
    } // run method END
} // MyThread END
