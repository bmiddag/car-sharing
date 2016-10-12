package executors;

import org.joda.time.DateTime;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import objects.Action;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * Created by Wouter Pinnoo on 26/04/14.
 */
public abstract class ActionExecutor implements Runnable {

    private static HashMap<String, ActionExecutor> executors = new HashMap<>();

    static {
        executors.put("DeleteReadNotifications", new DeleteReadNotificationsExecutor());
        executors.put("CheckForMailsToSend", new CheckForMailsToSendExecutor());
        executors.put("QuarterlyCosts", new QuarterlyCostsExecutor());
        executors.put("ApproveRidesExecutor",new ApproveRidesExecutor());
    }

    public static void schedule(Action a){
        if(executors.containsKey(a.getName())) {
            Logger.info("Scheduling action \"" + a.getName() + "\" with start=" + a.getStart() + " and period=" + a.getPeriod());
            long start = a.getStart()-System.currentTimeMillis();
            while(start < 0) {
                start += a.getPeriod();
            }

            Akka.system().scheduler().schedule(
                    Duration.create(start, TimeUnit.MILLISECONDS),
                    Duration.create(a.getPeriod(), TimeUnit.MILLISECONDS),
                    executors.get(a.getName()),
                    Akka.system().dispatcher()
            );
        } else Logger.error("This executor description isn't recognized. (" + a.getName() + ")");
    }
}
