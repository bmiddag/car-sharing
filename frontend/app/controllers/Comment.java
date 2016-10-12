package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import models.NotificationModel;
import models.UserModel;
import objects.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.TimeUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by wouter on 13/05/14.
 */
public class Comment extends Controller {

    @Restrict({@Group("admin"),@Group("owner")})
    public static Result newComment() throws DataAccessException {
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String damageId = postData.get("damage")[0];
        String content = postData.get("content")[0];

        int parsedDamageId = 0;
        try {
            parsedDamageId = Integer.parseInt(damageId);
        } catch(NumberFormatException e){
            Logger.error("", e);
            return badRequest("Het Damage ID is niet geldig.");
        }

        try {
            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            DamageDAO damageDAO = context.getDamageDAO();
            objects.Damage damage = damageDAO.getDamage(parsedDamageId);

            CarDAO carDAO = context.getCarDAO();
            objects.Car car = carDAO.getCar(damage.getCar());

            objects.User user = UserModel.getCurrentUser();

            CommentDAO commentDAO = context.getCommentDAO();
            commentDAO.createComment(user, damage, content);

            // Send notification to the receiver
            User receiver = UserModel.getUserById(damage.getUser());
            String message =
                    "De eigenaar van de auto " + car.getName()
                            + " heeft een commentaar geplaatst op het schadegeval";
            if(damage.getOccurred() != null){
                message += " van " + TimeUtils.getHumanReadableDate(damage.getOccurred());
            }
            message += ".";

            String subject = "Nieuw commentaar over schadegeval";
            NotificationModel.createNotification(receiver, subject, message);

            context.commit();
        }
        catch (DataAccessException e){
            flash("error", "Er is een fout opgetreden, het aanmaken van deze comment is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.CarManagement.carManagementDamage(parsedDamageId));
    }
}
