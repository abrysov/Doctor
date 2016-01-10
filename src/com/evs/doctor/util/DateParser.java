package com.evs.doctor.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.res.Resources;

import com.evs.doctor.R;

public class DateParser {

    private final static String DATE_FORMAT_PATTER = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private final static String SERVER_OFFSET = "+4";
    private Calendar calendarCurrent = Calendar.getInstance();
    private Calendar calendarReceived;
    private static Resources mResources;

    private DateParser() {

    }

    public static void init(Resources res) {
        mResources = res;
    }

    public static String parseDate(String strDate) {
        return new DateParser().getDate(strDate);
    }

    /**
     * Constructs and returns a String based on a date which is received from the Server in format
     * "yyyy-MM-dd'T'HH:mm:ss.SSSZ". If event happened today returns a String like
     * "Today 2 hours 23 minutes ago". If event happened yesterday returns a String like
     * "Yesterday 13:27". If event happened nor today neither yesterday returns a String like
     * "17/10/2010 at 16:20".
     *
     * @return String
     */
    public String getDate(String receivedStringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTER);
        sdf.parse(receivedStringDate, new ParsePosition(0));
        sdf.setTimeZone(TimeZone.getTimeZone(SERVER_OFFSET));
        String localOffset = Integer.toString(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 3600000);
        calendarCurrent.setTimeZone(TimeZone.getTimeZone(localOffset));
        calendarReceived = sdf.getCalendar();

        String result = null;
        int currentHour = calendarCurrent.get(Calendar.HOUR_OF_DAY);
        int receivedHour = calendarReceived.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendarCurrent.get(Calendar.MINUTE);
        int receivedMin = calendarReceived.get(Calendar.MINUTE);

        if (isHappenedToday()) {
            int hoursAgo = 0;
            int minsAgo = 0;

            hoursAgo = calendarCurrent.get(Calendar.HOUR_OF_DAY) - calendarReceived.get(Calendar.HOUR_OF_DAY);

            if (receivedHour == currentHour) {
                if (receivedMin == currentMin) {
                    return mResources.getString(R.string.just_now);
                } else {
                    minsAgo = currentMin - receivedMin;
                    return result = mResources.getString(R.string.today) + " " + minsAgo
                            + mResources.getString(R.string.minutes) + " " + mResources.getString(R.string.ago);
                }
            } else {
                if (receivedMin == currentMin) {
                    hoursAgo = currentHour - receivedHour;
                    return result = mResources.getString(R.string.today) + " " + hoursAgo
                            + mResources.getString(R.string.hours) + " " + mResources.getString(R.string.ago);
                } else {
                    minsAgo = (60 - receivedMin) + currentMin;
                    hoursAgo = currentHour - receivedHour;

                    if (minsAgo / 60 >= 1) {
                        hoursAgo += (minsAgo / 60);
                        minsAgo = minsAgo % 60;
                    }
                }
            }

            if (hoursAgo == 0) {
                return result = mResources.getString(R.string.today) + " " + minsAgo
                        + mResources.getString(R.string.minutes) + " " + mResources.getString(R.string.ago);
            }

            if (minsAgo == 0) {
                return result = mResources.getString(R.string.today) + " " + hoursAgo
                        + mResources.getString(R.string.hours) + " " + mResources.getString(R.string.ago);
            }

            return result = mResources.getString(R.string.today) + " " + hoursAgo
                    + mResources.getString(R.string.hours) + " " + minsAgo + mResources.getString(R.string.minutes)
                    + " " + mResources.getString(R.string.ago);
        } else if (isHappenedYesterday()) {
            if (receivedMin < 10) {
                return result = mResources.getString(R.string.yesterday) + " " + mResources.getString(R.string.at) + " "
                        + receivedHour + ":" + "0" + receivedMin;
            }

            return result = mResources.getString(R.string.yesterday) + " " + mResources.getString(R.string.at) + " "
                    + receivedHour + ":" + receivedMin;
        }
        // Event happened nor today neither yesterday:
        if (calendarReceived.get(Calendar.MINUTE) < 10) {
            return result = calendarReceived.get(Calendar.DATE) + "/" + calendarReceived.get(Calendar.MONTH) + "/"
                    + calendarReceived.get((Calendar.YEAR)) + " " + mResources.getString(R.string.at) + " "
                    + calendarReceived.get(Calendar.HOUR_OF_DAY) + ":" + "0" + calendarReceived.get(Calendar.MINUTE);
        }

        return result = calendarReceived.get(Calendar.DATE) + "/" + calendarReceived.get(Calendar.MONTH) + "/"
                + calendarReceived.get((Calendar.YEAR)) + " " + mResources.getString(R.string.at) + " "
                + calendarReceived.get(Calendar.HOUR_OF_DAY) + ":" + calendarReceived.get(Calendar.MINUTE);
    }

    private boolean isHappenedToday() {
        if (calendarCurrent.get(Calendar.YEAR) == calendarReceived.get(Calendar.YEAR)
                && calendarCurrent.get(Calendar.MONTH) == calendarReceived.get(Calendar.MONTH)
                && calendarCurrent.get(Calendar.DAY_OF_MONTH) == calendarReceived.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isHappenedYesterday() {
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.add(Calendar.DAY_OF_MONTH, -1);
        Calendar calendarPreviousMonth = Calendar.getInstance();
        calendarPreviousMonth.add(Calendar.MONTH, -1);

        if (calendarCurrent.get(Calendar.YEAR) != calendarReceived.get(Calendar.YEAR)) {
            return false;
        }

        if (calendarCurrent.get(Calendar.DATE) == 1
                && calendarPreviousMonth.getActualMaximum(Calendar.DAY_OF_MONTH) == calendarReceived
                        .get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            if (calendarCurrent.get(Calendar.YEAR) == calendarReceived.get(Calendar.YEAR)
                    && calendarCurrent.get(Calendar.MONTH) == calendarReceived.get(Calendar.MONTH)
                    && calendarYesterday.get(Calendar.DAY_OF_MONTH) == calendarReceived.get(Calendar.DAY_OF_MONTH)) {
                return true;
            } else {
                return false;
            }
        }
    }

}
