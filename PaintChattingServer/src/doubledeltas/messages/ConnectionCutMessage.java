package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class ConnectionCutMessage extends Message
implements ClientRecievable, ServerRecievable
{
	public ConnectionCutMessage() {
		this.type = TransferCode.CONNECTION_CUT;
	}
}
