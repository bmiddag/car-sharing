package interfaces;

import exceptions.DataAccessException;
import java.util.List;
import objects.Notification;
import objects.User;

/**
 * This DAO Regulates access to the notifications in the database.
 */
public interface NotificationDAO extends DataAccessObject<Notification> {

    /**
     * Creates a notification object in de database.
     *
     * @param user the recipient of the new notification
     * @param subject the subject of the new notification
     * @param content the content of the new notification
     * @return the newly created Notification object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Notification createNotification(User user, String subject, String content) throws DataAccessException;

    /**
     * Returns the notification with the given id.
     *
     * @param id the id of the notification to find in the db
     * @return a Notification object representing the Notification in the db;
     * null if not found
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Notification getNotification(Integer id) throws DataAccessException;

    /**
     * Returns all notifications.
     *
     * @return A list of Notification objects
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Notification> getNotifications() throws DataAccessException;

    /**
     * Returns all notifications intended for the user with the given user id.
     *
     * @param user the user id for whom the notifications are intended
     * @return A list of Notification objects
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Notification> getNotificationsByUser(Integer user) throws DataAccessException;

    /**
     * Returns all unseen notifications intended for the user with the given
     * user id.
     *
     * @param user the user id for whom the notifications are intended
     * @return A list of Notification objects
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Notification> getUnseenNotificationsByUser(Integer userid) throws DataAccessException;

    /**
     * Updates the given notification in the db.
     *
     * @param message an adapted Notification object to update in the db
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateNotification(Notification message) throws DataAccessException;

    /**
     * Deletes all read notifications that are older than seven days.
     *
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteRead() throws DataAccessException;

    /**
     * Deletes the given notification.
     *
     * @param message the Notification object to delete from the db
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteNotification(Notification message) throws DataAccessException;

    /**
     * Returns a notification filter.
     *
     * @return the filter
     * @see    interfaces.Filter
     */
    public Filter<Notification> getFilter();

    /**
     * An enum representing the fields on which one can filter.
     *
     * @see interfaces.Filter
     */
    public enum Field {
        ID, USER, SUBJECT, CONTENT, SEEN, TIME;
    }
}
