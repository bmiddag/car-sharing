import exceptions.DataAccessException;
import executors.ActionExecutor;
import interfaces.ActionDAO;
import interfaces.DataAccessContext;
import jdbc.JDBCDataAccessProvider;
import objects.Action;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;

import java.util.List;

import static play.mvc.Results.internalServerError;
import static play.mvc.Results.notFound;

/**
 * This class defines global callback methods for certain events.
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */
public class Global extends GlobalSettings {
    /**
     * This method is called when the application is loaded.
     * Two things are done:
     * (1) The executors are started
     * (2) The DataAccessProvider is initialized with the right credentials
     */
    @Override
    public void onStart(Application application) {
        // Initialize the DataAccessProvider
        String dbname = play.Play.application().configuration().getString("dbname");
        String dbuser = play.Play.application().configuration().getString("dbuser");
        String dbpasswd = play.Play.application().configuration().getString("dbpasswd");
        JDBCDataAccessProvider.getDataAccessProvider().init(dbname, dbuser, dbpasswd);

        // Get the executors from the database
        List<Action> actions = null;
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ActionDAO actionDAO = context.getActionDAO();
            actions = actionDAO.getActions();
        } catch (DataAccessException | NumberFormatException e) {
            Logger.error("", e);
        }

        // Schedule them
        if (actions != null) {
            for (Action action : actions) {
                ActionExecutor.schedule(action);
            }
        }
    }

    /**
     * This method is called when a requested url isn't defined in conf/routes.
     *
     * @param request The request whose url wasn't defined
     * @return The 404 Not Found error page
     */
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
                views.html.notFoundPage.render()
        ));
    }

    /**
     * This method is called when a controllers returns a bad request. This is
     * handled by onError by throwing a RuntimeException.
     *
     * @param request The request that went wrong
     * @param error Detailed information of what went wrong
     * @return Doesn't apply with this implementation
     */
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
        throw new RuntimeException(error);
    }

    /**
     * This method is called when an action throws an exception.
     * We just render an error page displaying debug information.
     *
     * @return The error page in a future result
     */
    @Override
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
        return Promise.<SimpleResult>pure(internalServerError(
                views.html.error.render(t)
        ));
    }
}
