package app.fevermeter.org.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import app.fevermeter.org.Database.DatabaseHandler;
import app.fevermeter.org.Helper.HelperService;
import app.fevermeter.org.Model.Fever;
import org.fevermeter.app.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class AddFeverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fever);
        setYearSpinner();
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
                Intent fever = new Intent(this, AddFeverActivity.class);
                startActivity(fever);
                return true;
            case R.id.feverReport:
                Intent feverReport = new Intent(this, FeverReportActivity.class);
                startActivity(feverReport);
                return true;
            case R.id.home:
                Intent home = new Intent(this, HomeActivity.class);
                startActivity(home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addFever(View view) {


        EditText temperatureText = (EditText) findViewById(R.id.temperature);
        Spinner yearSpinner = (Spinner) findViewById(R.id.yearList);
        Spinner monthSpinner = (Spinner) findViewById(R.id.monthList);
        Spinner daySpinner = (Spinner) findViewById(R.id.dayList);
        Spinner timeSpinner = (Spinner) findViewById(R.id.timeList);

        double temperature = Double.parseDouble(temperatureText.getText().toString());
        int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
        int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
        String timeText = timeSpinner.getSelectedItem().toString();
        String time = HelperService.getActualTime(timeText);

        System.out.println("Adding Time:"+time);

        String feverDate = year + "/" + month + "/" + day + " " + time;

        Fever fever = new Fever(temperature, HelperService.getTimeInMillis(feverDate));

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.addFever(fever);
    }

    private void setYearSpinner() {
        List<String> year_list = HelperService.getDynamicYearList();

        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year_list);

        Spinner year_spinner = (Spinner) findViewById(R.id.yearList);
        year_spinner.setAdapter(year_adapter);
    }
}