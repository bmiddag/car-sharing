package models;

import controllers.Excel;
import controllers.PDF;
import exceptions.DataAccessException;
import interfaces.*;

import jdbc.JDBCDataAccessProvider;
import objects.*;
import play.Logger;
import utils.TimeUtils;
import views.html.bill;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * Created by Gilles on 27/03/14.
 */
public class FacturisationModel {
    /**
     * @return all PriceRanges from the DB, sorted by their lower bound.
     */
    public static List<PriceRange> getAllPriceRanges() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            List<PriceRange> priceRanges = priceRangeDAO.getAllPriceRanges();
            Collections.sort(priceRanges, new Comparator<PriceRange>(){
                @Override
                public int compare(PriceRange priceRange, PriceRange priceRange2) {
                    if(priceRange.getMin() < priceRange2.getMin()) return -1;
                    else if(priceRange.getMin() == priceRange2.getMin()) return 0;
                    else return 1;
                }
            });
            return priceRanges;
        }
    }

    /**
     * Creates a new price range
     * @param min the lower bound of the price range
     * @param max the upper bound of the price range
     * @param price the price per kilometer for the price range
     */

    public static void createNewPriceRange(int min, int max, int price) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            priceRangeDAO.createPriceRange(min, max, price);
            dac.commit();
        }
    }

    /**
     * Checks the correctness of the price ranges. Every value from 0 untill 65535 has to be in a pricerange
     * and there can't be any conflicts between priceranges (e.g: the upper bound of one pricerange is higher
     * than the lower bound of another)
     * @return A boolean indicating the correctness.
     */

    public static boolean checkPriceRanges() throws DataAccessException {
        List<PriceRange> priceRanges = getAllPriceRanges();
        if(priceRanges.size() == 0) return false;
        //The first lower bound has to be zero.
        if(priceRanges.get(0).getMin() != 0) return false;
        for(int i = 0; i < priceRanges.size() - 1; i++){
            //The lower bound of the next price range has to follow up on the upper bound.
            if(priceRanges.get(i).getMax() != priceRanges.get(i+1).getMin() - 1) return false;
        }
        //The last upper bound has to be 65535
        if(priceRanges.get(priceRanges.size()-1).getMax() != 65535) return false;
        return true;
    }

    /**
     * Method for updating the minimum of a pricerange.
     * @param id The id of the pricerange to update.
     * @param min The new minimum.
     */

    public static void updateMinimum(int id, int min) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            PriceRange p = priceRangeDAO.getPriceRange(id);
            p.setMin(min);
            priceRangeDAO.updatePriceRange(p);
            dac.commit();
        }
    }

    /**
     * Method for updating the maximum of a pricerange.
     * @param id The id of the pricerange to update.
     * @param max The new maximum.
     */

    public static void updateMaximum(int id, int max) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            PriceRange p = priceRangeDAO.getPriceRange(id);
            p.setMax(max);
            priceRangeDAO.updatePriceRange(p);
            dac.commit();
        }
    }

    /**
     * Method for updating the price of a pricerange.
     * @param id The id of the pricerange to update.
     * @param price The new price.
     */

    public static void updatePrice(int id, Integer price) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            PriceRange p = priceRangeDAO.getPriceRange(id);
            p.setPrice(price);
            priceRangeDAO.updatePriceRange(p);
            dac.commit();
        }
    }

    /**
     * Method that removes a pricerange from the database.
     * @param id The id of the pricerange that has to be removed.
     */

    public static void removePriceRange(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            PriceRange p = priceRangeDAO.getPriceRange(id);
            priceRangeDAO.deletePriceRange(p);
            dac.commit();
        }
    }

    /**
     * Private method that changes all bounds so no intervals conflict
     * CURRENTLY: BUGGED AND NOT USED
     */


    /*private static void changeBounds(int min, int max, float price){
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            List<PriceRange> priceRanges = priceRangeDAO.getAllPriceRanges();
            for(PriceRange priceRange: priceRanges){
                if(min > priceRange.getMin() && max < priceRange.getMax()) {
                    int temp = priceRange.getMax();
                    priceRange.setMax(min - 1);
                    priceRangeDAO.createPriceRange(max + 1, temp, new BigDecimal(price));
                }
                if(min < priceRange.getMax() && min > priceRange.getMin()) {
                    priceRange.setMax(min - 1);
                    int index = priceRanges.indexOf(priceRange) + 1;
                    while(index < priceRanges.size() && priceRanges.get(index).getMin() > min){
                        priceRangeDAO.deletePriceRange(priceRanges.get(index));
                        index++;
                    }
                }
                if(max > priceRange.getMin() && max < priceRange.getMax()) {
                    priceRange.setMin(max + 1);
                    int index = priceRanges.indexOf(priceRange) - 1;
                    while(index > 0 && priceRanges.get(index).getMax() < max){
                        priceRangeDAO.deletePriceRange(priceRanges.get(index));
                        index--;
                    }
                }
                priceRangeDAO.updatePriceRange(priceRange);
            }
            dac.commit();
        } catch (DataAccessException e) {
            Logger.error("", e);
        }
    }*/

    /**
     * Algorithm to calculate the costs made by every user in the system.
     * Iterate over every user in the system; for every ride made by the user multiply
     * the driven kilometers with the corresponding price.
     * @return A map that maps the user id's to a string format of the facturisations
     */
    //TODO: andere kosten in rekening brengen (behalve gereden km's)
    //TODO: ook opbrengsten voor een autoeigenaar in rekening brengen
    //TODO: enkel geaccepteerde ritgegevens in rekening brengen!
    public static Map<Integer, String> calculateFacturations() throws DataAccessException {
        Map<Integer, String> result = new HashMap<>();
        Map<Integer, Float> totalCosts = new HashMap<>();
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            List<PriceRange> priceRanges = priceRangeDAO.getAllPriceRanges();
            RideDAO rideDAO = dac.getRideDAO();
            List<Ride> rides = rideDAO.getAllRides();
            for(Ride ride: rides){
                int userID = ride.getUserID();
                float drivenKM = (float) (ride.getStopKM() - ride.getStartKM());
                float price = -1;
                //Check for every PriceRange if the number of driven kilometers is between the minimum and maximum of the range
                //If so: multiply the number of driven kilometers with the price of that range.
                //price should never be -1!
                for(PriceRange priceRange: priceRanges){
                    if(drivenKM >= priceRange.getMin() && drivenKM <= priceRange.getMax())
                        price = drivenKM*priceRange.getPrice().floatValue();
                }
                //TODO: String mooier formateren!
                String cost = "datum: " + ride.getBegin() + "\n"
                        + "\t Kilometerstand begin: " + ride.getStartKM() + "\n"
                        + "\t Kilometerstand einde: " + ride.getStopKM() + "\n"
                        + "\t Gereden kilometers: " + (drivenKM) + "\n"
                        + "\t ==> Prijs: " + price + "\n-----------------------------------\n";
                if(result.get(userID) == null){
                    //Put a new element in the hashmap
                    String header = "\n FACTUUR \n \n-----------------------------------\n" + cost;
                    result.put(userID, header);
                    totalCosts.put(userID, price);
                } else {
                    //Update an element in the hashmap.
                    String entry = result.get(userID) + "\n" + cost;
                    float newCost = totalCosts.get(userID) + price;
                    result.put(userID, entry);
                    totalCosts.put(userID, newCost);
                }
            }
            for(Integer key: totalCosts.keySet()){
                //End the facturation with the total cost.
                String totalCost = "\n \n \n Totale kost = " + totalCosts.get(key);
                String s = result.get(key) + totalCost;
                result.put(key, s);
            }
        }
        return result;
    }

    /**
     * Get the invoice objects for a specific user, useful for a invoices page
     * @param   user    The user whose invoice you want to get
     * @return          The invoices for this specific user
     */
    public static List<Invoice> getInvoicesByUser(User user) throws DataAccessException {
        Long payDate = TimeUtils.getDateOffset(new java.util.Date().getTime(), 0, 1, 0); // 1 month from now!
        List<Invoice> userInvoices;
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            RideDAO rideDAO = dac.getRideDAO();
            RefuelingDAO refuelingDAO = dac.getRefuelingDAO();
            InvoiceDAO invoiceDAO = dac.getInvoiceDAO();
            Filter<Invoice> invoiceFilter = invoiceDAO.getFilter();
            invoiceFilter.fieldEquals(Invoice.Field.USER,user.getId());
            userInvoices = invoiceDAO.getByFilter(invoiceFilter);
        }
        return userInvoices;
    }

    public static Invoice getLatestInvoiceByUser(User user) throws DataAccessException {
        Invoice latestInvoice = null;
        List<Invoice> userInvoices = getInvoicesByUser(user);
        for(Invoice i: userInvoices) {
            if(i.getTime() > (latestInvoice == null? i.getTime()-1: latestInvoice.getTime())) {
                latestInvoice = i;
            }
        }
        return latestInvoice;
    }

    /**
     * Algorithm to calculate the costs made by a specific user
     * For every ride made by the user, multiply the driven kilometers with the corresponding price.
     * @param   user    The user whose invoice you want to get
     * @return          The invoice for this specific user
     */
    // TODO: Send a reminder that all rides from a specific period should be approved, then only start this method when all those rides are approved.
    public static Invoice addInvoiceForUser(User user) throws DataAccessException {
        Long payDate = TimeUtils.getDateOffset(new java.util.Date().getTime(), 0, 1, 0); // 1 month from now!
        Invoice newInvoice;
        //LocalInvoice invoice = new LocalInvoice(user,date,beforeDate);
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            RideDAO rideDAO = dac.getRideDAO();
            RefuelingDAO refuelingDAO = dac.getRefuelingDAO();
            InvoiceDAO invoiceDAO = dac.getInvoiceDAO();
            FileDAO fileDAO = dac.getFileDAO();
            Filter<Invoice> invoiceFilter = invoiceDAO.getFilter();
            invoiceFilter.fieldEquals(Invoice.Field.USER,user.getId());
            List<Invoice> userInvoices = invoiceDAO.getByFilter(invoiceFilter);
            Long lastDate = null;
            for(Invoice i: userInvoices) {
                if(i.getTime() > (lastDate == null? i.getTime()-1: lastDate)) lastDate = i.getTime();
            }
            newInvoice = invoiceDAO.createInvoice(user,lastDate == null? user.getTime() : lastDate);
            //newInvoice = new Invoice(1,user,lastDate,new java.util.Date().getTime());
            List<Ride> ridesList = getInvoiceRides(rideDAO, newInvoice);
            List<Refueling> refuelingsList = getInvoiceRefuelings(refuelingDAO, newInvoice);

            int totalCost = getTotalCost(user, ridesList, refuelingsList);
            newInvoice.setTotalCost(totalCost);
            byte[] pdfBytes = PDF.invoiceBytes(bill.render("Ter attentie van " + user.getTitle() + " " + user.getSurname(), newInvoice, ridesList, refuelingsList, payDate));
            byte[] xlsxBytes = Excel.invoiceBytes(newInvoice, ridesList, refuelingsList, payDate);
            File PDF = fileDAO.createFile(new ByteArrayInputStream(pdfBytes),"invoice_" + user.getId() + "_" + newInvoice.getId() + ".pdf");
            File XLSX = fileDAO.createFile(new ByteArrayInputStream(xlsxBytes),"invoice_" + user.getId() + "_" + newInvoice.getId() + ".xlsx");
            newInvoice.setPDF(PDF.getId());
            newInvoice.setExcel(XLSX.getId());
            invoiceDAO.updateInvoice(newInvoice);
            // TODO: Create button in admin panel to create invoices for all users whenever the admin wants to <--- not necessary

            if(totalCost > 0){
                NotificationModel.createNotification(user,"Nieuwe factuur beschikbaar","Er is een nieuwe factuur beschikbaar ter waarde van &#8364;"+String.format("%d.%02d",totalCost/100,totalCost%100)
                        + ". Gelieve deze som te betalen ten laatste voor " + TimeUtils.getFormattedDate("dd/MM/yyyy",payDate) + ".");
            }

            dac.commit();

            return newInvoice;
        }
    }

    public static String getOGM(Invoice invoice) {
        int id = invoice.getId();
        int check = id % 97;
        return String.format("+++%03d/%04d/%03d%02d+++",(id/10000000)%1000,(id/1000)%10000,id%1000,check);
    }

    private static List<Ride> getInvoiceRides(RideDAO rideDAO, Invoice invoice) throws DataAccessException {
        Filter<Ride> filter = rideDAO.getFilter();
        filter.fieldEquals(Ride.Field.USER, invoice.getUser().getId());
        filter.fieldEquals(Ride.Field.APPROVED,Boolean.TRUE);
        filter.fieldGreaterThanOrEquals(Ride.Field.END,invoice.getTillDate()); // tillDate = begin of ride counting period
        filter.fieldLessThan(Ride.Field.END,invoice.getTime());
        return rideDAO.getByFilter(filter);
    }

    private static List<Refueling> getInvoiceRefuelings(RefuelingDAO refuelingDAO, Invoice invoice) throws DataAccessException {
        return refuelingDAO.getRefuelingsByUser(invoice.getUser().getId(), invoice.getTillDate(), invoice.getTime());
    }

    private static List<Damage> getInvoiceDamages(DamageDAO damageDAO, Invoice invoice) throws DataAccessException {
        Filter<Damage> filter = damageDAO.getFilter();
        filter.fieldEquals(Damage.Field.USER,invoice.getUser().getId());
        filter.fieldGreaterThanOrEquals(Damage.Field.TIME,invoice.getTillDate()); // tillDate = begin of ride counting period
        filter.fieldLessThan(Damage.Field.TIME,invoice.getTime());
        return damageDAO.getByFilter(filter);
    }
      
    private static int getTotalCost(User u, List<Ride> rides, List<Refueling> refuelings) throws DataAccessException {
        int total = 0;
        for(Ride ride: rides) {
            total += ride.getPrice();
        }
        for(Refueling refueling: refuelings) {
            if(!refueling.getRide().getCar().getOwner().equals(u)) total -= refueling.getPrice();
        }
        return total;
    }

    public static int getCurrentTotal(User user) throws DataAccessException {
        Invoice newInvoice;
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            RefuelingDAO refuelingDAO = dac.getRefuelingDAO();
            InvoiceDAO invoiceDAO = dac.getInvoiceDAO();
            Filter<Invoice> invoiceFilter = invoiceDAO.getFilter();
            invoiceFilter.fieldEquals(Invoice.Field.USER,user.getId());
            List<Invoice> userInvoices = invoiceDAO.getByFilter(invoiceFilter);
            Long lastDate = null;
            for(Invoice i: userInvoices) {
                if(i.getTime() > (lastDate == null? i.getTime()-1: lastDate)) lastDate = i.getTime();
            }
            newInvoice = new Invoice(1,user,null,null,0,lastDate == null? user.getTime() : lastDate,System.currentTimeMillis());
            List<Ride> ridesList = getInvoiceRides(rideDAO, newInvoice);
            List<Refueling> refuelingsList = getInvoiceRefuelings(refuelingDAO, newInvoice);
            int totalCost = getTotalCost(user, ridesList, refuelingsList);
            return totalCost;
        }
    }

    public static int getSubtotalByInvoice(Invoice invoice, List<Ride> rides, List<Refueling> refuelings, boolean ridesSubTotal) {
        int total = 0;
        for(Ride ride: rides) {
            total += ride.getPrice();
        }
        if(ridesSubTotal) {
            return total;
        } else return -invoice.getTotalCost()+total;
    }


}