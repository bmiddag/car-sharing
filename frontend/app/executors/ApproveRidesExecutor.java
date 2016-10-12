package executors;

import exceptions.DataAccessException;
import controllers.Email;


/**
 * Created by Robin on 18/05/14.
 */
public class ApproveRidesExecutor extends ActionExecutor {

    @Override
    public void run() {

        try {
            new Email().sendApproveRideNotification();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


}
