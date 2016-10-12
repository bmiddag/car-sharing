package utils;

import com.typesafe.plugin.*;
import interfaces.DataAccessContext;
import jdbc.JDBCDataAccessProvider;
import models.AdminModel;
import objects.*;
import exceptions.DataAccessException;
import play.Logger;

import java.util.Collection;

/**
 * Created by Wouter Pinnoo on 16/02/14.
 */
public class Mailer {

    protected static final String FROM_ADDRESS = "Edran <noreply@edran.ugent.be>";



    public static enum Type {
        PLAIN_TEXT, HTML
    }

    private Mailer(){
    }

    public static String getMailHeader(User u){
        return new StringBuilder()
                .append(u.getName())
                .append(" <")
                .append(u.getMail())
                .append(">")
                .toString();
    }

    public static String sendRegistrationConfirmation(User u,String token){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Registratie");
            String body = AdminModel.parseTemplate(t,u,null,token,null);
            sendMail("Welkom bij Edran!", Mailer.getMailHeader(u), body, Type.HTML);
            return null;
        }
        catch (DataAccessException e) {
            return "Er ging iets mis bij het versturen van een confirmatie mail. Gelieve later opnieuw te proberen.";
        }
    }

    public static void sendUnreadMessagesNotification(User u, Collection<Notification> m){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Infosessie_herinnering");
            String body = AdminModel.parseTemplate(t,u,null,null,m);
            sendMail("Berichten op Edran", Mailer.getMailHeader(u), body, Type.HTML);

        }
        catch (DataAccessException e) {
            Logger.error("Notificationmails sending failed" + e.toString());
        }
    }

    public static void sendInfosessionConfirmation(User u, InfoSession i){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Infosessie_ingeschreven");
            String body = AdminModel.parseTemplate(t,u,i,null,null);
            sendMail("Inschrijving infosessie", Mailer.getMailHeader(u), body, Type.HTML);

        }
        catch (DataAccessException e) {
            Logger.error("Infosessions sending failed" + e.toString());
        }
    }

    public static void sendInfosessionChangeNotification(User u, InfoSession i){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Infosessie_verandering");
            String body = AdminModel.parseTemplate(t,u,i,null,null);
            sendMail("Wijziging van de infosessie", Mailer.getMailHeader(u), body, Type.HTML);
        }
        catch (DataAccessException e) {
            Logger.error("Infosessions sending failed" + e.toString());
        }
    }

    public static void sendInfosessionSoonNotification(User u, InfoSession i){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("");
            String body = AdminModel.parseTemplate(t,u,i,null,null);
            sendMail("Infosessie nabij", Mailer.getMailHeader(u), body, Type.HTML);
        }
        catch (DataAccessException e) {
            Logger.error("Infosessions sending failed" + e.toString());
        }
    }

    public static void sendEndOfQuarterNotification(User u){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("KwartaalRekening");
            String body = AdminModel.parseTemplate(t,u,null,null,null);
            sendMail("Afrekening beschikbaar", Mailer.getMailHeader(u), body, Type.HTML);
        }
        catch (DataAccessException e) {
            Logger.error("EndofQuartedMail sending failed" + e.toString());
        }
    }

    public static String sendMailReset(User u, Long token) {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Wachtwoord_reset");
            String body = AdminModel.parseTemplate(t,u,null,String.valueOf(token),null);
            sendMail("Resetten wachtwoord Edran",u.getMail(),body ,Type.HTML);
            return null;
        }
        catch (DataAccessException e) {
            return "Wachtwoordreset-mail verzenden mislukt, probeer later opnieuw.";
        }
    }

    public static void sendApproveRidesMail(User u){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            Template t = dac.getTemplateDAO().getTemplate("Bevestig_ritten");
            String body = AdminModel.parseTemplate(t,u,null,null,null);
            sendMail("Binnenkort afrekening", Mailer.getMailHeader(u), body, Type.HTML);
        }
        catch (DataAccessException e) {
            Logger.error("Fout bij het verzenden van de notificatie voor het bevestigen van ritten, probeer later opnieuw." + e.toString());
        }

    }

    public static void sendMail(String subject, String recipient, String body, Type type){
        if(subject == null || recipient == null || body == null) return;

        MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
        mail.setSubject(subject);
        mail.addRecipient(recipient);
        mail.addFrom(FROM_ADDRESS);

        switch(type){
            case PLAIN_TEXT:
                mail.send(body);
                break;
            case HTML:
            default:
                mail.sendHtml(body);
                break;
        }
    }
}
