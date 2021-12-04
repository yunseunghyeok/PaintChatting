package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserFontChangeSucMessage extends Message
implements ClientRecievable
{	
	public UserFontChangeSucMessage() {
		this.type = TransferCode.USER_FONT_CHANGE_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
