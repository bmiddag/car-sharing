package models;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.Filter;
import interfaces.RoleDAO;
import interfaces.UserDAO;
import jdbc.JDBCDataAccessProvider;
import objects.Role;
import objects.User;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This model is used to check the permissions of a user.
 *
 * @author  Bart Middag
 * @see     objects.Role
 */
public class RoleModel {
    /**
     * This enum lists all possible permissions.
     */
    public enum Permission {
        VIEW_INFOSESSIONS, EDIT_INFOSESSIONS, VIEW_CARMANAGEMENT, VIEW_RESERVATIONS, VIEW_ADMIN_PANEL, ADD_CAR
    }

    /**
     * Checks if a user has a specific permission.
     *
     * @param   user        The user whose permissions you want to check.
     * @param   permission  The permission you want to check for this user.
     * @return              True if the user has this permission.
     */
    public static boolean hasPermission(User user, Permission permission) {
        switch(permission) {
            case ADD_CAR:
                if(user != null && user.getRole().getName().equals("new_user")) return true; break;
            case EDIT_INFOSESSIONS:
                if(user != null && user.getRole().getName().equals("admin")) return true; break;
            case VIEW_INFOSESSIONS:
                if(user != null) {
                    if(user.getRole().getName().equals("admin")) { return true; }
                    if(user.getRole().getName().contains("new_")) {
                        try {
                            if(InfoSessionsModel.getInscriptionCount(user) < 1) return true;
                        } catch(DataAccessException e) {
                            Logger.error("", e);
                        }
                    }
                }
                break;
            case VIEW_CARMANAGEMENT:
                if(user != null && (user.getRole().getName().equals("admin") || (user.getRole().getName().equals("owner")))) return true; break;
            case VIEW_RESERVATIONS:
                if(user != null && (user.getRole().getName().equals("admin") || (!user.getRole().getName().contains("new_")))) return true; break;
            case VIEW_ADMIN_PANEL:
                if(user != null && user.getRole().getName().equals("admin")) return true; break;
            default:
                return false;
        }
        return false;
    }

    /**
     * Checks if the current user has a specific permission.
     *
     * @param   permission  The permission you want to check for the current user.
     * @return              True if the user has this permission.
     */
    public static boolean hasPermission(Permission permission) throws DataAccessException{
        return hasPermission(UserModel.getCurrentUser(),permission);
    }

    /**
     * Gets all roles from the database.
     * @return  A list of all roles
     */
    public static List<Role> getAllRoles() throws DataAccessException{
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RoleDAO roleDAO = context.getRoleDAO();
            Filter filter = roleDAO.getFilter();
            filter.fieldEqualsNot(Role.Field.ID,null);
            return roleDAO.getByFilter(filter);
        }
    }

    public static void updateRoles(Map<String,String[]> map) throws DataAccessException{

        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            for(Map.Entry<String, String[]> e : map.entrySet()){
                Logger.debug(e.getValue()[0]);
                if(e.getValue()[0].equals("true")){
                    String id = e.getKey().split("Field")[1];
                    User user = userDAO.getUser(Integer.parseInt(id));
                    RoleDAO roleDAO = dac.getRoleDAO();
                    Role role = roleDAO.getRole(user.getRole().getId());
                    if(role.getName().equals("almost_user")){
                        role = roleDAO.getRoleByName("user");
                        String message = "Proficiat je bent vanaf nu een volwaardige gebruiker! Om het systeem wat beter te leren kennen"
                        + ", raden we je aan onze handleiding te lezen!";
                        NotificationModel.createNotification(user,"Registratie goedgekeurd",message);
                    }
                    if(role.getName().equals("almost_owner")){
                        if(CarManagementModel.getCarByUserId(user.getId()) != null && CarManagementModel.getCarByUserId(user.getId()).getApproved()){
                            role = roleDAO.getRoleByName("owner");
                            String message = "Proficiat je bent vanaf nu een volwaardige gebruiker! Om het systeem wat beter te leren kennen"
                                    + ", raden we je aan onze handleiding te lezen!";
                            NotificationModel.createNotification(user,"Registratie goedgekeurd",message);
                        }
                    }
                    user.setRole(role);
                    user.setApproved(true);
                    userDAO.updateUser(user);
                } else if(e.getValue()[0].equals("false")){
                    String id = e.getKey().split("Field")[1];
                    User user = userDAO.getUser(Integer.parseInt(id));
                    user.setApproved(false);
                    userDAO.updateUser(user);
                }

            }



            dac.commit();
        }

    }




}
