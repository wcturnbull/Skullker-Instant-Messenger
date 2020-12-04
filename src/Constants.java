/**
 * Interface containing server-client protocol constants.
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * Constants pertaining to each class are assigned hexadecimal prefixes like so:
 *      Message - 0x1
 *      Account - 0x2
 *      Chat    - 0x3
 * Generic constants that are used in multiple contexts are given the prefix, 0x.
 *
 * @author Wes Turnbull, Evan Wang CS18000, 001
 * @version 7 December 2020
 */
public interface Constants {
    byte SEND_MESSAGE       = 0x1a;
    byte DELETE_MESSAGE     = 0x1d;
    byte EDIT_MESSAGE       = 0x1e;

    byte REGISTER_ACCOUNT   = 0x2a;
    byte LOG_IN             = 0x2b;
    byte DELETE_ACCOUNT     = 0x2d;
    byte EDIT_USERNAME      = 0x21;
    byte EDIT_PASSWORD      = 0x22;

    byte ADD_USER_TO_CHAT   = 0x3a;
    byte CREATE_CHAT        = 0x3c;
    byte LEAVE_CHAT         = 0x3d;

    byte REQUEST_DATA       = 0x0;
    byte CONTINUE           = 0xc;
    byte DENIED             = 0xd;
}
