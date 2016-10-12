package jdbc;

import exceptions.DataAccessException;
import interfaces.Filter;
import interfaces.TokenDAO;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Token;
import objects.User;

class JDBCTokenDAO extends JDBCDataAccessObject<Token> implements TokenDAO{

    public JDBCTokenDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Token.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Token.Field.TOKEN;
    }

    @Override
    public Filter<Token> getFilter() {
        return new JDBCFilter<Token>() {};
    }
    
    @Override
    public User getUser(Long token) throws DataAccessException {
        Token t = getSingleByAttr("token", token);
        return new JDBCUserDAO(dac).getUser(t.getUid()); 
    }

    @Override
    public void createToken(Integer uid, Long token) throws DataAccessException {
        create(uid, token);
    }

    @Override
    public void deleteToken(Long token) throws DataAccessException {
        deleteByAttr("token", token);
    }   
    
}
