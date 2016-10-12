package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This utility class for parsing and formatting of dates.
 *
 * @author Wouter Pinnoo
 * @author Strijn Seghers
 */
public class TimeUtils {

    private static final DateFormat HUMAN_READABLE_FORMAT =
        DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.forLanguageTag("nl"));

    private TimeUtils(){}

    public static String getFormattedDate(String format, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getFormattedDate(String format, long date) {
        return TimeUtils.getFormattedDate(format, new Date(date));
    }

    public static String getHumanReadableDate(long date) {
        return HUMAN_READABLE_FORMAT.format(new Date(date));
    }

    public static long stringToLong(String formatString, String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.parse(date).getTime();
    }

    public static long getDateOffset(long dateLong, int years, int months, int days) {
        Date date = new Date(dateLong);
        Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.YEAR, years);
        return cal.getTime().getTime();
    }
}
