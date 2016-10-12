package models;

import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import objects.Address;
import objects.Place;
import objects.User;
import play.Logger;
import play.mvc.Http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Gilles
 */
public class MyInfoModel {

    /**
     * Analogous to the change email method, but then ofcourse for a password.
     * @param oldPassword
     * @param newPassword1
     * @param newPassword2
     * @return
     */
    public static int changePassword(String oldPassword, String newPassword1, String newPassword2) throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            User u = UserModel.getCurrentUser();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            if(newPassword1.equals(newPassword2) && edran.auth.Authentication.authenticate(u.getMail(), oldPassword) == 0 ){
                u.setPass(edran.auth.Authentication.hash(newPassword1));
                userDAO.updateUser(u);
                dac.commit();
                return 0;
            } else return 2;
        }

    }

    /**
     * Change the email. Allows the user to enter his new email twice to make sure no typos are made.
     * @param newEmail1
     * @param newEmail2
     * @return an error code, 0 means the email was changed successfully, 1 indicates a wrong old email or the two new emails are not identical, 2 means a database error
     */
    public static int changeEmail(String newEmail1, String newEmail2) throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            User u = UserModel.getCurrentUser();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            if(newEmail1.equals(newEmail2)){
                u.setMail(newEmail1);
                userDAO.updateUser(u);
                dac.commit();
                return 0;
            } else return 2;
        }
    }

    public static int changePhone(String newPhone) throws DataAccessException{

        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            User u = UserModel.getCurrentUser();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            u.setPhone(newPhone);
            userDAO.updateUser(u);
            dac.commit();
            return 0;
        }
    }

    public static int changeAddress(String street, String number, String bus, String city, String PCode) throws DataAccessException{

        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            User u = UserModel.getCurrentUser();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            AddressDAO addressDAO = dac.getAddressDAO();
            PlaceDAO placeDAO = dac.getPlaceDAO();
            Place p = UserModel.helper(placeDAO.getPlacesWithZip(PCode), city);
            if(p == null) return 1;
            Address a = addressDAO.createAddress(p, street, Integer.valueOf(number), bus);
            u.setAddress(a);
            userDAO.updateUser(u);
            dac.commit();
            return 0;
        }catch(NullPointerException e){
            return 2;
        }

    }

    public static int changeDomicile(String street, String number, String bus, String city, String PCode) throws DataAccessException{

        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            User u = UserModel.getCurrentUser();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            AddressDAO addressDAO = dac.getAddressDAO();
            PlaceDAO placeDAO = dac.getPlaceDAO();
            Place p = UserModel.helper(placeDAO.getPlacesWithZip(PCode), city);
            if(p == null) return 1;
            Address a = addressDAO.createAddress(p, street, Integer.valueOf(number), bus);
            u.setDomicile(a);
            userDAO.updateUser(u);
            dac.commit();
            return 0;
        }catch(NullPointerException e){
            return 2;
        }

    }

    public static int changeProfilePicture(Http.MultipartFormData.FilePart filePart) throws DataAccessException, FileNotFoundException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            objects.File file = null;
            UserDAO userDAO = dac.getUserDAO();
            if(filePart != null){
                FileDAO fileDAO = dac.getFileDAO();
                file = fileDAO.createFile(new FileInputStream(filePart.getFile()), filePart.getFilename());

            }
            if(file != null){
                User u = UserModel.getCurrentUser();
                u.setPhoto(file.getId());
                userDAO.updateUser(u);
                dac.commit();
            }
        }
        return 1;
    }


}
