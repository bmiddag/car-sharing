package executors;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.NotificationDAO;
import jdbc.JDBCDataAccessProvider;
import play.Logger;

/**
 * Created by Wouter Pinnoo on 17/02/14.
 */
public class DeleteReadNotificationsExecutor extends ActionExecutor {

    @Override
    public void run() {
        Logger.info("Starting executor DeleteReadNotifications");
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            NotificationDAO notifDAO = dac.getNotificationDAO();
            notifDAO.deleteRead();
            dac.commit();
        } catch (DataAccessException e) {
            // TODO: Better exception handling?
            e.printStackTrace();
        }
    }
}
