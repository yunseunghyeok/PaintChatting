package doubledeltas.thread;

import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.*;

public class FileDownloadThread extends Thread {
    private String tmpPath;
    private Socket socket;
    private int code;
    private File file;

    public FileDownloadThread(Socket socket, int code) throws IOException {
        this.socket = socket;
        this.code = code;
        tmpPath = Environment.FILE_DIR + "\\$" + code + ".tmp";
        file = new File(tmpPath);
        file.createNewFile();   // 파일이 없을 때 새 파일 만들기
    }

    @Override
    public void run() {
        InputStream is = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[1024];
        int size = 0;
        int len;

        try {
            is = socket.getInputStream();
            fos = new FileOutputStream(file);

            Logger.l(code + " 파일 다운로드 시작.");

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer);
                size += len;
            }

            Logger.l(code + " 파일 다운로드 종료. 크기: " + size);
        }
        catch (IOException e) {
            e.printStackTrace();
            Logger.l(code + "파일 다운로드 실패.");
            return;
        }
    }
}
