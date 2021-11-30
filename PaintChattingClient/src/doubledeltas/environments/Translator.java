package doubledeltas.environments;

public class Translator {
	public static byte[] sendLogin(String id, String pw) {
		if (id.length() > 45 || pw.length() > 45) {
			return null;
		}
		byte[] bytes = new byte[1+45+45];
		bytes[0] = (byte) TransferCode.LOGIN.ordinal();
		for(int i=0; i<id.length(); i++) bytes[1+i] = (byte)id.charAt(i);
		for(int i=0; i<pw.length(); i++) bytes[46+i] = (byte)pw.charAt(i);
		return bytes;
	}
	
	public static byte[] sendRegister(String id, String pw, String nick) {
		if (id.length() > 45 || pw.length() > 45 || nick.length() > 45) {
			return null;
		}
		byte[] bytes = new byte[1+45+45+45];
		bytes[0] = (byte) TransferCode.REGISTER.ordinal();
		for(int i=0; i<id.length(); i++) bytes[1+i] = (byte)id.charAt(i);
		for(int i=0; i<pw.length(); i++) bytes[46+i] = (byte)pw.charAt(i);
		for(int i=0; i<nick.length(); i++) bytes[91+i] = (byte)nick.charAt(i);
		return bytes;
	}
	
	public static byte[] sendConnectionCut() {
		byte[] bytes = new byte[1];
		bytes[0] = (byte) TransferCode.CONNECTION_CUT.ordinal();
		return bytes;
	}
	
	public static byte[] sendRoomEnter(String id, int roomid) {
		if (id.length() > 45) {
			return null;
		}
		byte[] bytes = new byte[1+45+4];
		bytes[0] = (byte) TransferCode.ROOM_ENTER.ordinal();
		for(int i=0; i<id.length(); i++) bytes[1+i] = (byte)id.charAt(i);
		bytes[46] = (byte)((roomid & 0xFF000000) >> 24);
		bytes[47] = (byte)((roomid & 0x00FF0000) >> 16);
		bytes[48] = (byte)((roomid & 0x0000FF00) >> 8);
		bytes[49] = (byte)((roomid & 0x000000FF));
		return bytes;
	}
	
	public static byte[] sendChat(String id, String nick, String msg) {
		if (id.length() > 45 || nick.length() > 45 || msg.length() > 1024) {
			return null;
		}
		byte[] bytes = new byte[1+45+45+1024];
		bytes[0] = (byte)TransferCode.CHAT.ordinal();
		for(int i=0; i<id.length(); i++) bytes[1+i] = (byte)id.charAt(i);
		for(int i=0; i<nick.length(); i++) bytes[46+i] = (byte)nick.charAt(i);
		for(int i=0; i<msg.length(); i++) bytes[91+i] = (byte)msg.charAt(i);
		return bytes;
	}
	
	
}
