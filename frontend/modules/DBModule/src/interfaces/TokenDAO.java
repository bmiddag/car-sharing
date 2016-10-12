
package interfaces;

import exceptions.DataAccessException;
import objects.Token;
import objects.User;

/**
 * This DAO lets enables access to security tokens related to users.
 * They're automatically deleted after a week has passed.
 */
public interface TokenDAO extends DataAccessObject<Token> {
    
    public User getUser(Long token) throws DataAccessException;
    
    public void createToken(Integer id, Long token) throws DataAccessException;
    
    public void deleteToken(Long token) throws DataAccessException;
    
}
