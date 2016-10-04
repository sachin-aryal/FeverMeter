package app.fevermeter.org.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import app.fevermeter.org.Adapter.FeverAdapter;
import app.fevermeter.org.Database.DatabaseHandler;
import app.fevermeter.org.Helper.HelperService;
import app.fevermeter.org.Model.Fever;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.fevermeter.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static android.R.attr.entries;

public class FeverReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fever_report);
        new FeverListFiller(this).execute("limited");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fever_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFever:
                Intent fever = new Intent(this,AddFeverActivity.class);
                startActivity(fever);
                return true;
            case R.id.feverReport:
                Intent feverReport = new Intent(this,FeverReportActivity.class);
                startActivity(feverReport);
                return true;
            case R.id.home:
                Intent home = new Intent(this,HomeActivity.class);
                startActivity(home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toggleFilterBar(View view){
        RelativeLayout relativeView = (RelativeLayout) findViewById(R.id.fever_report_scroll);
        if(relativeView.getVisibility()==View.GONE){
            relativeView.setVisibility(View.VISIBLE);
        }else{
            relativeView.setVisibility(View.GONE);
        }
    }

    public void feverListView(View view){
        ListView fever_report_list = (ListView) findViewById(R.id.fever_report_list);
        LineChart fever_report_graph = (LineChart) findViewById(R.id.fever_report_graph);
        fever_report_list.setVisibility(View.VISIBLE);
        fever_report_graph.setVisibility(View.GONE);
    }

    public void feverGraphView(View view){
        ListView fever_report_list = (ListView) findViewById(R.id.fever_report_list);
        LineChart fever_report_graph = (LineChart) findViewById(R.id.fever_report_graph);
        fever_report_graph.setVisibility(View.VISIBLE);
        fever_report_list.setVisibility(View.GONE);
    }

    public void filterData(View view){
        String startYear = ((Spinner) findViewById(R.id.startYear)).getSelectedItem().toString();
        String endYear = ((Spinner) findViewById(R.id.endYear)).getSelectedItem().toString();

        String startMonth = ((Spinner) findViewById(R.id.startMonth)).getSelectedItem().toString();
        String endMonth = ((Spinner) findViewById(R.id.endMonth)).getSelectedItem().toString();

        String startDay = ((Spinner) findViewById(R.id.startDay)).getSelectedItem().toString();
        String endDay = ((Spinner) findViewById(R.id.endDay)).getSelectedItem().toString();

        String startTime = ((Spinner) findViewById(R.id.startTime)).getSelectedItem().toString();
        String endTime = ((Spinner) findViewById(R.id.endTime)).getSelectedItem().toString();

        String startDate = startYear+"-"+startMonth+"-"+startDay+"-"+startTime;
        String endDate = endYear+"-"+endMonth+"-"+endDay+"-"+endTime;

        new FeverListFiller(this).execute("filter",startDate,endDate);

    }


    private class FeverListFiller extends AsyncTask<String,String,List<Fever>> {

        private Activity activity = null;

        FeverListFiller(Activity activity){
            this.activity = activity;
        }

        @Override
        protected List<Fever> doInBackground(String... params) {

            DatabaseHandler databaseHandler = new DatabaseHandler(activity);

            if(params[0].equalsIgnoreCase("limited")){
                return databaseHandler.getLimitData();
            }else if(params[0].equalsIgnoreCase("filter")){
                return databaseHandler.getData(params[1],params[2]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Fever> fevers) {
            super.onPostExecute(fevers);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ListView fever_list= (ListView) findViewById(R.id.fever_report_list);
                    FeverAdapter feverAdapter = new FeverAdapter(activity,fevers);
                    fever_list.setAdapter(fevers.size()!=0?feverAdapter:null);

                    LineChart lineChart = (LineChart) findViewById(R.id.fever_report_graph);
                    lineChart.setPinchZoom(true);
                    lineChart.setBackgroundColor(Color.TRANSPARENT);
                    lineChart.setHighlightPerDragEnabled(true);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelsToSkip(-1);
                    xAxis.setLabelRotationAngle(90);

                    YAxis yAxisRight = lineChart.getAxisRight();
                    yAxisRight.setEnabled(false);

                    YAxis yAxisLeft = lineChart.getAxisLeft();
                    yAxisLeft.setStartAtZero(false);

                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<>();
                    int index = 0;

                    for(Fever f:fevers){
                        if(index<10) {
                            entries.add(new Entry(Float.parseFloat(String.valueOf(f.getTemperature())), index++));
                            labels.add(HelperService.getFormatterFeverDate(f.getFeverDate()));
                        }
                    }

                    LineDataSet dataSet = new LineDataSet(entries, "Temperature");
                    dataSet.setDrawCircles(true);
                    dataSet.setCircleSize(5);
                    dataSet.setDrawCubic(true);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);



                    LineData data = new LineData(labels, dataSet);
                    lineChart.setData(data);

                    lineChart.setDescription("Fever Meter");
                    lineChart.invalidate();


                    //Setting Filter Parameters


                    if(fevers.size()>0) {
                        List<String> year_list = setYearSpinner();
                        List<String> month_list = new ArrayList<>();
                        month_list.add("Month");
                        List<String> day_list = new ArrayList<>();
                        day_list.add("Day");
                        List<String> time_list = new ArrayList<>();
                        time_list.add("Time");
                        for (int i = 1; i <= 12; i++) {
                            month_list.add(i + "");
                            time_list.add(i + " AM");
                        }
                        for (int i = 1; i <= 30; i++) {
                            day_list.add(i + "");
                        }
                        for (int i = 1; i <= 12; i++) {
                            time_list.add(i + " PM");
                        }

                        long startDateInMillis = fevers.get(fevers.size() - 1).getFeverDate();
                        long endDateInMillis = fevers.get(0).getFeverDate();

                        Spinner start_year = (Spinner) findViewById(R.id.startYear);
                        Spinner end_year = (Spinner) findViewById(R.id.endYear);
                        start_year.setSelection(year_list.indexOf(HelperService.getYearFromDate(startDateInMillis) + ""));
                        end_year.setSelection(year_list.indexOf(HelperService.getYearFromDate(endDateInMillis) + ""));


                        Spinner startMonth = (Spinner) findViewById(R.id.startMonth);
                        Spinner endMonth = (Spinner) findViewById(R.id.endMonth);
                        startMonth.setSelection(month_list.indexOf(HelperService.getMonthFromDate(startDateInMillis) + ""));
                        endMonth.setSelection(month_list.indexOf(HelperService.getMonthFromDate(endDateInMillis) + ""));

                        Spinner startDay = (Spinner) findViewById(R.id.startDay);
                        Spinner endDay = (Spinner) findViewById(R.id.endDay);
                        startDay.setSelection(day_list.indexOf(HelperService.getDayFromDate(startDateInMillis) + ""));
                        endDay.setSelection(day_list.indexOf(HelperService.getDayFromDate(endDateInMillis) + ""));

                        Spinner startTime = (Spinner) findViewById(R.id.startTime);
                        Spinner endTime = (Spinner) findViewById(R.id.endTime);
                        startTime.setSelection(time_list.indexOf(HelperService.getTimeFromDate(startDateInMillis) + ""));
                        endTime.setSelection(time_list.indexOf(HelperService.getTimeFromDate(endDateInMillis) + ""));

                    }

                }
            });
        }

        private List<String> setYearSpinner(){
            List<String> year_list = HelperService.getDynamicYearList();

            ArrayAdapter<String> year_adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item,year_list);

            Spinner start_year = (Spinner) findViewById(R.id.startYear);
            start_year.setAdapter(year_adapter);
            Spinner end_year = (Spinner) findViewById(R.id.endYear);
            end_year.setAdapter(year_adapter);
            return year_list;
        }

    }

}
