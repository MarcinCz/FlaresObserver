package pl.mczerwi.flarespredict.heavensabove;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeavensAboveUtilTest {

    @Test
    public void shouldParseDateFromString() throws Exception {
        DateTime now = DateTime.now();

        DateTime date = HeavensAboveUtil.parseDateFromString("Dec 31, 23:59:58");
        System.out.println(date);
        assertEquals(12, date.getMonthOfYear());
        assertEquals(31, date.getDayOfMonth());
        assertEquals(23, date.getHourOfDay());
        assertEquals(59, date.getMinuteOfHour());
        assertEquals(58, date.getSecondOfMinute());
        assertEquals(now.getYear(), date.getYear());

        date = HeavensAboveUtil.parseDateFromString("Jan 1, 00:00:01");
        System.out.println(date);
        assertEquals(now.getYear() + 1, date.getYear());
    }

    @Test
    public void shouldPrintDateWithTimeZone() {
        DateTime date = HeavensAboveUtil.parseDateFromString("May 19, 21:59:58");
        DateTime now = DateTime.now();
        System.out.println("Getting hour difference: " + date + now);
        System.out.println(Hours.hoursBetween(date, now).getHours());
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println(dtf.withZone(DateTimeZone.getDefault()).print(date));
        assertTrue(true);
    }
}