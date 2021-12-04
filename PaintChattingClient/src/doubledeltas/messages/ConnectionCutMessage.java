package doubledeltas.messages;

public class ConnectionCutMessage extends Message
implements ClientRecievable, ServerRecievable
{
	public ConnectionCutMessage() {
		this.type = TransferCode.CONNECTION_CUT;
	}
}
