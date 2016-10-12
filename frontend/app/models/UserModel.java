package models;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.session;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import controllers.routes;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import objects.*;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import exceptions.DataAccessException;
import play.mvc.Http;
import utils.Mailer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Model for retrieving information about a user
 */
public class UserModel {

    /**
     * Remove a user from the database
     *
     * @param   id The id of the User you want to remove
     */
    public static void removeUser(int id) throws DataAccessException{
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            UserDAO userDAO = context.getUserDAO();
            userDAO.deleteUser(id);
            context.commit();
        }
    }

    /**
     * Block a user temporary
     *
     * @param   userid The id of the User
     * @param   roleid The id of the Role you want to give
     */
    public static void setRole(int userid, int roleid) throws DataAccessException  {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            UserDAO userDAO = context.getUserDAO();
            RoleDAO roleDAO = context.getRoleDAO();
            Role role= roleDAO.getRole(roleid);
            User user = userDAO.getUser(userid);
            System.out.println("------------------");
            user.setRole(role);
            userDAO.updateUser(user);
            context.commit();
        }
    }

    /**
     * Set a user's role by role name
     * @param userId    the user's id
     * @param roleName  the role's name
     */
    public static void setRole(int userId, String roleName) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RoleDAO roleDAO = context.getRoleDAO();
            Role role = roleDAO.getRoleByName(roleName);
            if(role != null) setRole(userId, role.getId());
        }
    }


    /**
     * Retrieve a user from the database
     * @param id ID of the user
     * @return User object containing all available information about the user
     */
    public static User getUserById(int id) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserDAO userDAO = context.getUserDAO();
            User user= userDAO.getUser(id);
            return user;
        }
    }

    /**
     * Retrieve a user from the database
     * @param user_id ID of the user
     * @return Car the Car owned by this user.
     * */
    public static Car getCarOwnedByUser(int user_id) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = context.getCarDAO();

            return carDAO.getCarByUserId(user_id);
        }
    }

    /**
     * Get the logged in user from cookies
     * @return The logged in user
     */
    public static User getCurrentUser() throws DataAccessException {
        if (session("current_user") == null) return null;
        int id = Integer.parseInt(session("current_user"));
        return getUserById(id);
    }


    /**
     * Get the logged in user from cookies as an UserSubject
     * @return The logged in user as an UserSubject
     */
    public static UserSubject getCurrentUserSubject() {

        if (session("current_user") == null) return null;
        int id = Integer.parseInt(session("current_user"));
        try{
        User user = getUserById(id);
        UserSubject usersubject = new UserSubject(user);
        return usersubject;
        }catch(DataAccessException e){
            Logger.info(e+"");
            return null;
        }

    }

    public static String getIdByMail(String email) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserDAO userDAO = context.getUserDAO();
            return userDAO.getIdByMail(email);
        }
    }

    public static void completeRegistrationUser(Http.MultipartFormData body, User u) throws DataAccessException, FileNotFoundException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Map<String, String[]> postData = body.asFormUrlEncoded();

            PlaceDAO placeDAO = dac.getPlaceDAO();
            UserDAO userDAO = dac.getUserDAO();
            AddressDAO addressDAO = dac.getAddressDAO();
            IdCardDAO idCardDAO = dac.getIdCardDAO();
            LicenseDAO licenseDAO = dac.getLicenseDAO();
            FileDAO fileDAO = dac.getFileDAO();
            Http.MultipartFormData.FilePart idCardFilePart = body.getFile("id_card_pic");
            Http.MultipartFormData.FilePart licenseFilePart = body.getFile("license_pic");

            Address address = null;
            Address domicile = null;

            if(postData.get("postalcode")[0] != null && !postData.get("postalcode")[0].equals("")
                    && postData.get("city")[0] != null && !postData.get("city")[0].equals("")
                    && postData.get("streetname")[0] != null && !postData.get("streetname")[0].equals("")
                    && postData.get("huisnummer")[0] != null && !postData.get("huisnummer")[0].equals("")) {
                Place place = InfoSessionsModel.getOrAddPlace(placeDAO, postData.get("city")[0],postData.get("postalcode")[0]);
                Integer number = Integer.parseInt(postData.get("huisnummer")[0]);
                address = InfoSessionsModel.getOrAddAddress(addressDAO, place, postData.get("streetname")[0], number, postData.get("bus") == null ? null : postData.get("bus")[0]);
            }

            u.setAddress(address);
            if(postData.get("domicile_postalcode")[0] != null && !postData.get("domicile_postalcode")[0].equals("")
                    && postData.get("domicile_city")[0] != null && !postData.get("domicile_city")[0].equals("")
                    && postData.get("domicile_streetname")[0] != null && !postData.get("domicile_streetname")[0].equals("")
                    && postData.get("domicile_huisnummer")[0] != null && !postData.get("domicile_huisnummer")[0].equals("")) {
                Place place = InfoSessionsModel.getOrAddPlace(placeDAO, postData.get("domicile_city")[0],postData.get("domicile_postalcode")[0]);
                Integer number = Integer.parseInt(postData.get("domicile_huisnummer")[0]);
                domicile = InfoSessionsModel.getOrAddAddress(addressDAO, place, postData.get("domicile_streetname")[0], number, postData.get("domicile_bus") == null ? null : postData.get("domicile_bus")[0]);
            }
            u.setDomicile(domicile);

            if(postData.get("telephone") != null && !postData.get("telephone").equals("")) u.setPhone(postData.get("telephone")[0]);

            if(postData.get("id_card_number") != null && !postData.get("id_card_number")[0].equals("")
                    && postData.get("id_card_reg") != null && !postData.get("id_card_reg")[0].equals("")
                    && idCardFilePart != null) {
                objects.File idCardFile = fileDAO.createFile(new FileInputStream(idCardFilePart.getFile()), idCardFilePart.getFilename());
                IdCard idCard;
                if((idCard = idCardDAO.getIdCard(u.getIdCard().getId())) == null){
                    idCard = idCardDAO.createIdCard(postData.get("id_card_number")[0], postData.get("id_card_reg")[0], idCardFile.getId());
                } else {
                    idCard.setNumber(postData.get("id_card_number")[0]);
                    idCard.setRegister(postData.get("id_card_reg")[0]);
                    idCard.setFile(idCardFile.getId());
                    idCardDAO.updateIdCard(idCard);
                }
                u.setIdCard(idCard);
            }

            if(postData.get("license_number") != null && !postData.get("license_number")[0].equals("")
                    && licenseFilePart != null) {
                Logger.debug(postData.get("license_number")[0]);
                objects.File licenseFile = fileDAO.createFile(new FileInputStream(licenseFilePart.getFile()), licenseFilePart.getFilename());;
                License license;
                if((license = licenseDAO.getLicense(u.getLicense().getId())) == null){
                    license = licenseDAO.createLicense(postData.get("license_number")[0], licenseFile.getId());
                } else  {
                    license.setNumber(postData.get("license_number")[0]);
                    license.setFile(licenseFile.getId());
                    licenseDAO.updateLicense(license);
                }
                u.setLicense(license);
            }

            userDAO.updateUser(u);
            dac.commit();
        }
    }

    public static User createNewUser(Map<String, String[]> postData, boolean owner) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();

            UserDAO userDAO = dac.getUserDAO();
            PlaceDAO placeDAO = dac.getPlaceDAO();
            AddressDAO addressDAO = dac.getAddressDAO();
            RoleDAO roleDAO = dac.getRoleDAO();

            String role = owner ? "new_owner" : "new_user";
            Address address = null;
            User u = null;

            if(postData.get("postalcode") != null && !postData.get("postalcode")[0].equals("")
                && postData.get("city") != null && !postData.get("city")[0].equals("")
                && postData.get("streetname") != null && !postData.get("streetname")[0].equals("")
                && postData.get("huisnummer")[0] != null && !postData.get("huisnummer")[0].equals("")) {
                Place place = InfoSessionsModel.getOrAddPlace(placeDAO, postData.get("city")[0],postData.get("postalcode")[0]);
                Integer number = Integer.parseInt(postData.get("huisnummer")[0]);
                address = InfoSessionsModel.getOrAddAddress(addressDAO, place, postData.get("streetname")[0], number, postData.get("bus") == null ? null : postData.get("bus")[0]);
            }

            if(postData.get("username") != null && !postData.get("username")[0].equals("")
                && postData.get("lastname") != null && !postData.get("lastname")[0].equals("")
                && postData.get("title") != null && !postData.get("title")[0].equals("")
                && postData.get("email") != null && !postData.get("email")[0].equals("")
                && postData.get("password") != null && !postData.get("password")[0].equals("")) {
                u = userDAO.createUser(
                        postData.get("username")[0],
                        postData.get("lastname")[0],
                        roleDAO.getRoleByName(role),
                        postData.get("title")[0],
                        postData.get("email")[0],
                        edran.auth.Authentication.hash( postData.get("password")[0]),
                        (postData.get("telephone") == null || postData.get("telephone")[0].equals("")) ? null : postData.get("telephone")[0],
                        null,
                        null,
                        address == null ? null : address.getId(),
                        null,
                        address == null ? null : address.getId(),
                        null,
                        null);
            }

            TokenDAO tDao = dac.getTokenDAO();
            Long token = createToken(u.getMail() + u.getName() + u.getPhone());
            tDao.createToken(u.getId(),token);

            Mailer.sendRegistrationConfirmation(u,token.toString());
            dac.commit();
            return u;
        }
    }

    public static List<User> getAlmostUsers() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserDAO userDAO = dac.getUserDAO();
            RoleDAO roleDAO = dac.getRoleDAO();

            //Get the almost_users and save them in a list
            Role r = roleDAO.getRoleByName("almost_user");
            //This is if should never be true
            if(r == null) return new ArrayList<User>();
            Filter<User> filter1 = userDAO.getFilter();
            filter1.fieldEquals(User.Field.ROLE, r.getId());
            List<User> list1 = userDAO.getByFilter(filter1);

            //Get the almost_owners and append them to the list
            r = roleDAO.getRoleByName("almost_owner");
            //This is if should never be true
            if(r == null) return new ArrayList<User>();
            Filter<User> filter2 = userDAO.getFilter();
            filter2.fieldEquals(User.Field.ROLE, r.getId());
            List<User> list2 = userDAO.getByFilter(filter2);
            if(list2 != null && list1 != null) list1.addAll(list2);

            return list1;
        }
    }

    public static Place getPlace(String zip,String place) throws DataAccessException{
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            PlaceDAO placeDAO = context.getPlaceDAO();
             return helper(placeDAO.getPlacesWithZip(zip),place);
        }

    }

    public static Place helper(List<Place> list, String str){
        if(list.size() == 1) return list.get(0);
        if(list.size() == 0) return null;
        int min = list.get(0).getName().length();
        str = str.toLowerCase();
        Place mostLikely = null;
        for(Place p : list) {
            int dist = StringUtils.getLevenshteinDistance(p.getName().toLowerCase(),str);
            if(dist < min ){
                min = dist;
                mostLikely = p;
            }
        }
        return mostLikely;
    }

    public static String createAndMailToken(String mail) throws DataAccessException {
        String id = getIdByMail(mail);
        if(id == null) return "Dit e-mailadres zit reeds in de database. <a href=\"" + controllers.routes.ForgotPassword.forgot() + "\" > Wachtwoord vergeten?</a>";

        Long token = createToken(mail);

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            TokenDAO tDAO = context.getTokenDAO();
            tDAO.createToken(Integer.decode(id), token);
            User u = tDAO.getUser(token);
            context.commit();
            Mailer.sendMailReset(u,token);
            return null;
        }

    }


    public static String setNewPassword(String token, String pass1) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            TokenDAO tDAO = context.getTokenDAO();
            User u = tDAO.getUser(Long.valueOf(token));
            if(u == null){
                return "Ongeldig security token, deze zijn maar eenmalig te gebruiken en vervallen na een week.";
            }
            u.setPass(edran.auth.Authentication.hash(pass1));
            u.setMailVerified();
            tDAO.deleteToken(Long.valueOf(token));
            context.getUserDAO().updateUser(u);
            context.commit();
            return null;

        }


    }
    private static final SecureRandom s = new java.security.SecureRandom();
    private static long createToken(String base){
        s.setSeed(base.hashCode() + System.currentTimeMillis());
        return s.nextLong();
    }

    public static User verifymailAndGetId(String token) throws DataAccessException {
        DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        context.begin();
        Long ltoken = Long.valueOf(token);
        Logger.error(token);

        TokenDAO tDAO = context.getTokenDAO();
        User u = tDAO.getUser(ltoken);

        u.setMailVerified();

        tDAO.deleteToken(ltoken);
        context.getUserDAO().updateUser(u);

        context.commit();
        context.close();

        return u;



    }
}
