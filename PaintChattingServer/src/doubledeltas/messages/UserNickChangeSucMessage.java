package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class UserNickChangeSucMessage extends Message
implements ClientRecievable
{
	public UserNickChangeSucMessage() {
		this.type = TransferCode.USER_NICK_CHANGE_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
