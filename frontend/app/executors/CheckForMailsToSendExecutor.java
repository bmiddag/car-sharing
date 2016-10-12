package executors;

import controllers.Email;
import exceptions.DataAccessException;
import play.Logger;

/**
 * Created by Robin on 4/05/14.
 */
public class CheckForMailsToSendExecutor extends ActionExecutor {

    @Override
    public void run()
    {
        Logger.info("Starting executor CheckForMailsToSend");
        try {
            new Email().checkForNotifications();
            new Email().checkForInfosessions();
        } catch (DataAccessException e) {
            // TODO: Better exception handling?
            e.printStackTrace();
        }
    }


}
