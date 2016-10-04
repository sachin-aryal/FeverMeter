package app.fevermeter.org.Helper;

import android.widget.Spinner;
import app.fevermeter.org.Database.DatabaseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        String feverTime = getTimeFromDate(feverDateInMillis);
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
        return calendar.get(Calendar.MONTH)+1;
    }

    public static int getDayFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static String getTimeFromDate(long feverDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(feverDateInMillis);

        if(calendar.get(Calendar.MINUTE)==24){
            return "12 PM";
        }
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

    public static String getActualTime(String feverTime) {
        if (feverTime.contains("AM")) {
            feverTime = feverTime.replace("AM", "").trim();
            return feverTime+":00:00";
        }
        feverTime = feverTime.replace("PM", "").trim();
        int hour = Integer.parseInt(feverTime)+12;
        if(hour==24){
           return hour+":24:00";
        }
        return hour+":00:00";
    }

    public static List<String> getDynamicYearList(){
        List<String> year_list = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1990;i<=year;i++){
            year_list.add(i+"");
        }
        year_list.add("Year");
        Collections.reverse(year_list);
        return year_list;
    }


    public static String buildDbQuery(String startDate,String endDate){

        String[] sDate = startDate.split("-");
        String[] eDate = endDate.split("-");

        String sYear = sDate[0];
        String sMonth = sDate[1];
        String sDay = sDate[2];
        String sTime = sDate[3];

        String eYear = eDate[0];
        String eMonth = eDate[1];
        String eDay = eDate[2];
        String eTime = eDate[3];

//        yyyy/MM/dd hh:mm:ss

        if(sYear.equals("Year")){
            return "SELECT *FROM "+ DatabaseHandler.TABLE_FEVER;
        }else if(sMonth.equals("Month")){
            startDate = sYear+"/1/1 01:00:00";
            endDate = eYear+"/12/30 11:24:00";
            long startDateInMillis = getTimeInMillis(startDate);
            long endDateInMillis = getTimeInMillis(endDate);

            return "SELECT *FROM "+DatabaseHandler.TABLE_FEVER+" WHERE "+
                    DatabaseHandler.KEY_FEVER_DATE +">="+ startDateInMillis+" AND "+
                    DatabaseHandler.KEY_FEVER_DATE +"<="+ endDateInMillis;
        }else if(sDay.equals("Day")){

            startDate = sYear+"/"+sMonth+"/01 01:00:00";
            endDate = eYear+"/"+eMonth+"/30 11:24:00";
            long startDateInMillis = getTimeInMillis(startDate);
            long endDateInMillis = getTimeInMillis(endDate);

            return "SELECT *FROM "+DatabaseHandler.TABLE_FEVER+" WHERE "+
                    DatabaseHandler.KEY_FEVER_DATE +">="+ startDateInMillis+" AND "+
                    DatabaseHandler.KEY_FEVER_DATE +"<="+ endDateInMillis;
        }else if(sTime.equals("Time")){
            startDate = sYear+"/"+sMonth+"/"+sDay+" 01:00:00";
            endDate = eYear+"/"+eMonth+"/"+eDay+ " 11:24:00";
            long startDateInMillis = getTimeInMillis(startDate);
            long endDateInMillis = getTimeInMillis(endDate);

            return "SELECT *FROM "+DatabaseHandler.TABLE_FEVER+" WHERE "+
                    DatabaseHandler.KEY_FEVER_DATE +">="+ startDateInMillis+" AND "+
                    DatabaseHandler.KEY_FEVER_DATE +"<="+ endDateInMillis;
        }else{
            startDate = sYear+"/"+sMonth+"/"+sDay+" "+getActualTime(sTime);
            endDate = eYear+"/"+eMonth+"/"+eDay+ " "+getActualTime(eTime);
            long startDateInMillis = getTimeInMillis(startDate);
            long endDateInMillis = getTimeInMillis(endDate);

            return "SELECT *FROM "+DatabaseHandler.TABLE_FEVER+" WHERE "+
                    DatabaseHandler.KEY_FEVER_DATE +">="+ startDateInMillis+" AND "+
                    DatabaseHandler.KEY_FEVER_DATE +"<="+ endDateInMillis;
        }

    }

}
