package controllers;

import exceptions.DataAccessException;
import models.CarManagementModel;
import models.InfoSessionsModel;
import models.UserModel;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Controller;
import controllers.PDF;
import views.html.reportcar;
import views.html.reportuser;
import views.html.reportinfosession;



/**
 * Created by Robin on 27/04/14.
 */
public class Report extends Controller {


    /**
     *
     * @param id user id
     * @return a report in pdf with all the data of the user corresponding the id
     */

    public static Result getReportUser(int id) throws DataAccessException{

        return PDF.ok(reportuser.render(UserModel.getUserById(id)));

    }

    /**
     *
     * @param id car id
     * @return a report in pdf with all the data of the car corresponding the id
     */

    public static Result getReportCar(int id) throws DataAccessException {

        return PDF.ok(reportcar.render(CarManagementModel.getCarByUserId(id)));

    }

    /**
     *
     * @param id infosession id
     * @return a report in pdf with all the data of the infosession corresponding the id
     */
    public static Result getReportInfosession(int id) throws DataAccessException {

        return PDF.ok(reportinfosession.render(InfoSessionsModel.getInfoSessionById(id)));
    }






}
