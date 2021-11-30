package doubledeltas.messages;

import java.util.HashMap;
import java.util.Vector;

public class MessageQueues {
	HashMap<Byte, Vector<Message>> hm;
	
	public MessageQueues() {
		hm = new HashMap<Byte, Vector<Message>>();
	}
}
