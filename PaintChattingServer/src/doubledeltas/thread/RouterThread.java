package doubledeltas.thread;

import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.Environment;
import doubledeltas.utils.TransferCode;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RouterThread extends Thread {
	private byte[] bytes;
	ByteStringReader bsr;
	Socket socket;

	public RouterThread(Socket socket) throws IOException {
		this.socket = socket;

		bytes = new byte[1024];
		InputStream is = socket.getInputStream();
		is.read(bytes);

		bsr = new ByteStringReader(bytes);
	}

	@Override
	public void run() {
		String id, pw, nick, msg, oldnick;
		int roomid, imgid;
		Font font;
		Image img;

		bsr.setCursor(2);	// 앞 2 Byte는 Magic Number(0xC4 0xA7)이었음
		switch (TransferCode
				.valueOf(Byte.toString(bytes[2]))) {
			case LOGIN:
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				//askLogin(id, pw);
				break;
			case REGISTER:
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				nick 	= bsr.readInString(45);
				//askRegister(id, pw, nick);
				break;
			case CONNECTION_CUT:
				//cutConnection();
				break;
			case ROOM_ENTER:
				id		= bsr.readInString(45);
				roomid	= bsr.readInInteger(4);
				//askRoomEnter(id, roomID);
				break;
			case CHAT:
				roomid	= bsr.readInInteger(4);
				nick	= bsr.readInString(45);
				msg		= bsr.readInString(1024);
				font	= new Font(bsr.readInString(45), Font.PLAIN, 10);
				imgid	= bsr.readInInteger(4);
				if (imgid == 0) img = null; else img = waitImage(imgid);
				//chat(roomID, nick, msg, font, img);
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
				if (imgid == 0) img = null; else img = waitImage(imgid);
				// askUserProfileChange(id, img);
				break;
			default:
				break;
		}
		return;
	}

	protected Image waitImage(int imgid) {
		//String path = Environment.FILE_DIR + "\\$" + imgid + ".png";
		File f = new File(path);
		while (!f.exists()) {}
		return new ImageIcon(path).getImage();
	}
}
