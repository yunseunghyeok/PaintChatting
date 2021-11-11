package doubledeltas.threads;

import doubledeltas.utils.Environment;
import doubledeltas.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileDownloadThread extends Thread {
    private String tmpName, fileName;
    private PollerThread poller;
    private InputStream is;
    private int code;
    private File tmpFile, file;

    public FileDownloadThread(PollerThread poller, InputStream is, int code) throws IOException {
        this.poller = poller;
        this.is = is;
        this.code = code;
        tmpName     = "$" + code + ".tmp";
        fileName    = code + ".png";
        tmpFile     = new File(Environment.FILE_DIR, tmpName);
        file        = new File(Environment.FILE_DIR, fileName);
    }

    @Override
    public void run() {
        FileOutputStream fos = null;
        byte[] buffer = new byte[1024];
        int size = 0;
        int len;

        try {
            tmpFile.createNewFile();   // 파일이 없을 때 새 파일 만들기

            fos = new FileOutputStream(tmpFile);

            Logger.l(code + " 파일 다운로드 시작.");

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer);
                size += len;
            }

            tmpFile.renameTo(file);
            Logger.l(code + " 파일 다운로드 종료. 크기: " + size);
        }
        catch (IOException e) {
            e.printStackTrace();
            Logger.l(code + "파일 다운로드 실패.");
        }
        finally {
            poller.notify();
            return;
        }
    }
}
