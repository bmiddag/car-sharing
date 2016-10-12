
package controllers;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import play.mvc.Http;


public abstract class AbstractDynamicResourceHandler implements DynamicResourceHandler
{
    public boolean checkPermission(String permissionValue, DeadboltHandler deadboltHandler, Http.Context ctx)
    {
        return false;
    }

    public boolean isAllowed(String name, String meta, DeadboltHandler deadboltHandler, Http.Context ctx)
    {
        return false;
    }
}
