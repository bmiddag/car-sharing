package controllers;

import exceptions.DataAccessException;
import models.*;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import objects.*;
import play.Routes;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.formdata.InfoSessionFormData;
import views.html.*;
import views.html.index;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {
    //@Restrict({@Group("!blocked")})

    /**
     * Get the needed information for the dashboards and generate the front page
     * @return the front page
     */
    public static Result index(String tab) throws DataAccessException {
        User user = UserModel.getCurrentUser();
        if(user == null) {
            return ok(index.render(tab, null, null, null, null, null, null, null, null));
        } else {
            List<InfoSessionFormData> formDataList = InfoSessionsModel.getInscriptionFormDataList(user);
            List<Form<InfoSessionFormData>> formList = new ArrayList<Form<InfoSessionFormData>>();
            for(InfoSessionFormData formData: formDataList) {
                formList.add(form(InfoSessionFormData.class).fill(formData));
            }
            if(CarManagementModel.getCarByUserId(user.getId()) == null){
                return ok(index.render(tab, formList, DashboardModel.getPendingReservations(user),
                        DashboardModel.getPendingRideInfo(user), DashboardModel.getPendingDamages(user),
                        DashboardModel.getStats(user), NotificationModel.getUnreadNotifications(),
                        null, null));
            } else {
                return ok(index.render(tab, formList, DashboardModel.getPendingReservations(user),
                        DashboardModel.getPendingRideInfo(user), DashboardModel.getPendingDamages(user),
                        DashboardModel.getStats(user), NotificationModel.getUnreadNotifications(),
                        DashboardModel.getPendingReservationsForCar(CarManagementModel.getCarByUserId(user.getId())),
                        DashboardModel.getPendingRideInfoByCar(CarManagementModel.getCarByUserId(user.getId()))));
            }
        }
    }

    public static Result contact(){
        return ok(contact.render());
    }

    public static Result map(){
        return ok(map.render());
    }
    /**
     * Defines the javascripts routes used in the reserve.scala.html and admin.scale.html view.
     *
     * @return JavaScript code which defines a router
     */
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("javascriptRoutes",
                routes.javascript.Notifications.read(),
                routes.javascript.Assets.at(),
                routes.javascript.Reserve.index(),
                routes.javascript.Reserve.getReservations(),
                routes.javascript.Reserve.getAvailableCars(),
                routes.javascript.Admin.getUserPermissions(),
                routes.javascript.CarManagement.carManagementDamage(),
                routes.javascript.Reserve.damage()));
    }
}
