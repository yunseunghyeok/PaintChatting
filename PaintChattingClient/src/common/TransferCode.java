/*  Last Update: 20211119
 *  Authored by DoubleDeltas
 */

package common;

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

    ROOM_ENTER,
    ROOM_ENTER_SUC,
    ROOM_ENTER_FAIL,
    ROOM_SOMEONE_JOINED,
    CHAT,
    CHAT_SUC,
    CHAT_FAIL,
    SEND_IMAGE,

    // ---- 0x1x ----
    USER_NICK_CHANGE,
    USER_NICK_CHANGE_SUC,
    USER_NICK_CHANGE_FAIL,
    USER_PROFILE_CHANGE,
    USER_PROFILE_CHANGE_SUC,
    USER_PROFILE_CHANGE_FAIL;
}
