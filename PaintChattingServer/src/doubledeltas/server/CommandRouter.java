package doubledeltas.server;

import doubledeltas.utils.TransferCode;

public class CommandRouter {
	private int cur = 0;

	public CommandRouter() {
		// todo
	}

	public byte[] readBytesFrom(byte[] bytes, int count) {
		int len = Math.min(count, bytes.length - cur);
		if (len < 1) return null;

		byte[] res = new byte[len];
		for(int j=0; j<len; j++)
			res[j] = bytes[cur+j];
		cur += len;
		return res;
	}
	
	public boolean route(byte[] bytes) {
		switch (TransferCode
				.valueOf(Byte.toString(bytes[0]))) {
			case CHAT:
				cur = 1;
				String id = new String(readBytesFrom(bytes, 45));
				String pw = new String(readBytesFrom(bytes, 45));
				break;
			default:
				break;
		}

		return false;
	}
}
