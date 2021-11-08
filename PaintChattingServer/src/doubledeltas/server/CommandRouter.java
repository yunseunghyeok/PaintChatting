package doubledeltas.server;

import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.TransferCode;

import java.awt.*;
import java.nio.charset.StandardCharsets;

public class CommandRouter implements Runnable {
	private byte[] bytes;
	ByteStringReader bsr;
	Thread th;

	public CommandRouter(byte[] bytes) {
		this.bytes = bytes;
		bsr = new ByteStringReader(bytes);

		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		String id, pw, nick, msg, oldnick;
		int roomid, imgid;
		Font font;
		Image img;

		bsr.setCursor(1);
		switch (TransferCode
				.valueOf(Byte.toString(bytes[0]))) {
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
				if (imgid == 0) img = null;
				// else: imgid의 이미지가 전송되기를 기다림
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
			default:
				break;
		}
		return;
	}
}
