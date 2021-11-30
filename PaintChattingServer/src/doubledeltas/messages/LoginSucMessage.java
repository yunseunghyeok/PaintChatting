package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class LoginSucMessage extends Message
implements ClientRecievable
{	
	public LoginSucMessage() {
		this.type = TransferCode.LOGIN_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
