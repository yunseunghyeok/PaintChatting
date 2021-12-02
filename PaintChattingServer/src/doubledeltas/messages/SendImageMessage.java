package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
		super.send(dos);
		dos.writeUTF(fileName);
		dos.writeLong(file.length());
		
		FileInputStream fin = new FileInputStream(this.file);
		byte[] buf = new byte[4096];
		while (fin.read(buf) != -1) {
			dos.write(buf);
		}
		fin.close();
	}
	
	public String getFileName() { return fileName; }
	public File getFile() { return file; }
}
