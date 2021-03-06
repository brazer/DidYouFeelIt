package by.org.cgm.didyoufeelit.utils;

/**
 * Author: Anatol Salanevich
 * Date: 08.05.2015
 */
public class StringUtils {

    public final static String EMPTY = "";
    public final static String PAGE_POSITION = "page_number";
    public static final String EVENTS_FILE = "events.json";
    public static final String EVENT_LIST_POSITION = "position";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String EMAIL_CGM = "salanevich@cgm.org.by";

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return String.valueOf((double) tmp / factor);
    }

    public static String getDoubleDigits(int number) {
        if (number<10) return "0" + number;
        else return String.valueOf(number);
    }

}
