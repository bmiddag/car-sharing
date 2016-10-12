package models;

import exceptions.DataAccessException;
import interfaces.CostDAO;
import interfaces.DataAccessContext;
import interfaces.RefuelingDAO;
import jdbc.JDBCDataAccessProvider;
import objects.Cost;
import objects.Refueling;
import play.Logger;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by laurens on 16.04.14.
 */
public class ProofsModel {

    public static List<Cost> getUnapprovedCosts() throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            CostDAO costDAO = dac.getCostDAO();
            return costDAO.getPendingCosts();
        }
    }


    public static List<Refueling> getUnapprovedRefuelings() throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            RefuelingDAO refuelingDAO = dac.getRefuelingDAO();

            return refuelingDAO.getPendingRefuelings();
        }
    }

    public static void updateProofs(Map<String, String[]> map) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            CostDAO costDAO = dac.getCostDAO();
            for(Map.Entry<String, String[]> e : map.entrySet()){
                // nullcheck
                if("".equals(e.getValue()[0])) continue;

                // the name of the fields are "xxx id" : "boolean"
                Integer id = Integer.valueOf(e.getKey().split(" ")[1]);
                Cost c = costDAO.getCost(id);
                c.setApproved(Boolean.parseBoolean(e.getValue()[0]));
                costDAO.updateCost(c);
            }
            dac.commit();
        }
    }

    /* Totally parallel to updateProofs above*/
    public static void updateRefuelings(Map<String, String[]> map) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            RefuelingDAO refuelingDAO = dac.getRefuelingDAO();
            for(Map.Entry<String, String[]> e : map.entrySet()){
                if("".equals(e.getValue()[0])) continue;
                Integer id = Integer.valueOf(e.getKey().split(" ")[1]);
                Refueling r = refuelingDAO.getRefueling(id);

                r.setApproved(Boolean.parseBoolean(e.getValue()[0]));
                refuelingDAO.updateRefueling(r);
            }
            dac.commit();
        }
    }
}
