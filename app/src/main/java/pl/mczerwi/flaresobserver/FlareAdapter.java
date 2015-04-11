package pl.mczerwi.flaresobserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-04-11.
 */
public class FlareAdapter extends ArrayAdapter<IridiumFlare> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("d.mm hh:MM:ss");

    public FlareAdapter(Context context, List<IridiumFlare> flares) {
        super(context, R.layout.flare_row, flares);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.flare_row, null);

        IridiumFlare flare = getItem(position);

        TextView dateView = (TextView) view.findViewById(R.id.flare_row_date);
        dateView.setText(sdf.format(flare.getDate()));

        TextView brightnessView = (TextView) view.findViewById(R.id.flare_row_brightness);
        brightnessView.setText(String.valueOf(flare.getBrightness()));

        return view;
    }
}
