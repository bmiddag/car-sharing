/****************************************************************************
*                                                                           *
*  ___________________ __________    _____    _______   Verantwoordelijke   *
*  \_   _____/\______ \\______   \  /  _  \   \      \                      *
*  |    __)_  |    |  \|       _/ /  /_\  \  /   |   \       Laurens        *
*  |        \ |    `   \    |   \/    |    \/    |    \     De Graeve       *
*  /_______  //_______  /____|_  /\____|__  /\____|__  /                    *
*          \/         \/       \/         \/         \/                     *
*   _____          __  .__        _____             .___    .__             *
*   /  _  \  __ ___/  |_|  |__    /     \   ____   __| _/_ __|  |   ____    *
*  /  /_\  \|  |  \   __\  |  \  /  \ /  \ /  _ \ / __ |  |  \  | _/ __ \   *
* /    |    \  |  /|  | |   Y  \/    Y    (  <_> ) /_/ |  |  /  |_\  ___/   *
* \____|__  /____/ |__| |___|  /\____|__  /\____/\____ |____/|____/\___  >  *
*         \/                 \/         \/            \/               \/   *
*                                                                           *
*                                                                           *
****************************************************************************/
package edran.auth;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Laurens De Graeve
 */
public class PasswordHashTest {


    @Test
    public void testHashesNotSame() throws Exception {
        for (int i = 0; i < 100; i++) {
            String password = "" + i + i * 3 + i;
            String hash = PasswordHash.createHash(password);
            String secondHash = PasswordHash.createHash(password);
            if (hash.equals(secondHash)) {
                fail("FAILURE: TWO HASHES ARE EQUAL!");
            }
        }
    }
    
    @Test
    public void testWrongPass() throws Exception {
        for (int i = 0; i < 100; i++) {
            String password = "" + i + i * 3 + i;
            String hash = PasswordHash.createHash(password);
            if (PasswordHash.validatePassword(password + i, hash)) {
                fail("FAILURE: Wrong password accepted.");
            }
        }
    }
    
    @Test
    public void testCorrectPass() throws Exception {
        for (int i = 0; i < 100; i++) {
            String password = "" + i + i * 3 + i;
            String hash = PasswordHash.createHash(password);
            if (!PasswordHash.validatePassword(password, hash)) {
                fail("FAILURE: Correct password not accepted.");
            }
        }
    }
    
        @Test
    public void ABUSE_REMOVE_ME_PLEASE() throws Exception {
        System.out.println(PasswordHash.createHash("pass"));
        //System.out.println(PasswordHash.createHash("owner"));
        System.out.println(PasswordHash.createHash("owner"));
        for (int i = 0; i < 100; i++) {
            String password = "" + i + i * 3 + i;
            String hash = PasswordHash.createHash(password);
            if (PasswordHash.validatePassword(password + i, hash)) {
                fail("FAILURE: Wrong password accepted.");
            }
        }
    }
}
