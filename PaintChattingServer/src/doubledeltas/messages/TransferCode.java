/*  Last Update: 20211119
 *  Authored by DoubleDeltas
 */

package doubledeltas.messages;

public class TransferCode {
	public static final byte
	LOGIN						= 0x01,
	LOGIN_SUC					= 0x02,
	LOGIN_FAIL 					= 0x03,
	REGISTER					= 0x04,
	REGISTER_SUC				= 0x05,
	REGISTER_FAIL				= 0x06,
	CONNECTION_CUT				= 0x07,
	ROOM_ENTER					= 0x08,
	ROOM_ENTER_SUC				= 0x09,
	ROOM_ENTER_FAIL				= 0x0A,
	ROOM_SOMEONE_JOINED			= 0x0B,
	CHAT						= 0x0C,
	CHAT_SUC					= 0x0D,
	CHAT_FAIL					= 0x0E,
	SEND_IMAGE					= 0x0F,
	
	USER_NICK_CHANGE			= 0x10,
	USER_NICK_CHANGE_SUC		= 0x11,
	USER_NICK_CHANGE_FAIL		= 0x12,
    USER_PROFILE_CHANGE			= 0x13,
    USER_PROFILE_CHANGE_SUC		= 0x14,	
    USER_PROFILE_CHANGE_FAIL	= 0x15,
    ROOM_CREATE					= 0x16,
    ROOM_CREATE_SUC				= 0x17,
    ROOM_CREATE_FAIL			= 0x18,
    USER_FONT_CHANGE			= 0x19,
    USER_FONT_CHANGE_SUC		= 0x1A,
    USER_FONT_CHANGE_FAIL		= 0x1B;
}
