package app.fevermeter.org.Helper;

import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by iam on 9/22/16.
 */
public class HelperService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

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

    public static String getFormatterFeverDate(long feverDateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        String feverTime = SimpleDateFormat.getTimeInstance().format(calendar.getTime());
        String feverDate = SimpleDateFormat.getDateInstance().format(calendar.getTime());
        return feverDate+" "+feverTime;
    }

    public static int getYearFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonthFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static String getTimeFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        if(calendar.get(Calendar.AM_PM)==0) {
            return calendar.get(Calendar.HOUR) + " " + "AM";
        }
        return calendar.get(Calendar.HOUR) + " " + "PM";
    }


  /*  public static String getFormattedTime(int feverTime) {
        String formattedTime = " ";
        if (feverTime > 12) {
            return String.valueOf(24 - feverTime) + " PM";
        }
        return feverTime + " AM";
    }*/

    public static int getActualTime(String feverTime) {
        if (feverTime.contains("AM")) {
            feverTime = feverTime.replace("AM", "").trim();
            return Integer.parseInt(feverTime);
        }
        feverTime = feverTime.replace("PM", "").trim();
        return Integer.parseInt(feverTime)+12;
    }

    public static List<String> getDynamicYearList(){
        List<String> year_list = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        year_list.add("Year");
        for(int i=1990;i<=year;i++){
            year_list.add(i+"");
        }
        return year_list;
    }
}
