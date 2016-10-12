package controllers;

import exceptions.DataAccessException;
import models.UserModel;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.userpage;

/**
 * Class to get user pages
 * @author Gilles
 */
public class UserController extends Controller {
    
    public static Result getUser(int id) throws DataAccessException{
        return ok(userpage.render(UserModel.getUserById(id)));
    }
}
