package app.fevermeter.org.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.fevermeter.org.Helper.HelperService;
import app.fevermeter.org.Model.Fever;
import org.fevermeter.app.R;

import java.util.List;

/**
 * Created by iam on 9/17/16.
 */
public class FeverAdapter extends ArrayAdapter<Fever>{

    public FeverAdapter(Context context, List<Fever> feverList) {
        super(context, 0,feverList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fever_list, parent, false);
        }

        TextView feverDateText = (TextView) convertView.findViewById(R.id.feverDate);
        TextView temperatureText = (TextView) convertView.findViewById(R.id.temperature);

        Fever fever = getItem(position);

        String feverDate = HelperService.getFeverDate(fever.getFeverDate());

        int time = fever.getFeverTime();
        feverDate+=" "+HelperService.getFormattedTime(time);

        feverDateText.setText(feverDate);
        temperatureText.setText(fever.getTemperature()+" F");

        return convertView;
    }
}
