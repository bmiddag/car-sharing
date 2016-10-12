package models;

import interfaces.*;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

import jdbc.JDBCDataAccessProvider;

import objects.*;

import play.Logger;

import exceptions.DataAccessException;

/**
 * Created by Gilles on 8/03/14.
 */
public class AdminModel {
    public static List<Template> getTemplates() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            TemplateDAO templateDAO = dac.getTemplateDAO();
            return templateDAO.getAllTemplates();
        }
    }

    /**
     * Get users
     * @return List with users
     */
    public static List<User> getUsers(Integer offset, Integer limit) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            List<User> userList= new ArrayList<User>();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            userList = userDAO.getPage(offset,limit);
            return userList;
        }
    }

    /**
     * Get all users
     * @return List with all users
     */
    public static List<User> getAllUsers() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            List<User> userList= new ArrayList<User>();
            dac.begin();
            UserDAO userDAO = dac.getUserDAO();
            userList = userDAO.getAllUsers();
            return userList;
        }
    }

    public static void createTemplate(String name) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            TemplateDAO templateDAO = dac.getTemplateDAO();
            templateDAO.createTemplate(name);
            dac.commit();
        }
    }

    public static String parseTemplate(Template t, User u) {
        return parseTemplate(t,u, null, null, null);
    }


    public static String parseTemplate(Template t, User u, InfoSession s, String tok, Collection<Notification> n) {
        String content = t.getContent();
        String parsedContent = "";
        int start = 0, end = 0;
        while(end < content.length()) {
            end = content.indexOf("{{",start);
            if(end == -1) end = content.length();
            parsedContent += content.substring(start,end);
            if(end != content.length()) {
                start = end+2;
                end = content.indexOf("}}",start);
                if(end == -1) {
                    end = content.length();
                    parsedContent += content.substring(start-2,end);
                    break;
                }
                switch(content.substring(start,end)) {
                    case "name":
                        parsedContent += u.getName(); break;
                    case "surname":
                        parsedContent += u.getSurname(); break;
                    case "title":
                        parsedContent += u.getTitle(); break;
                    case "email":
                        parsedContent += u.getMail(); break;
                    case "phone":
                        parsedContent += u.getPhone(); break;
                    case "address":
                        parsedContent += u.getAddress() == null ? "" : u.getAddress().getFormattedAddress(); break;
                    case "domicile":
                        parsedContent += u.getDomicile() == null ? "" : u.getDomicile().getFormattedAddress(); break;
                    case "zone":
                        parsedContent += u.getZone().getName(); break;
                    case "infosession.address":
                        parsedContent += s == null ? "(infosessie adres)" : (s.getAddress() == null ? "" : s.getAddress().getFormattedAddress()); break;
                    case "infosession.date":
                        parsedContent += s == null ? "(infosessie datum)" : s.getFormattedDate("d MMMMM yyyy"); break;
                    case "token":
                        parsedContent += tok == null ? "(token string)" : tok; break;
                    case "message.size":
                        parsedContent += n == null ? "geen" : n.size();  break;
                    case "notifications":
                        if(n == null) {
                            parsedContent += "(lijst notificaties)";
                        } else {
                            parsedContent += n.isEmpty() ? "" : "<ul>";
                            for(Notification not : n) {
                                parsedContent += "<li>" + not.getSubject() + not.getFormattedDate("d MMMMM") + "</li>";
                            }
                            parsedContent += n.isEmpty() ? "" : "</ul>";
                        }
                        break;
                    default:
                        parsedContent += content.substring(start-2,end+2); break;
                }
                start = end+2;
            }
        }
        return parsedContent;
    }

    public static String previewTemplate(String text) throws DataAccessException{

        Template t = new Template(1,"test",text,true,0l);
        return parseTemplate(t, UserModel.getCurrentUser());

    }

    public static void deleteTemplate(String id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            TemplateDAO templateDAO = dac.getTemplateDAO();
            int parsedId = Integer.parseInt(id);
            templateDAO.deleteTemplate(templateDAO.getTemplate(parsedId));
            dac.commit();
        }
    }

    public static void saveTemplate(int id, String name, String content) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            Template t = new Template(id, name, content,true, System.currentTimeMillis());
            // Use this to send mails:
            // utils.Mailer.sendTemplateTestMail(UserModel.getCurrentUser(),t);
            dac.begin();

            TemplateDAO templateDAO = dac.getTemplateDAO();
            templateDAO.updateTemplate(t);
            dac.commit();
        }
    }

    //CURRENTLY UNUSED
    public HashMap<String, Integer> getNotificiationPreferences(){
        HashMap<String, Integer> result = new HashMap<>();
        File file = new File("notifications.conf");
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String[] t = s.split("\t");
                result.put(t[0], Integer.parseInt(t[1]));
            }
        } catch(FileNotFoundException e){
        }
        return result;
    }

    public static void deleteAllUserPermissions(int userid) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            //PermissionDAO permissionDAO = dac.getPermissionDAO();
            UserPermissionDAO userPermissionDAO = dac.getUserPermissionDAO();
            List<UserPermission> uplist = userPermissionDAO.getUserPermissionByUser(UserModel.getUserById(userid));
            for (int i=0;i<uplist.size();i++){
                userPermissionDAO.deleteUserPermission(uplist.get(i).getId());
            }
            dac.commit();
        }
    }

    public static void addUserPermission(int userid,int permissionid) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PermissionDAO permissionDAO = dac.getPermissionDAO();
            UserPermissionDAO userPermissionDAO = dac.getUserPermissionDAO();

            userPermissionDAO.createUserPermission(UserModel.getUserById(userid),permissionDAO.getPermission(permissionid));

            dac.commit();
        }
    }
}

