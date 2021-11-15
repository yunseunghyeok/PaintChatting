package doubledeltas.client.senders;

import doubledeltas.environments.TransferCode;

public class LoginSender extends Sender {
	public LoginSender(String id, String pw) {
		if (id.length() > 45 || pw.length() > 45) {
			return;
		}
		
		msg = new byte[91];
		msg[0] = (byte) TransferCode.LOGIN.ordinal();
		for(int i=0; i<id.length(); i++) msg[1+i] = (byte)id.charAt(i);
		for(int i=0; i<pw.length(); i++) msg[46+i] = (byte)id.charAt(i);
	}
}
