/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jdbc;

import interfaces.DataAccessContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author laurens
 */
public class JDBCDataAccessProviderTest {
    
    /**
     * Test of getDataAccessContext();
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDataAccessContext() throws Exception {
        DataAccessContext result  = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        assertNotNull(result);
    }

    /**
     * Test of getDataAccessContext();
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDataAccessContextWithPriviligeID() throws Exception {
     }

    /**
     * Test of getDataAccessContext();
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDataAccessContextWithUserID() throws Exception {

    }
    
}
