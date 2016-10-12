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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.Filter;
import interfaces.UserDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.User;
import jdbc.JDBCDataAccessProvider;





public class Authentication {

    /**
     * Authenticates a user with the given email and pass.
     *
     * @param email
     * @param pass
     * @return  0 if authentication succeeds
     *          -1 Database failure or worse
     *          1 Wrong password
     *          2 Unknown email
     *          3 Unverified email
     *          
     */
    public static int authenticate(String email, String pass) {
            /* Initialize Database access */
            DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();            
            dap.init("test", "auth", "auth");

            try (DataAccessContext context = dap.getDataAccessContext();) {
                context.begin();

                /* Retrieve the stored hash, compute the hash of this password and compare both */
                try (UserDAO udao = context.getUserDAO();) {
                    if (udao == null) {
                        return -1;
                    }
                    Filter<User> filter = context.getUserDAO().getFilter() ;
                    filter.fieldEqualsCaseInsensitive(User.Field.MAIL, email);                    
                    List<User> users = udao.getByFilter(filter);                    
                    if(users.isEmpty()) return 2;
                    User u = users.get(0);
                    if(!u.getMailVerified()) return 3;
                    
                    String storedHash = u.getPass();                    

                    if (PasswordHash.validatePassword(pass, storedHash)) {
                        return 0;
                    } else {
                        return 1;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
                    return -1;
            }
        } catch (exceptions.DataAccessException e) {  
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, e);
            return 1;
        } 

    }// authenticate
    
    public static String hash(String pass) {
        try {
            return PasswordHash.createHash(pass);
        } catch (NoSuchAlgorithmException |InvalidKeySpecException ex) {
            return null;
        }
    }
}
