package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.FileDAO;
import interfaces.InvoiceDAO;
import jdbc.JDBCDataAccessProvider;
import models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import play.mvc.*;

import views.html.*;
import objects.*;

import java.util.*;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.request;
import static play.mvc.Controller.response;
import static play.mvc.Results.*;

import play.*;

/**
 * Contains all functions and methods for the page "Beheer" (admin page)
 * Created by stevendeblieck on 04/03/14.
 */
public class Admin {
    /**
     * Get the permissions for the selected user to an xml
     *
     * @param  userid id of the user
     * @return The rendered xml that contains the permissions
     * @throws DataAccessException
     */
    public static Result getUserPermissions(Integer userid) throws DataAccessException {
        User user = UserModel.getUserById(userid);
        UserSubject userSubject = new UserSubject(user);
        List<MyPermission> permissionList = (List<MyPermission>) userSubject.getPermissions();
        List<String> permissionStrings = new ArrayList<String>();
        for (int i=0;i<permissionList.size();i++){
            permissionStrings.add(permissionList.get(i).getValue());
        }
        return ok(views.xml.userpermissions.render(permissionStrings));
    }

    @Restrict({@Group("admin")})
    public static Result admin(String tab, int offset, int limit) throws DataAccessException {
        return ok(admin.render(tab, AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),AdminModel.getUsers(offset,limit),RoleModel.getAllRoles(),PermissionModel.getAllPermissions(),offset,limit));
    }



    public static Result postUsers() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        ArrayList<String> checkboxes = new ArrayList<>();
        //This is done in a try-catch block, to make sure users were selected to process
        //If the user didn't select any, a NullPointer will be thrown up and caught here.
        try{
            for(String checkbox: map.get("user")) checkboxes.add(checkbox);
        } catch(NullPointerException e){
            flash("error", "Gelieve eerst users te selecteren.");
            return ok(admin.render("users", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),AdminModel.getUsers(0,199),RoleModel.getAllRoles(),PermissionModel.getAllPermissions(),0,100));
        }
        //Process every selected (through a checkbox) user
        String action = map.get("submit")[0];
        //delete, temporary block users or add/remove permissions
        //has to be done by superuser
        //send email afterwards
        //delete
        if("Verwijderen".equals(action)){
            for(int i = 0; i < checkboxes.size(); i++) {
                UserModel.removeUser(Integer.parseInt(checkboxes.get(i)));
                flash("success", "De geselecteerde users zijn verwijderd!");
            }
         //change role or temporary block
        }else if("Rol wijzigen".equals(action)){
            ArrayList<String> roles = new ArrayList<>();
            for(String option: map.get("selectedrole")) roles.add(option);
            for(int i = 0; i < checkboxes.size(); i++) {
                UserModel.setRole(Integer.parseInt(checkboxes.get(i)), Integer.parseInt(roles.get(0)));
                flash("success", "De geselecteerde users hebben een nieuwe rol!");
            }
        //change permissions
        }else if("Permissies wijzigen".equals(action)){
            String userid = map.get("userid")[0];
            System.out.println(userid);
            AdminModel.deleteAllUserPermissions(Integer.parseInt(userid));
            for(int i = 0; i < checkboxes.size(); i++) {
                AdminModel.addUserPermission(Integer.parseInt(userid),Integer.parseInt(checkboxes.get(i)));
            }
        }
        return ok(admin.render("users", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),AdminModel.getUsers(0,199),RoleModel.getAllRoles(),PermissionModel.getAllPermissions(),0,100));
    }

    public static Result filterUsers() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        ArrayList<String> checkboxes = new ArrayList<>();
        String action = map.get("submit")[0];
        ArrayList<String> roles = new ArrayList<>();
        for(String option: map.get("selectedrole")) roles.add(option);
        int role = Integer.parseInt(roles.get(0));
        String name = map.get("name")[0];
        List<User> users= new ArrayList<>();
        users = AdminModel.getAllUsers();
        List<User> filteredusers= new ArrayList<>();
            for (int i=0;i<users.size();i++){
                if (users.get(i).getRole().getId()==role || role==1000){
                    if (users.get(i).getName().contains(name) || users.get(i).getSurname().contains(name) || name==""){
                    filteredusers.add(users.get(i));
                }}

            }


        return ok(admin.render("users", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),filteredusers,RoleModel.getAllRoles(),PermissionModel.getAllPermissions(),0,0));
    }

    // TODO: Change to returning a file
    public static Result renderPDF() throws DataAccessException {
        User user = UserModel.getCurrentUser();
        return null;
        //return PDF.invoice(bill.render("Ter attentie van " + user.getTitle() + " " + user.getSurname(), FacturisationModel.getInvoiceByUser(user)));
    }

    public static Result renderExcel(int invoiceId) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InvoiceDAO invoiceDAO = dac.getInvoiceDAO();
            FileDAO fileDAO = dac.getFileDAO();
            Invoice invoice = invoiceDAO.getInvoice(invoiceId);
            if(invoice.getUser().equals(UserModel.getCurrentUser()) || UserModel.getCurrentUser().getRole().getName().equals("admin")) {
                objects.File xlsxFile = fileDAO.getFile(invoice.getExcel());
                response().setHeader("Content-Disposition", "attachment; filename=" + xlsxFile.getFilename());
                return Results.ok(xlsxFile.getFileAsStream()).as("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } else {
                throw new SecurityException("Onvoldoende rechten om deze factuur te bekijken!");
            }
        }
        //return Excel.invoice(FacturisationModel.getInvoiceByUser(user));
    }

    public static Result renderPDF(int invoiceId) throws DataAccessException {
        //User user = UserModel.getUserById(userId);
        //Invoice invoice = FacturisationModel.addInvoiceForUser(user);
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InvoiceDAO invoiceDAO = dac.getInvoiceDAO();
            FileDAO fileDAO = dac.getFileDAO();
            Invoice invoice = invoiceDAO.getInvoice(invoiceId);
            if(invoice.getUser().equals(UserModel.getCurrentUser()) || UserModel.getCurrentUser().getRole().getName().equals("admin")) {
                objects.File pdfFile = fileDAO.getFile(invoice.getPDF());
                response().setHeader("Content-Disposition", "attachment; filename=" + pdfFile.getFilename());
                return Results.ok(pdfFile.getFileAsStream()).as("application/pdf");
            } else {
                throw new SecurityException("Onvoldoende rechten om deze factuur te bekijken!");
            }
        }
        //return PDF.invoice(bill.render("Ter attentie van " + user.getTitle() + " " + user.getSurname(), FacturisationModel.getInvoiceByUser(user)));
    }

    public static Result createTemplate() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String s = map.get("templateName")[0];
        String action = map.get("createAccept")[0];
        if("Creëer".equals(action)){
            try {
                AdminModel.createTemplate(s);
            } catch(DataAccessException e) {
                flash("error", "Er is een fout opgetreden. Dit template kon niet aangemaakt worden. Probeer het later opnieuw.");
                Logger.error("", e);
            }
        } else {
            return badRequest("Deze actie is niet toegestaan.");
        }
        return ok(admin.render("mailpreferences", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    public static Result deleteTemplate() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String id = map.get("deleteTemplateId")[0];
        try {
            AdminModel.deleteTemplate(id);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Dit template kon niet verwijderd worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("mailpreferences", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    /**
     * This method will save the template to the database.
     * @return The admin page.
     */
    public static Result saveTemplate() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        int id = Integer.parseInt(map.get("templateid")[0]);
        String content = map.get("editor")[0];
        String name = map.get("templatename")[0];
        try {
            AdminModel.saveTemplate(id, name, content);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Dit template kon niet worden opgeslaan. Probeer het later opnieuw.");
            Logger.error("", e);
        }

        String frequency = map.get("frequency")[0];
        boolean activated = map.containsKey("activated");

        // TODO: submit action

        return ok(admin.render("mailpreferences", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    /**
     * This method will show a preview of the parsed template
     * @return A model with the parsed template content in it.
     */
    public static Result previewTemplate() throws DataAccessException{

        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String content = map.get("content")[0];
        return ok(AdminModel.previewTemplate(content));
    }

    /**
     * This method will calculate facturations for every user.
     * @return The same page
     */
    public static Result calculateFacturations() throws DataAccessException {
        Map<Integer, String> facturaties;
        facturaties = FacturisationModel.calculateFacturations();
        for(Integer key: facturaties.keySet()){
            Logger.debug(facturaties.get(key));
        }
        return ok(admin.render("accounts", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    /**
     * Adds a new price range to the database.
     * @return The same page with the new added price range
     */
    public static Result newPriceRange() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        try {
            String min = map.get("pricerange-begin")[0];
            String max = map.get("pricerange-end")[0];
            String price = map.get("pricerange-price")[0];
            if(Integer.parseInt(min) < Integer.parseInt(max)) FacturisationModel.createNewPriceRange(Integer.parseInt(min), Integer.parseInt(max), (int)Math.round(Double.parseDouble(price)*100));
            else flash("error", "De ondergrens moet kleiner zijn dan de bovengrens!");
        } catch(NullPointerException e){
            flash("error", "Gelieve alles eerst in te vullen.");
        } catch (NumberFormatException e){
            flash("error", "Minimum en maximum moeten een decimaal getal zijn, prijs een kommagetal.");
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Dit prijsinterval kon niet aangemaakt worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("accounts", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    /**
     * The elements in the table are editable. When clicked, a box will appear where the new value can be given in.
     * This method will change the old value in the new value.
     * @return The same page
     */

    public static Result updatePriceRange() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String value = map.get("value")[0];
        String name = map.get("name")[0];
        String priceRangeId = map.get("pk")[0];
        try {
            if(name.equals("minimum")){
                FacturisationModel.updateMinimum(Integer.parseInt(priceRangeId), Integer.parseInt(value));
            } else if(name.equals("maximum")){
                FacturisationModel.updateMaximum(Integer.parseInt(priceRangeId), Integer.parseInt(value));
            } else if(name.equals("price")){
                FacturisationModel.updatePrice(Integer.parseInt(priceRangeId), (int)Math.round(Double.parseDouble(value)*100));
            } else {
                return badRequest("Deze actie is niet toegestaan.");
            }
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Dit prijsinterval kon niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return admin("accounts",0,100);
    }

    /**
     * This method will remove the correct price range
     * @return The same page
     */

    public static Result removePriceRange() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String priceRangeId = map.get("selectedID")[0];
        try {
            FacturisationModel.removePriceRange(Integer.parseInt(priceRangeId));
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Dit prijsinterval kon niet verwijderd worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("accounts", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),null,null,null,0,100));
    }

    public static Result updateCar() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        try {
            CarManagementModel.updateCars(map);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. De autogegevens konden niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("cars", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(), AdminModel.getUsers(0,199), RoleModel.getAllRoles(), PermissionModel.getAllPermissions(),0,100));
    }

    public static Result updateProof() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        try {
            ProofsModel.updateProofs(map);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Het bewijsmateriaal kon niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("proof", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(), AdminModel.getUsers(0,199), RoleModel.getAllRoles(), PermissionModel.getAllPermissions(),0,100));

    }

    public static Result updateRefueling() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        try {
            ProofsModel.updateRefuelings(map);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. Deze tankbeurt kon niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return ok(admin.render("proof", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(), AdminModel.getUsers(0,199), RoleModel.getAllRoles(), PermissionModel.getAllPermissions(),0,100));
    }

    public static Result getBackup()  {
        response().setContentType("application/x-download");
        response().setHeader("Content-disposition","attachment; filename=current.sql.bz2");
        response().setHeader("Content-Description", "File Transfer");
        try {
            java.io.File file = Play.application().getFile("/backups/current.sql.bz2");
            return ok(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            flash("error", "Kon de laatste back-up niet vinden, klik aub hieronder om een nieuwe aan te maken, dit kan enige momenten duren.");
            return redirect(routes.Admin.admin("backup",0,100));
        }
    }

    public static Result createBackup()  {
        Runtime rt = Runtime.getRuntime();
        try {
            // TODO backup prod!
            Process pr = rt.exec("./backup.sh test");
            //return redirect(routes.Admin.getBackup());
            flash("success", "Backups worden op dit moment gecreëerd. Dit kan lang duren, afhankelijk van de belasting op de server en de grootte van de backup. Reken zeker op 15 minuten.");
            return redirect(routes.Admin.admin("backup",0,100));//ok(admin.render("parameters",null,null,null,null,null,0,100));
        } catch (IOException e) {
            flash("error","Er was een probleem bij het creeëren van een nieuwe backup.");
            return badRequest();
        }

    }

    //TODO
    public static Result updateRegistration() throws DataAccessException{


        Map<String, String[]> map = request().body().asFormUrlEncoded();

        try {
            RoleModel.updateRoles(map);
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. De gegevens konden niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }


        //werkt niet meer als er geen open verzoeken zijn van almost_users....dus eens geaccepteerd, slaagt hij tilt

        return ok(admin.render("users", AdminModel.getTemplates(), FacturisationModel.getAllPriceRanges(),AdminModel.getUsers(0,199),RoleModel.getAllRoles(),PermissionModel.getAllPermissions(),0,100));

    }
}
