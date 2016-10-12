package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import exceptions.DataAccessException;
import interfaces.CarDAO;
import interfaces.DataAccessContext;
import interfaces.RideDAO;
import jdbc.JDBCDataAccessProvider;
import models.ReserveModel;
import objects.Reservation;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wouter Pinnoo on 23/03/14.
 */
public class Car extends Controller {
    @Restrict({@Group("admin"),@Group("owner")})
    public static Result search() throws DataAccessException {
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String query = postData.get("query")[0];

        List<objects.Car> cars = new ArrayList<objects.Car>();
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            CarDAO dao = context.getCarDAO();
            cars = dao.searchCar(query);
            context.commit();
        } catch (DataAccessException | NumberFormatException e) {
            flash("error", "Er is een fout opgetreden, het opzoeken van auto's is niet gelukt.");
            Logger.error("", e);
        }
        return ok(views.html.cars.render(cars));
    }
}
