package app.fevermeter.org.Activity;

import android.app.Activity;
import android.content.Intent;
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
import app.fevermeter.org.Model.Fever;
import org.fevermeter.app.R;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView graph = (TextView) findViewById(R.id.home_fever_graph);
        graph.setVisibility(View.GONE);

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

    public void graphicalView(View view){
        ListView feverList = (ListView) findViewById(R.id.home_fever_list);
        feverList.setVisibility(View.GONE);

        TextView graph = (TextView) findViewById(R.id.home_fever_graph);
        graph.setVisibility(View.VISIBLE);
    }

    public void feverListView(View view){
        ListView feverList = (ListView) findViewById(R.id.home_fever_list);
        feverList.setVisibility(View.VISIBLE);

        TextView graph = (TextView) findViewById(R.id.home_fever_graph);
        graph.setVisibility(View.GONE);
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

                }
            });
        }
    }

}
