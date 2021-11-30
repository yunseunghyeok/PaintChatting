package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class SendImageMessage extends Message
implements Broadcastable, ClientRecievable, ServerRecievable
{
	String fileName;
	File file;
	
	public SendImageMessage(String fileName, File file) {
		this.fileName = new String(fileName);
		this.file = file;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		// TODO: ±¸Çö
	}
}
