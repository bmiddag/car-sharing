package controllers;


import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.Filter;
import interfaces.InfoSessionDAO;
import interfaces.InscriptionDAO;
import jdbc.JDBCDataAccessProvider;
import models.*;
import objects.*;
import play.Logger;
import utils.Mailer;

import java.util.*;
import java.lang.Math;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.Date;
import java.util.zip.DataFormatException;

import org.joda.time.DateTime;
import org.joda.time.Days;
import utils.TimeUtils;

/**
 * Created by Robin on 2/05/14.
 */
public class Email {
    /**
     * When there is an infosession where a user will go to, he receives a email with a warning when the infosession takes place within a day.
     */
    public void checkForInfosessions() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            InscriptionDAO inscriptionDAO = dac.getInscriptionDAO();
            InfoSessionDAO infoSessionDAO = dac.getInfoSessionDAO();
            Filter<InfoSession> filter = infoSessionDAO.getFilter();
            filter.fieldGreaterThan(InfoSession.Field.DATE,System.currentTimeMillis());
            filter.fieldLessThanOrEquals(InfoSession.Field.DATE, TimeUtils.getDateOffset(System.currentTimeMillis(),0,0,1));
            List<InfoSession> infoSessions = infoSessionDAO.getByFilter(filter);
            for(InfoSession infoSession: infoSessions){
                for(Inscription inscription: InfoSessionsModel.getInscriptionsByInfosession(infoSession)){
                    Mailer.sendInfosessionSoonNotification(inscription.getUser(), infoSession);
                }
            }
        }
    }

    /**
     * When there are more than 2 notifications, send a user a mail with a warning.
     */
    public void checkForNotifications() throws DataAccessException {
        for(User user: AdminModel.getAllUsers())  {
            List<Notification> userNotifications = NotificationModel.getUnreadNotifications(user);
            if((userNotifications == null ? 0 : userNotifications.size()) > 2){
                Mailer.sendUnreadMessagesNotification(user, userNotifications);
            }
        }
    }

    public void checkForBills() throws DataAccessException {
        for (User user : AdminModel.getAllUsers()) {
            Invoice invoice = FacturisationModel.addInvoiceForUser(user);
            if(invoice != null && invoice.getTotalCost() != null) {
                if(invoice.getTotalCost() > 0) Mailer.sendEndOfQuarterNotification(user);
            } else {
                System.out.println("LOL WUT");
            }
        }
    }

    public void sendApproveRideNotification() throws DataAccessException{

        for(User user: AdminModel.getAllUsers()){
            if(CarManagementModel.getCarByUserId(user.getId()) != null){
                Mailer.sendApproveRidesMail(user);
            }
        }
    }
}

