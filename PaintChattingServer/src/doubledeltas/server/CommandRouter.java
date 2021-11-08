package doubledeltas.server;

import doubledeltas.utils.ByteStringReader;
import doubledeltas.utils.TransferCode;

import java.nio.charset.StandardCharsets;

public class CommandRouter {

	public CommandRouter() {
		// todo
	}
	
	public boolean route(byte[] bytes) {
		ByteStringReader bsr = new ByteStringReader(bytes);

		String id, pw, nick, msg, oldNick;
		int roomID;
		switch (TransferCode
				.valueOf(Byte.toString(bytes[0]))) {
			case LOGIN:
				bsr.setCursor(1);
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				break;
			case REGISTER:
				bsr.setCursor(1);
				id		= bsr.readInString(45);
				pw		= bsr.readInString(45);
				nick 	= bsr.readInString(45);
				break;
			case ROOM_ENTER:
				bsr.setCursor(1);
				id		= bsr.readInString(45);
				roomID	= bsr.readInInteger(4);
			default:
				break;
		}

		return false;
	}
}
