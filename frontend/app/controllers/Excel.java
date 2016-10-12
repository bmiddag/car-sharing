package controllers;

import exceptions.DataAccessException;
import models.FacturisationModel;
import objects.Invoice;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import play.Logger;
import play.mvc.Result;
import play.mvc.Results;
import utils.TimeUtils;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Wouter Pinnoo on 05/05/14.
 */
public class Excel {

    private static XSSFColor COLOR = new XSSFColor(new Color(92,184,92));

    public static byte[] invoiceBytes(Invoice invoice, List<objects.Ride> rides, List<objects.Refueling> refuelings, Long payDate) {
        try(ByteArrayOutputStream stream = new ByteArrayOutputStream()){
            XSSFWorkbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Ritten");

            Row headerRow1 = sheet.createRow(0);
            headerRow1.createCell(0).setCellValue("Dégage VZW");
            Font headerFont = wb.createFont();
            headerFont.setFontHeightInPoints((short)18);
            headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(COLOR);
            headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headerStyle.setFont(headerFont);
            headerRow1.setRowStyle(headerStyle);
            headerRow1.setHeightInPoints(25);

            Row headerRow2 = sheet.createRow(1);
            headerRow2.createCell(0).setCellValue("Afrekening voor "
                    + invoice.getUser().getTitle()
                    + " "
                    + invoice.getUser().getSurname()
                    + " "
                    + invoice.getUser().getName());
            headerRow2.createCell(5).setCellValue(TimeUtils.getFormattedDate("dd/MM/yyyy", invoice.getTime()));

            Row ridesHeaderRow = sheet.createRow(4);
            ridesHeaderRow.createCell(0).setCellValue("Auto");
            ridesHeaderRow.createCell(1).setCellValue("Datum");
            ridesHeaderRow.createCell(2).setCellValue("Beginkilometerstand");
            ridesHeaderRow.createCell(3).setCellValue("Eindkilometerstand");
            ridesHeaderRow.createCell(4).setCellValue("Aantal gereden kilometers");
            ridesHeaderRow.createCell(5).setCellValue("Prijs");

            XSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setFillForegroundColor(COLOR);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setWrapText(true);
            Font ridesHeaderFont = wb.createFont();
            ridesHeaderFont.setFontHeightInPoints((short) 16);
            ridesHeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(ridesHeaderFont);
            ridesHeaderRow.setRowStyle(style);
            ridesHeaderRow.setHeightInPoints(25);

            int i = 5;
            for(objects.Ride ride : rides){
                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(ride.getCar().getName());
                row.createCell(1).setCellValue(TimeUtils.getFormattedDate("dd/MM/yyyy", ride.getTime()));
                row.createCell(2).setCellValue(ride.getStartKM());
                row.createCell(3).setCellValue(ride.getStopKM());
                row.createCell(4).setCellValue(ride.getStopKM() - ride.getStartKM());
                row.createCell(5).setCellValue(ride.getPrice() / 100);
            }

            Row subTotal1 = sheet.createRow(i++);
            subTotal1.createCell(4).setCellValue("Subtotaal:");
            subTotal1.createCell(5).setCellValue(FacturisationModel.getSubtotalByInvoice(invoice, rides, refuelings, true) / 100);

            Row refuelingsHeaderRow = sheet.createRow(i++);
            refuelingsHeaderRow.createCell(0).setCellValue("Tankbeurt");
            refuelingsHeaderRow.createCell(1).setCellValue("Aantal liter");
            refuelingsHeaderRow.createCell(2).setCellValue("Datum");
            refuelingsHeaderRow.createCell(3).setCellValue("Prijs");
            refuelingsHeaderRow.setRowStyle(style);
            refuelingsHeaderRow.setHeightInPoints(25);

            for(objects.Refueling refueling : refuelings){
                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue("#" + refueling.getId());
                row.createCell(1).setCellValue(refueling.getLitre());
                row.createCell(2).setCellValue(utils.TimeUtils.getFormattedDate("%dd/MM/yyyy",refueling.getTime()));
                row.createCell(3).setCellValue(refueling.getPrice() / 100);
            }

            Row subTotal2 = sheet.createRow(i++);
            subTotal2.createCell(4).setCellValue("Subtotaal:");
            subTotal2.createCell(5).setCellValue(FacturisationModel.getSubtotalByInvoice(invoice, rides, refuelings, false) / 100);

            Row footerRow1 = sheet.createRow(i++);
            footerRow1.createCell(4).setCellValue("Totaal:");
            footerRow1.createCell(5).setCellValue(invoice.getTotalCost() / 100);

            Row footerRow2 = sheet.createRow(i);
            footerRow2.createCell(4).setCellValue("Te betalen voor:");
            footerRow2.createCell(5).setCellValue(TimeUtils.getFormattedDate("dd/MM/yyyy", payDate));

            wb.write(stream);
            return stream.toByteArray();
        } catch (IOException | DataAccessException e) {
            Logger.error("Creating excel file from invoice", e);
        }
        return null;
    }

    public static Result invoice(Invoice invoice, List<objects.Ride> rides, List<objects.Refueling> refuelings, Long payDate) {
        return Results.ok(invoiceBytes(invoice,rides,refuelings,payDate)).as("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
