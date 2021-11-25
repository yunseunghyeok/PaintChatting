package doubledeltas.messages;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Vector;

public abstract class BroadcastableMessage extends Message {
	
	public void broadcast(HashMap<Integer, Vector<OutputStream>> hm,
			int roomid) throws Exception
	{
		Vector<OutputStream> streams = null;
		for (int id : hm.keySet()) {
			if (id == roomid) {
				streams = hm.get(id);
				break;
			}
		}
		if (streams == null) throw new Exception("Room ID Å½»ö ½ÇÆÐ");
		
		for (OutputStream os : streams) {
			send(os);
		}
	}
}
