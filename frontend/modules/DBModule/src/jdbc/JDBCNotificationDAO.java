/*        ______ _____  _____            _   _                    *
 *       |  ____|  __ \|  __ \     /\   | \ | |Verantwoordelijken:*
 *       | |__  | |  | | |__) |   /  \  |  \| |                   *
 *       |  __| | |  | |  _  /   / /\ \ | . ` | Laurens De Graeve *
 *       | |____| |__| | | \ \  / ____ \| |\  |  & Wouter Termont *
 *       |______|_____/|_|  \_\/_/    \_\_|_\_|    _              *
 *       |  __ \|  _ \                    | |     | |             *
 *       | |  | | |_) |_ __ ___   ___   __| |_   _| | ___         *
 *       | |  | |  _ <| '_ ` _ \ / _ \ / _` | | | | |/ _ \        *
 *       | |__| | |_) | | | | | | (_) | (_| | |_| | |  __/        *
 *       |_____/|____/|_| |_| |_|\___/ \__,_|\__,_|_|\___|        *
 *                                                                *
 *****************************************************************/
package jdbc;

import exceptions.DataAccessException;
import interfaces.NotificationDAO;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Notification;
import objects.User;

class JDBCNotificationDAO extends JDBCDataAccessObject<Notification> implements NotificationDAO {

    JDBCNotificationDAO(JDBCDataAccessContext dac) {
        super(dac);
    }

    @Override
    public JDBCFilter<Notification> getFilter() {
        return new JDBCFilter<Notification>() {};
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Notification.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Notification.Field.TIME};
    }
    
    @Override
    protected DataField getKey() {
        return Notification.Field.ID;
    }

    @Override
    public Notification createNotification(User user, String subject, String content) throws DataAccessException {

        return new Notification(create(user, subject, content, false), user.getId(), subject, content, false, new Date().getTime());

    }

    @Override
    public void updateNotification(Notification message) throws DataAccessException {

        update(message);

    }

    @Override
    public void deleteNotification(Notification message) throws DataAccessException {
        deleteByID(message.getId());
    }
    
    @Override
    public Notification getNotification(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
    @Override
    public List<Notification> getNotificationsByUser(Integer userid) throws DataAccessException {
        JDBCFilter<Notification> filter = getFilter();
        filter.fieldEquals(Notification.Field.USER, userid);
        return getByFilter(filter, Notification.Field.TIME, false);
    }

    @Override
    public List<Notification> getUnseenNotificationsByUser(Integer userid) throws DataAccessException {
        JDBCFilter<Notification> filter = getFilter();
        filter.fieldEquals(Notification.Field.SEEN, false);
        filter.fieldEquals(Notification.Field.USER, userid);
        return getByFilter(filter);
    }

    @Override
    public List<Notification> getNotifications() throws DataAccessException {
        return getAll();
    }

    @Override
    public void deleteRead() throws DataAccessException {
        JDBCFilter<Notification> filter = getFilter();
        filter.fieldEquals(Notification.Field.SEEN, true);
        Timestamp sevenDaysAgo = new Timestamp(System.currentTimeMillis() - 7*3600000);
        filter.fieldLessThan(Notification.Field.TIME, sevenDaysAgo);
        deleteByFilter(filter);
    }
    
}
