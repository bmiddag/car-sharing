package models;

import exceptions.DataAccessException;
import interfaces.CarDAO;
import interfaces.CarPhotoDAO;
import interfaces.DamageDocDAO;
import interfaces.DataAccessContext;
import jdbc.JDBCDataAccessProvider;
import objects.CarPhoto;
import objects.DamageDoc;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wouter Pinnoo
 * @author Gilles Vandewiele
 */
public class CarModel {
    public static objects.Car getCarById(int id) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = context.getCarDAO();
            return carDAO.getCar(id);
        }
    }

    public static List<CarPhoto> getPhotosByCar(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarPhotoDAO carPhotoDAO = dac.getCarPhotoDAO();
            return carPhotoDAO.getPhotosByCar(id);
        }
    }
}
