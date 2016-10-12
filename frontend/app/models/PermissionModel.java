package models;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.PermissionDAO;
import interfaces.RoleDAO;
import jdbc.JDBCDataAccessProvider;
import objects.Permission;
import objects.Role;
import objects.User;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This model is used to check the permissions of a user.
 *
 * @author  Bart Middag
 * @see     objects.Permission
 */
public class PermissionModel {


    public static List<Permission> getAllPermissions() throws DataAccessException{
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            List<Permission> permissionList= new ArrayList<>();
            context.begin();
            PermissionDAO permissionDAO = context.getPermissionDAO();
            Permission p;
            for (int i=1;i<37;i++){
                p=permissionDAO.getPermission(i);
                permissionList.add(p);
            }
            context.commit();
            return permissionList;
        }
    }
    }
