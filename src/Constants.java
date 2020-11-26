/**
 * Interface containing server-client protocol constants.
 *
 * Constants pertaining to each class are assigned hexadecimal prefixes like so:
 *      Message - 0x1
 *      Account - 0x2
 *      Chat    - 0x3
 *
 * From there, individual constants are assigned meanings like so:
 *      0x1a - Send message to chat.
 *      0x1d - Delete message from chat.
 *      0x1e - Edit message from chat.
 *
 *      0x2a - Register for account.
 *      0x2b - Log into account.
 *
 *      0x3a - Add user to chat.
 *      0x3c - Create chat.
 *      0x3d - Delete chat.
 */
public interface Constants {
    byte SEND_MESSAGE       = 0x1a;
    byte DELETE_MESSAGE     = 0x1d;
    byte EDIT_MESSAGE       = 0x1e;

    byte REGISTER_ACCOUNT   = 0x2a;
    byte LOG_IN             = 0x2b;
    byte INVALID_ACCOUNT    = 0x2c;
    byte FOUND_ACCOUNT      = 0x2f;

    byte ADD_USER_TO_CHAT   = 0x3a;
    byte CREATE_CHAT        = 0x3c;
    byte DELETE_CHAT        = 0x3d;

    byte REQUEST_DATA       = 0xa;
    byte CONTINUE           = 0xc;
    byte CLIENT_DISCONNECT  = 0xd;
}
