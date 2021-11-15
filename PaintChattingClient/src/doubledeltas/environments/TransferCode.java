/*  Last Update: 20211114 - 8th meeting
 *  Authored by DoubleDeltas
 */

package doubledeltas.environments;

public enum TransferCode {
    // ---- 0x0x ----
    NULL,
    LOGIN,
    LOGIN_SUC,
    LOGIN_FAIL,
    REGISTER,
    REGISTER_SUC,
    REGISTER_FAIL,
    CONNECTION_CUT,

    // ---- 0x1x ----
    ROOM_ENTER,
    ROOM_ENTER_SUC,
    ROOM_ENTER_FAIL,
    ROOM_SOMEONE_JOINED,
    CHAT,
    CHAT_SUC,
    CHAT_FAIL,
    USER_NICK_CHANGE,

    // ---- 0x2x ----
    USER_NICK_CHANGE_SUC,
    USER_NICK_CHANGE_FAIL,
    USER_PROFILE_CHANGE,
    USER_PROFILE_CHANGE_SUC,
    USER_PROFILE_CHANGE_FAIL
}
