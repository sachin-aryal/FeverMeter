package app.fevermeter.org.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by iam on 9/22/16.
 */
public class HelperService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static long getTimeInMillis(String feverDate) {
        Date date = null;
        try {
            date = sdf.parse(feverDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getFeverDate(long feverDateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        return sdf.format(calendar.getTime());
    }

    public static String getFormattedTime(int feverTime) {
        String formattedTime = " ";
        if (feverTime > 12) {
            return String.valueOf(24 - feverTime) + " PM";
        }
        return feverTime + " AM";
    }

    public static int getActualTime(String feverTime) {
        if (feverTime.contains("AM")) {
            feverTime = feverTime.replace("AM", "").trim();
            return Integer.parseInt(feverTime);
        }
        feverTime = feverTime.replace("PM", "").trim();
        return Integer.parseInt(feverTime + 12);
    }
}
