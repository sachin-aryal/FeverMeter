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
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new FeverListFiller(this).execute();

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

    public void feverGraphView(View view){
        ListView feverList = (ListView) findViewById(R.id.home_fever_list);
        feverList.setVisibility(View.GONE);

        LineChart home_fever_graph = (LineChart) findViewById(R.id.home_fever_graph);
        home_fever_graph.setVisibility(View.VISIBLE);
    }

    public void feverListView(View view){
        ListView feverList = (ListView) findViewById(R.id.home_fever_list);
        feverList.setVisibility(View.VISIBLE);

        LineChart home_fever_graph = (LineChart) findViewById(R.id.home_fever_graph);
        home_fever_graph.setVisibility(View.GONE);
    }



    private class FeverListFiller extends AsyncTask<String,String,List<Fever>>{

        private Activity activity = null;

        FeverListFiller(Activity activity){
            this.activity = activity;
        }

        @Override
        protected List<Fever> doInBackground(String... params) {

            DatabaseHandler databaseHandler = new DatabaseHandler(activity);

            return databaseHandler.getLimitData();
        }

        @Override
        protected void onPostExecute(final List<Fever> fevers) {
            super.onPostExecute(fevers);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ListView fever_list= (ListView) findViewById(R.id.home_fever_list);
                    FeverAdapter feverAdapter = new FeverAdapter(activity,fevers);
                    fever_list.setAdapter(fevers.size()!=0?feverAdapter:null);


                    LineChart lineChart = (LineChart) findViewById(R.id.home_fever_graph);
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

                    // creating list of entry<br />
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<>();
                    int index = 0;

                    for(Fever f:fevers){
                        entries.add(new Entry(Float.parseFloat(String.valueOf(f.getTemperature())),index++));
                        labels.add(HelperService.getFormatterFeverDate(f.getFeverDate()));
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

                }
            });
        }
    }

}
