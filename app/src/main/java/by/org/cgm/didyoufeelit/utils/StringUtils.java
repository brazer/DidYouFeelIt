package by.org.cgm.didyoufeelit.utils;

/**
 * Author: Anatol Salanevich
 * Date: 08.05.2015
 */
public class StringUtils {

    public final static String EMPTY = "";

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return String.valueOf((double) tmp / factor);
    }

}
