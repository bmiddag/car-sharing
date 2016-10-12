package models;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.InfoSessionDAO;
import jdbc.JDBCDataAccessProvider;
import objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import interfaces.UserPermissionDAO;
import objects.UserPermission;
import play.Logger;

/**
 * Created by stevendeblieck on 27/03/14.
 */
public class UserSubject implements Subject {
    private User user;

    public UserSubject(User user){
        this.user=user;
    }

    public User getUser(){
        return user;
    }

    @Override
    public List<? extends Role> getRoles() {
        return Arrays.asList(new MyRole(user.getRole().getId(),user.getRole().getName()));
    }

    @Override
    public List<? extends Permission> getPermissions() {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserPermissionDAO userPermissionDAO = dac.getUserPermissionDAO();
            List<UserPermission> list = userPermissionDAO.getUserPermissionByUser(user);
            List<MyPermission> permissions= new ArrayList<>();
            for (int i=0; i< list.size();i++){
                //System.out.println(list.get(i).getPermission().getName());
                permissions.add(new MyPermission(list.get(i).getPermission().getName()));
            }


            return permissions;
        } catch (DataAccessException e) {
            Logger.error("", e);
        }
        return null;

    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
