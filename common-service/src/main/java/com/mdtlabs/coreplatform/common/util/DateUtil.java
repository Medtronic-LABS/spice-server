package com.mdtlabs.coreplatform.common.util;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This is used to calculate the past day from particular date
 * </p>
 *
 * @author AkashGopinath Created on 25 Aug 2022
 */
public class DateUtil {

    private DateUtil() {
    }

    /**
     * <p>
     * this is used to calculate days since particular date
     * </p>
     *
     * @param date  input date
     * @return long  days since past
     */
    public static long daysSincePast(Date date) {
        if (null != date) {
            LocalDate dateBefore = getZonedDateTime(date).toLocalDate();
            LocalDate dateAfter = getZonedDateTime(new Date()).toLocalDate();
            return ChronoUnit.DAYS.between(dateBefore, dateAfter);
        }
        return Constants.ZERO;
    }

    /**
     * <p>
     * This method is used to calculate year since particular date in string.
     * </p>
     *
     * @param dateString  input date string
     * @return int  years since past input date
     */
    public static int yearsSincePast(String dateString) {
        if (StringUtils.isNotBlank(dateString)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMATTER);
            LocalDate dateTimeDob = LocalDate.parse(dateString, formatter);
            return Period.between(dateTimeDob, LocalDate.now()).getYears();
        }
        return Constants.ZERO;
    }

    /**
     * <p>
     * This method is used to calculate year since particular date.
     * </p>
     *
     * @param date input date
     * @return int  years since past
     */
    public static int yearsSincePast(Date date) {
        if (null != date) {
            LocalDate dateBefore = getZonedDateTime(date).toLocalDate();
            LocalDate dateAfter = getZonedDateTime(new Date()).toLocalDate();
            return Period.between(dateBefore, dateAfter).getYears();
        }
        return Constants.ZERO;
    }

    /**
     * <p>
     * This method is used to get zonedDateTime.
     * </p>
     *
     * @param date  input date
     * @return ZonedDateTime  zoned date time response
     */
    public static ZonedDateTime getZonedDateTime(Date date) {
        Instant timeStamp = date.toInstant();
        String zoneId = String.valueOf(ZoneId.of(FieldConstants.UTC));
        return timeStamp.atZone(ZoneId.of(zoneId));
    }

    /**
     * <p>
     * This method is used to format date.
     * </p>
     *
     * @param dateStr    input date
     * @param formatStr  format type
     * @return Date  converted date into specified format
     */
    public static Date formatDate(String dateStr, String formatStr) {
        DateFormat df;
        Date date = null;
        try {
            if (!CommonUtil.isNull(dateStr, formatStr)) {
                df = new SimpleDateFormat(formatStr);
                date = df.parse(dateStr);
                date = new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
            }
        } catch (ParseException ex) {
            return null;
        }
        return date;
    }

    /**
     * <p>
     * This method is used to format date on Date.
     * </p>
     *
     * @param date  date to be formatted
     * @return Date  formatted date
     */
    public static Date formatDate(Date date) {
        return !CommonUtil.isNull(date) ? formatDate(StringUtil.getDateString(date), Constants.JSON_DATE_FORMAT) : null;
    }

    /**
     * <p>
     * Used to get the two dates difference days / month / year.
     * </p>
     *
     * @param fromDate  start date
     * @param toDate    end date
     * @param type      type of difference
     * @return int  difference in date
     */
    public static int getCalendarDiff(Date fromDate, Date toDate, int type) {
        Calendar fromDateCalendar = Calendar.getInstance();
        Calendar toDateCalendar = Calendar.getInstance();
        fromDateCalendar.setTime(fromDate);
        toDateCalendar.setTime(toDate);
        if (!CommonUtil.isNull(fromDate, toDate)) {
            return (toDateCalendar.get(type) - fromDateCalendar.get(type));
        }
        return Constants.ZERO;
    }

    /**
     * <p>
     * This method is used to subtract particular value in date
     * </p>
     *
     * @param date            from date
     * @param daysToSubtract  count to be subtracted
     * @return Date  subtracted date
     */
    public static Date subtractDates(Date date, int daysToSubtract) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -daysToSubtract);
        return cal.getTime();
    }

    /**
     * <p>
     * This method is used to get current day
     * </p>
     *
     * @return date  current day
     */
    public static Date getCurrentDay() {
        return new Date();
    }

    /**
     * <p>
     * Used to get the two dates difference in hours.
     * </p>
     *
     * @param fromDate  start date
     * @param toDate    end date
     * @return long  difference in hours
     */
    public static long getDiffInHours(Date fromDate, Date toDate) {
        long difference = fromDate.getTime() - toDate.getTime();
        return TimeUnit.MILLISECONDS.toHours(difference) % Constants.TWENTY_FOUR;
    }

    public static String getISOString(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(Constants.JSON_DATE_FORMAT); // Quoted "Z" to indicate UTC, no timezone
        // offset
        df.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_UTC));
        return df.format(calendar.getTime());
    }

    /**
     * <p>
     * Used to add the date.
     * </p>
     *
     * @param date      givenDate
     * @param daysToAdd number of days to be added
     * @return Date
     */
    public static Date addDate(Date date, int daysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, daysToAdd);
        return cal.getTime();
    }

    public static long getDateDiffInMinutes(Date dateFrom, Date dateTo) {

        if (null == dateFrom || null == dateTo) {
            return 0;
        }

        long differenceInTime = dateTo.getTime() - dateFrom.getTime();

        long differenceInYears = TimeUnit.MILLISECONDS.toDays(differenceInTime) / 365;
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime) % 365;
        long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInTime) % 24;
        long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInTime) % 60;

        return ((differenceInYears * 365 * 24 * 60) + (differenceInDays * 24 * 60) + (differenceInHours * 60)
                + differenceInMinutes);
    }

    /**
     * <p>
     * Calculates the next date for the treatment plan.
     * </p>
     *
     * @return date next treatment review date
     * @author Niraimathi S
     */
    public static Date getTreatmentPlanFollowupDate(String period, Integer duration) {
        Calendar calendar = Calendar.getInstance();
        Date date;
        if (Constants.DAY.equalsIgnoreCase(period)) {
            calendar.add(Calendar.DATE, duration);
            date = calendar.getTime();
        } else if (Constants.WEEK.equalsIgnoreCase(period)) {
            calendar.add(Calendar.WEEK_OF_YEAR, duration);
            date = calendar.getTime();
        } else {
            calendar.add(Calendar.MONTH, duration);
            date = calendar.getTime();
        }

        return date;
    }

    /**
     * <p>
     * Used to get the start date based on UTC.
     * </p>
     *
     * @return Date
     */
    public static String getStartOfDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return DateUtil.getISOString(calendar);
    }

    /**
     * <p>
     * Used to get the end date based on UTC.
     * </p>
     *
     * @return Date
     */
    public static String getEndOfDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return DateUtil.getISOString(calendar);
    }

    /**
     * Used to check if the mentioned two dates are same or not.
     *
     * @param fromDate - start date
     * @param toDate   - end date
     * @param type     - type of difference
     * @return boolean - true or false based on the condition
     */
    public static boolean isSameDate(Date fromDate, Date toDate, int type) {
        Calendar fromDateCalendar = Calendar.getInstance();
        Calendar toDateCalendar = Calendar.getInstance();
        fromDateCalendar.setTime(fromDate);
        toDateCalendar.setTime(toDate);
        return (toDateCalendar.get(type) == fromDateCalendar.get(type));
    }

    /**
     * Used to get the two dates difference days / month / year.
     *
     * @param fromDate - start date
     * @param toDate   - end date
     * @return int - difference in date
     */
    public static int getCalendarDiff(Date fromDate, Date toDate) {
        Instant fromDateInstant = fromDate.toInstant();
        Instant toDateInstant = toDate.toInstant();
        if (!CommonUtil.isNull(fromDate, toDate)) {
            Long noOfDaysLong = ChronoUnit.DAYS.between(fromDateInstant, toDateInstant);
            return noOfDaysLong.intValue();
        }
        return Constants.ZERO;
    }

}
