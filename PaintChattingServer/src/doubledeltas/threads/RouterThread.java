package doubledeltas.threads;

import doubledeltas.server.MysqlConnector;
import doubledeltas.threads.workers.*;
import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.Environment;
import doubledeltas.utils.TransferCode;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class RouterThread extends Thread {
	private byte[] bytes;
	MysqlConnector con;
	ByteStringReader bsr;
	PollerThread poller;
	Socket socket;
	InputStream is;

	public RouterThread(PollerThread poller,
						MysqlConnector con,
						Socket socket) throws IOException {
		this.con = con;
		this.poller = poller;

		bytes = new byte[1024];
		this.socket = socket;
		this.is = socket.getInputStream();
		is.read(bytes);

		bsr = new ByteStringReader(bytes);
	}

	@Override
	public void run() {
		String id, pw, nick, msg, oldnick;
		int roomid, imgid;
		Font font;
		Image img;
		Thread worker = null;

		bsr.setCursor(2);	// 앞 2 Byte는 Magic Number(0xC4 0xA7)이었음
		switch (TransferCode
				.valueOf(Byte.toString(bytes[2]))) {
			case LOGIN:
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				worker = new LoginWorker(con, socket, id, pw);
				break;
			case REGISTER:
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				nick 	= bsr.readInString(45);
				worker = new RegistrationWorker(con, socket, id, pw, nick);
				break;
			case CONNECTION_CUT:
				worker = new CutConnectionWorker(socket);
				break;
			case ROOM_ENTER:
				id		= bsr.readInString(45);
				roomid	= bsr.readInInteger(4);
				worker = new RoomEnterWorker(con, socket, id, roomid);
				break;
			case CHAT:
				id		= bsr.readInString(45);
				roomid	= bsr.readInInteger(4);
				msg		= bsr.readInString(1024);
				imgid	= bsr.readInInteger(4);
				worker = new ChatWorker(con, socket, id, roomid, msg, imgid);
				break;
			case USER_NICK_CHANGE:
				id		= bsr.readInString(45);
				oldnick	= bsr.readInString(45);
				nick	= bsr.readInString(45);
				// askUserNickChange(id, oldnick, nick);
				break;
			case USER_PROFILE_CHANGE:
				id		= bsr.readInString(45);
				imgid	= bsr.readInInteger(4);
				// askUserProfileChange(id, img);
				break;
			default:
				break;
		}
		worker.start();
		poller.notify();
		return;
	}

	/*
	protected Image getImage(int imgid) {
		String path = Environment.FILE_DIR + "\\" + imgid + ".png";
		File f = new File(Environment.FILE_DIR, imgid + ".png");
		if (!f.exists())
			return null;	// 먼저 클라이언트로부터 다운로드 받아야함!
		return new ImageIcon(path).getImage();
	}
	 */
}
