package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserFontChangeMessage extends Message
implements ServerRecievable
{
	private String id, newFont;
	
	public UserFontChangeMessage(String id, String newFont) {
		this.type = TransferCode.USER_FONT_CHANGE;
		this.id = new String(id);
		this.newFont = new String(newFont);
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
		dos.writeUTF(newFont);
	}
	
	public String getID() { return id; }
	public String getNewFont() { return newFont; }
}
