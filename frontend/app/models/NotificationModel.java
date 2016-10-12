package models;

import interfaces.DataAccessContext;
import interfaces.NotificationDAO;

import java.util.List;
import java.util.ArrayList;

import jdbc.JDBCDataAccessProvider;

import objects.Notification;
import objects.User;

import play.Logger;

import exceptions.DataAccessException;

/**
 * Model for the notifications.
 *
 * @author Gilles Vandewiele
 * @author Stijn Seghers
 */
public class NotificationModel {

    public static void createNotification(User user, String subject, String text) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            NotificationDAO dao = context.getNotificationDAO();
            dao.createNotification(user, subject, text);
        }
    }

    public static List<Notification> getNotifications(User u) {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            NotificationDAO notificationDAO = dac.getNotificationDAO();
            List<Notification> result = notificationDAO.getNotificationsByUser(u.getId());
            if(result == null) return new ArrayList<Notification>();
            return result;
        } catch (DataAccessException e) {
            Logger.error("", e);
        } catch(NullPointerException e){
            return new ArrayList<Notification>();
        }
        return new ArrayList<Notification>();
    }

    public static List<Notification> getUnreadNotifications(User u) throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            NotificationDAO notificationDAO = dac.getNotificationDAO();
            List<Notification> result = notificationDAO.getUnseenNotificationsByUser(u.getId());
            return result;
        } catch (NullPointerException e) {
            Logger.debug(e + "");
        }
        return null;
    }

    public static List<Notification> getUnreadNotifications() throws DataAccessException{
        return getUnreadNotifications(UserModel.getCurrentUser());
    }

    public static int getNumberOfUnreadNotifications(User u) throws DataAccessException{
        if(getUnreadNotifications(u) != null) return getUnreadNotifications(u).size();
        else return 0;
    }

    public static int getNumberOfUnreadNotifications() throws DataAccessException{
        return getNumberOfUnreadNotifications(UserModel.getCurrentUser());
    }

    /**
     * read a message
     * @param msg the id of the message to read
     */
    public static void readMessage(final int msg) throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            final NotificationDAO notificationDAO = dac.getNotificationDAO();
            final Notification n = notificationDAO.getNotification(msg);
            if (n != null && !n.isSeen()) {
                n.setSeen(true);
                notificationDAO.updateNotification(n);
            }
        }
    }
}
