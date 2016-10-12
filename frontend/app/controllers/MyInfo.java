package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.ning.http.client.FilePart;
import exceptions.DataAccessException;
import models.MyInfoModel;
import models.UserModel;
import objects.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.myinfo;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by Gilles on 17/04/14.
 */
public class MyInfo extends Controller {
    public static Result index() throws DataAccessException{
        User user = UserModel.getCurrentUser();
        return ok(myinfo.render(user));
    }
    @SubjectPresent
    public static Result changePassword() throws DataAccessException{
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String oldPassword = map.get("oldPassword")[0];
        String newPassword1 = map.get("newPassword1")[0];
        String newPassword2 = map.get("newPassword2")[0];
        int exitCode = MyInfoModel.changePassword(oldPassword, newPassword1, newPassword2);
        if(exitCode > 0) flash("error", "Het wachtwoord is niet veranderd. Ben je zeker dat jouw oude wachtwoord klopt en dat de twee nieuwe wachtwoorden aan elkaar gelijk zijn?");
        return index();
    }
    @SubjectPresent
    public static Result changeEmail() throws DataAccessException{
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        // String oldEmail = map.get("oldEmail")[0];
        String newEmail1 = map.get("newEmail1")[0];
        String newEmail2 = map.get("newEmail2")[0];
        int exitCode = MyInfoModel.changeEmail(newEmail1, newEmail2); //MyInfoModel.changeEmail(oldEmail, newEmail1, newEmail2);
        if(exitCode > 0) flash("error", "Het emailadres is niet veranderd. Ben je zeker dat jouw oude email klopt en dat de twee nieuwe email adressen aan elkaar gelijk zijn?");
        return index();
    }

    //TODO: format
    @SubjectPresent
    public static Result changePhone() throws DataAccessException{
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String newPhone = map.get("newPhone")[0];
        int exitCode = MyInfoModel.changePhone(newPhone);
        if(exitCode > 0) flash("error", "Het nummer is niet veranderd");
        return index();
    }
    @SubjectPresent
    public static Result changeAddress() throws DataAccessException{
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String newStreet = map.get("newStreet")[0];
        String newNumber = map.get("newNumber")[0];
        String newBus = map.get("newBus")[0];
        String newCity = map.get("newCity")[0];
        String newPCode = map.get("newPCode")[0];
        int exitCode = MyInfoModel.changeAddress(newStreet, newNumber, newBus, newCity, newPCode);
        if(exitCode > 0) flash("error", "Het adres is niet veranderd");
        return index();
    }
    @SubjectPresent
    public static Result changeDomicile() throws DataAccessException{
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String newStreet = map.get("newStreet")[0];
        String newNumber = map.get("newNumber")[0];
        String newBus = map.get("newBus")[0];
        String newCity = map.get("newCity")[0];
        String newPCode = map.get("newPCode")[0];
        int exitCode = MyInfoModel.changeDomicile(newStreet, newNumber, newBus, newCity, newPCode);
        if(exitCode > 0) flash("error", "Het domicilie-adres is niet veranderd");
        return index();
    }
    @SubjectPresent
    public static Result changeProfilePicture() throws FileNotFoundException, DataAccessException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart = body.getFile("pic");
        MyInfoModel.changeProfilePicture(filePart);

        return index();
    }
}
