package models;

import interfaces.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jdbc.JDBCDataAccessProvider;

import objects.*;

import play.Logger;

import utils.Mailer;
import utils.TimeUtils;
import views.formdata.InfoSessionFormData;

import exceptions.DataAccessException;

/**
 * This class contains various static methods to manage InfoSessions, serving as a link between
 * the view and the database. See the individual methods' documentation for more information.
 *
 * @author  Bart Middag
 * @see     objects.InfoSession
 * @see     views.formdata.InfoSessionFormData
 */
public class InfoSessionsModel {
    /**
     * Uses the DAO to get the InfoSession that corresponds with the specified ID.
     *
     * @param  id   the ID of the InfoSession
     * @return      the InfoSession with the specified ID
     * @see         objects.InfoSession
     */
    public static InfoSession getInfoSessionById(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();
            return infoSessionDAO.getInfoSession(id);
        }
    }


    /**
     *
     * @return list of all InfoSessions
     */
    public static List<InfoSession> getAllInfosessions() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();
            return infoSessionDAO.getInfoSessions();
        }
    }


    /**
     *
     * @return list of all inscriptions for the user corresponding the id
     */
    public static List<Inscription> getInscriptionsByInfosession(InfoSession infoSession) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            return inscriptionDAO.getInscriptionsByInfoSession(infoSession);
        }
    }


    /**
     * Creates, modifies or deletes the InfoSession described by the InfoSessionFormData object
     * that contains the data the administrator entered on the website.
     *
     * @param   formData    the Play form data of the InfoSession you want to manage
     * @param   user        the user whose inscriptions you want to manage
     * @return              the InfoSession as an InfoSession object, or null if it has been deleted
     * @see                 objects.InfoSession
     */
    public static InfoSession manageInfoSession(InfoSessionFormData formData, User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            PlaceDAO placeDAO = dac.getPlaceDAO();
            AddressDAO addressDAO = dac.getAddressDAO();
            RoleDAO roleDAO = dac.getRoleDAO();
            UserDAO userDAO = dac.getUserDAO();

            if(formData.remove.equals("removeSession") && (!formData.id.equals(""))) {
                InfoSession session = getInfoSessionById(Integer.parseInt(formData.id));
                removeInfoSession(dac, session);
                dac.commit();
                return null;
            } else {
                String bus = "";
                if(formData.addressBus != null) bus = formData.addressBus;
                // check for existence of address in db, otherwise make new one
                Place place = getOrAddPlace(placeDAO, formData.addressPlace,formData.addressZip);
                int number = Integer.parseInt(formData.addressNumber);
                Address address = getOrAddAddress(addressDAO, place, formData.addressStreet, number, bus);
                Long date;
                try {
                    date = TimeUtils.stringToLong("dd/MM/yyyy HH:mm",formData.date + " " + formData.time);
                } catch(ParseException e) {
                    throw new DataAccessException("Datum werd niet geparsed. Dit mag niet gebeuren!");
                }
                 /*Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(formData.dateYear), Integer.parseInt(formData.dateMonth) - 1, Integer.parseInt(formData.dateDay), Integer.parseInt(formData.dateHour), Integer.parseInt(formData.dateMinute));
                long date = cal.getTime().getTime(); */
                boolean owners = Boolean.parseBoolean(formData.owners);
                int places = Integer.parseInt(formData.places);
                InfoSession session;
                if(!formData.id.equals("")) {
                    session = getInfoSessionById(Integer.parseInt(formData.id));
                    session.setAddress(address);
                    session.setDate(date);
                    session.setOwners(owners);
                    session.setPlaces(places);
                    infoSessionDAO.updateInfoSession(session);
                    for(Inscription inscription: inscriptionDAO.getInscriptionsByInfoSession(session)) {
                        Mailer.sendInfosessionChangeNotification(inscription.getUser(),session);
                        inscription.setPresent(false);
                        inscriptionDAO.updateInscription(inscription);
                    }
                    for(String userId: formData.inscriptions) {
                        Inscription inscription = inscriptionDAO.getInscription(session.getId(),Integer.parseInt(userId));
                        if(inscription != null) {
                            inscription.setPresent(true);
                            inscriptionDAO.updateInscription(inscription);
                            String roleName = inscription.getUser().getRole().getName();
                            if(roleName.contains("new_")) {
                                // Upgrade member
                                roleName = roleName.replace("new_","almost_"); // Remove "new_"
                                Role role = roleDAO.getRoleByName(roleName);
                                User upgradedUser = inscription.getUser();
                                upgradedUser.setRole(role);
                                userDAO.updateUser(upgradedUser);
                            }
                        }
                    }

                } else {
                    session = infoSessionDAO.createInfoSession(address,date,owners,places);
                }
                if(formData.inscribed.equals("editInscription")) {
                    Inscription inscription = inscriptionDAO.getInscription(session.getId(), user.getId());
                    if(inscription != null) {
                        inscriptionDAO.deleteInscription(inscription);
                    } else {
                        inscriptionDAO.createInscription(session, user);
                        Mailer.sendInfosessionConfirmation(user,session);
                    }
                }
                dac.commit();
                return session;
            }
        }
    }

    /**
     * Changes the inscriptions of a User for an InfoSession based on an InfoSessionFormData object
     * that contains the data the user entered on the website.
     *
     * @param   formData    the Play form data of the InfoSession you want to manage
     * @param   user        the user whose inscriptions you want to manage
     * @return              the InfoSession as an InfoSession object
     * @see                 objects.InfoSession
     */
    public static InfoSession manageInscription(InfoSessionFormData formData, User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();

            InfoSession session = null;
            if(!formData.inscribed.equals("")) {
                if(!formData.id.equals("")) session = getInfoSessionById(Integer.parseInt(formData.id));
                if((user != null) && (session != null)) {
                    Inscription inscription = inscriptionDAO.getInscription(session.getId(), user.getId());
                    if(inscription != null) {
                        inscriptionDAO.deleteInscription(inscription);
                    } else {
                        if(!(user.getRole().equals("admin"))) {
                            List<Inscription> inscriptionsList = inscriptionDAO.getInscriptionsByUser(user);
                            for(Inscription i: inscriptionsList) {
                                inscriptionDAO.deleteInscription(i);
                            }
                        }
                        inscriptionDAO.createInscription(session, user);
                        Mailer.sendInfosessionConfirmation(user,session);
                    }
                    dac.commit();
                    return session;
                }
            }
            return null;
        }
    }

    /**
     * Get the Inscription object that exists for a specified User and InfoSession.
     * If it doesn't exist, this method returns null.
     *
     * @param   user    the User that is inscribed to an infosession
     * @param   session the InfoSession that the user is inscribed to
     * @return          the Inscription object, if an inscription exists, else null
     * @see             objects.Inscription
     */
    public static Inscription getUserInscription(User user, InfoSession session) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            return inscriptionDAO.getInscription(session.getId(), user.getId());
        }
    }

    /**
     * Get the amount of inscriptions for an InfoSession. Useful for calculating the amount of free places.
     *
     * @param   session the InfoSession you want to know the amount of inscriptions for
     * @return          the amount of inscriptions for this InfoSession
     */
    public static int getInscriptionCount(InfoSession session) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            return inscriptionDAO.getInscriptionsByInfoSession(session).size();
        }
    }

    /**
     * Get the amount of inscriptions for a User. Useful for checking if the user should be able to access the infoSessions page.
     *
     * @param   user    the User you want to know the amount of inscriptions for
     * @return          the amount of inscriptions for this User
     */
    public static int getInscriptionCount(User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            return inscriptionDAO.getInscriptionsByUser(user).size();
        }
    }


    /**
     * Get the users who have subscribed to a specific InfoSession.
     * @param session   the InfoSession you want to know about
     * @return          the list of users who have subscribed to this infoSession
     * @throws DataAccessException
     */
    public static List<User> getUsersForInfoSession(InfoSession session) throws DataAccessException{
        List<User> userList = new ArrayList<>();
        for(Inscription inscription: getInscriptionsByInfosession(session)){
            userList.add(inscription.getUser());
        }
        return userList;
    }

    /**
     * Convert all InfoSessions data to Play form data, useful for displaying the information in a form
     * on the website. Can also be used to just display the informations in String format. Uses the
     * {@link #makeInfoSessionFormData(InscriptionDAO, InfoSession, User) makeInfoSessionFormData} method
     * to create the InfoSessionFormData objects.
     *
     * @param   user    the user viewing the form, necessary for showing inscriptions
     * @return          a list of InfoSessions represented as data for a Play form
     * @see             views.formdata.InfoSessionFormData
     */
    public static List<InfoSessionFormData> getInfoSessionFormDataList(User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();

            List<InfoSessionFormData> formDataList = new ArrayList<InfoSessionFormData>();
            List<InfoSession> infoSessions;
            String roleName = user.getRole().getName();
            Filter<InfoSession> filter = infoSessionDAO.getFilter();
            if(roleName.equals("new_user")) {
                filter.fieldEquals(InfoSession.Field.OWNERS,Boolean.FALSE);
                // You can only register for infosessions more than one day away.
                filter.fieldGreaterThan(InfoSession.Field.DATE, TimeUtils.getDateOffset(new java.util.Date().getTime(),0,0,1));
                infoSessions = infoSessionDAO.getByFilter(filter);
            } else if(roleName.equals("new_owner")) {
                filter.fieldEquals(InfoSession.Field.OWNERS,Boolean.TRUE);
                // You can only register for infosessions more than one day away.
                filter.fieldGreaterThan(InfoSession.Field.DATE, TimeUtils.getDateOffset(new java.util.Date().getTime(),0,0,1));
                infoSessions = infoSessionDAO.getByFilter(filter);
            } else infoSessions = infoSessionDAO.getInfoSessions();
            for (InfoSession session : infoSessions) {
                formDataList.add(makeInfoSessionFormData(inscriptionDAO, session, user));
            }
            return formDataList;
        }
    }

    /**
     * Convert InfoSessions data to Play form data, but only for the InfoSessions the user is inscribed for.
     * Can also be used to just display the informations in String format. Uses the
     * {@link #makeInfoSessionFormData(InscriptionDAO, InfoSession, User) makeInfoSessionFormData} method
     * to create the InfoSessionFormData objects.
     *
     * @param   user    the user whose inscriptions you want to show
     * @return          a list of InfoSessions represented as data for a Play form
     * @see             views.formdata.InfoSessionFormData
     */
    public static List<InfoSessionFormData> getInscriptionFormDataList(User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            List<Inscription> inscriptions = inscriptionDAO.getInscriptionsByUser(user);
            List<InfoSessionFormData> formDataList = new ArrayList<InfoSessionFormData>();
            for (Inscription inscription: inscriptions) {
                formDataList.add(makeInfoSessionFormData(inscriptionDAO, inscription.getSession(), user));
            }
            return formDataList;
        }
    }

    /**
     * Remove an InfoSession from the database, along with all of its inscriptions.
     *
     * @param   dac     the Data Access Context used to get the DAO objects
     * @param   session the InfoSession you want to remove
     */
    private static void removeInfoSession(DataAccessContext dac, InfoSession session) throws DataAccessException {
        InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
        InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();

        List<Inscription> inscriptionsList = inscriptionDAO.getInscriptionsByInfoSession(session);
        for (Inscription i: inscriptionsList) {
            inscriptionDAO.deleteInscription(i);
        }

        infoSessionDAO.deleteInfoSession(session);
    }

    /**
     * Convert an InfoSession object to Play form data, useful for displaying the information in a form
     * on the website. Can also be used to just display the information in String format.
     *
     * @param   inscriptionDAO  the DAO object used for connection with the inscription table of the database
     * @param   session         the InfoSession object you want to convert to form data
     * @param   user            the user viewing the form, necessary for showing inscriptions
     * @return                  the InfoSession represented as data for a Play form, conveniently in String format.
     * @see                     views.formdata.InfoSessionFormData
     */
    private static InfoSessionFormData makeInfoSessionFormData(InscriptionDAO inscriptionDAO,
                                                               InfoSession session,
                                                               User user) throws DataAccessException {
        // To minimize DAO queries, we get the inscriptions only once
        boolean inscribed = false;
        List<Inscription> inscriptions = inscriptionDAO.getInscriptionsByInfoSession(session);
        for(Inscription inscription: inscriptions) {
            if(user.equals(inscription.getUser())) {
                inscribed = true;
                break;
            }
        }
        return new InfoSessionFormData(session.getId(),
                session.getAddress(),
                session.getDate(),
                session.getPlaces(),
                session.getOwners(),
                inscriptions,
                inscribed,
                new java.util.Date(session.getDate()).before(new java.util.Date(TimeUtils.getDateOffset(new java.util.Date().getTime(),0,0,1))));
    }

    /**
     * Uses the DAO to get the Place that matches the arguments.
     * If a Place with these arguments doesn't exist, creates one.
     *
     * @param   placeDAO    the DAO object used for connection with the place table of the database
     * @param   placeName   the name of this Place
     * @param   placeZip    the zip code of this Place
     * @return              the Place that matches with the arguments, or a new one
     * @see                 objects.Place
     */
    public static Place getOrAddPlace(PlaceDAO placeDAO, String placeName, String placeZip) throws DataAccessException {
        Filter<Place> filter = placeDAO.getFilter();
        filter.fieldEquals(Place.Field.ZIP,placeZip);
        filter.fieldEquals(Place.Field.NAME,placeName);
        List<Place> places = placeDAO.getByFilter(filter);
        if(!places.isEmpty()) return places.get(0);
        Place place = placeDAO.createPlace(placeName,placeZip);
        return place;
    }

    /**
     * Uses the DAO to get the Address that matches the arguments.
     * If an address with these arguments doesn't exist, creates one.
     *
     * @param   addressDAO  the DAO object used for connection with the address table of the database
     * @param   p           the Place object that this address uses. Use the
     *                      {@link #getOrAddPlace(PlaceDAO, String, String) getOrAddPlace} method to obtain this.
     * @param   street      the street name
     * @param   number      the house number
     * @param   bus         the bus (empty string if there is no bus)
     * @return              the Address that matches with the arguments, or a new one
     * @see                 objects.Address
     */
    public static Address getOrAddAddress(AddressDAO addressDAO, Place p, String street, Integer number, String bus) throws DataAccessException {
        Filter<Address> filter = addressDAO.getFilter();
        filter.fieldEquals(Address.Field.PLACE,p.getId());
        filter.fieldEquals(Address.Field.STREET,street);
        filter.fieldEquals(Address.Field.NUMBER,number);
        filter.fieldEquals(Address.Field.BUS,bus.equals("") ? null : bus);
        List<Address> addresses = addressDAO.getByFilter(filter);
        if(!addresses.isEmpty()) return addresses.get(0);
        Address address = addressDAO.createAddress(p,street,number,bus.equals("") ? null : bus);
        return address;
    }
}
