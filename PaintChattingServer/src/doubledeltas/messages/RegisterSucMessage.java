package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class RegisterSucMessage extends Message
implements ClientRecievable
{	
	public RegisterSucMessage() {
		this.type = TransferCode.REGISTER_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
