package executors;

import controllers.Email;
import exceptions.DataAccessException;
import play.Logger;

/**
 * Created by Robin on 4/05/14.
 */
public class QuarterlyCostsExecutor extends ActionExecutor {

    @Override
    public void run(){
        Logger.info("Starting executor QuarterlyCosts");
        try {
            new Email().checkForBills();
        } catch (DataAccessException e) {
            // TODO: Better exception handling?
            e.printStackTrace();
        }
    }
}
