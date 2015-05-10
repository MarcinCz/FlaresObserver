package pl.mczerwi.flarespredict.heavensabove;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by marcin on 2015-04-12.
 */
class HeavensAboveUtil {

    private final static DateTimeFormatter dtf = DateTimeFormat.forPattern(HeavensAboveConstants.DATE_FORMAT).withLocale(Locale.ENGLISH).withZoneUTC();

    public static DateTime parseDateFromString(String dateString) {

        DateTime dateTime = dtf.withZoneUTC().parseDateTime(dateString);
        DateTime dateTimeNow = DateTime.now();

        //set current year
        dateTime = dateTime.plusYears(dateTimeNow.getYear() - dateTime.getYear());

        if(dateTime.isBefore(dateTimeNow)) {
            //add one year if it's the date from the next year
            dateTime = dateTime.plusYears(1);
        }
        return dateTime;
    }
}
