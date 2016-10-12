
package controllers;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import models.UserModel;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;
import views.html.accessfailed;
import play.Logger;

public class MyDeadboltHandler extends AbstractDeadboltHandler {
    public F.Promise<SimpleResult> beforeAuthCheck(Http.Context context) {
        // Returning null means that everything is OK.
        return F.Promise.pure(null);
    }

    public Subject getSubject(Http.Context context) {
        return UserModel.getCurrentUserSubject();
    }

    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return new MyDynamicResourceHandler();
    }

    @Override
    public F.Promise<SimpleResult> onAuthFailure(Http.Context context, String content) {
        return F.Promise.<SimpleResult>pure(redirect(routes.Login.login()));
    }
}
