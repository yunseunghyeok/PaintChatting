package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class UserProfileChangeMessage extends Message
implements ServerRecievable
{
	private static final int MSG_SIZE = 1+45+45;
	private String id, fileName;
	
	public UserProfileChangeMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.USER_PROFILE_CHANGE.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.id = bsr.readInString(45);
		this.fileName = bsr.readInString(45);
	}
	
	public UserProfileChangeMessage(String id, String fileName) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.USER_PROFILE_CHANGE.getByte();
		
		bsr.setCursor(1);
		bsr.writeString(id, false);
		bsr.moveCursor(45);
		bsr.writeString(fileName, false);
		
		this.id=new String(id);
		this.fileName=new String(fileName);
	}
	
	public String getID() { return id; }
	public String getFileName() { return fileName; }
 }
