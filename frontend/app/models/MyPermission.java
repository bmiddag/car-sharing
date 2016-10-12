package models;

import be.objectify.deadbolt.core.models.Permission;

/**
 * Created by stevendeblieck on 27/03/14.
 */
public class MyPermission implements Permission {

    public String value;

    public MyPermission(String value) {
        this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
