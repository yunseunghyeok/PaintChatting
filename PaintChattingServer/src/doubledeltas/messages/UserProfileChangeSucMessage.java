package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class UserProfileChangeSucMessage extends Message
implements ClientRecievable
{
	public UserProfileChangeSucMessage() {
		this.type = TransferCode.USER_PROFILE_CHANGE_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
