package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import exceptions.DataAccessException;
import models.NotificationModel;
import models.UserModel;
import objects.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.notifications;

import java.util.Map;

/**
 * Created by Gilles on 25/04/14.
 */
public class Notifications extends Controller {
    @SubjectPresent
    public static Result notifications() throws DataAccessException {
        User u = UserModel.getCurrentUser();
        return ok(notifications.render(NotificationModel.getNotifications(u)));
    }
    @SubjectPresent
    public static Result read(int id) throws DataAccessException {
        NotificationModel.readMessage(id);
        return notifications();
    }
}
