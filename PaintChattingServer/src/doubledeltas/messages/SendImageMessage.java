package doubledeltas.messages;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import doubledeltas.messages.*;

public class SendImageMessage extends Message
implements Broadcastable, ClientRecievable, ServerRecievable
{
	String fileName;
	File file;
	
	public SendImageMessage(String fileName, File file) {
		
	}
	
	@Override
	public void send(OutputStream os) throws IOException {
		
	}
}
