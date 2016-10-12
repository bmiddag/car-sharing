package models;

import exceptions.DataAccessException;
import interfaces.DamageDocDAO;
import interfaces.DataAccessContext;
import interfaces.RideDAO;
import jdbc.JDBCDataAccessProvider;
import objects.DamageDoc;
import objects.User;
import play.Logger;
import utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wouter Pinnoo on 30/04/14.
 */
public class DamageDocModel {

    public static List<DamageDoc> getDamageDocsForDamage(objects.Damage damage) throws DataAccessException {
        List<DamageDoc> list = new ArrayList<>();
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            DamageDocDAO damageDocDAO = context.getDamageDocDAO();
            list.addAll(damageDocDAO.getDamageDocByDamage(damage));
        }
        return list;
    }
}
