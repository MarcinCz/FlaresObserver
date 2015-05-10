package pl.mczerwi.flaresobserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-04-11.
 */
public class FlareAdapter extends ArrayAdapter<IridiumFlare> {

    private final DateTimeFormatter dateDayFormatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZone(DateTimeZone.getDefault());
    private final DateTimeFormatter dateHourFormatter = DateTimeFormat.forPattern("HH:mm:ss").withZone(DateTimeZone.getDefault());
    private final List<IridiumFlare> flares;

    public FlareAdapter(Context context, int textViewResourceId, List<IridiumFlare> objects) {
        super(context, textViewResourceId, objects);
        this.flares = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.flare_row, null);

            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        IridiumFlare flare = flares.get(position);
        viewHolder.mAzimuthTextView.setText(String.valueOf(flare.getAzimuth()));
        viewHolder.mAltitudeTextView.setText(String.valueOf(flare.getAltitude()));
        viewHolder.mBrightnessTextView.setText(String.valueOf(flare.getBrightness()));
        viewHolder.mDateDayTextView.setText(dateDayFormatter.print(flare.getDate()));
        viewHolder.mDateHourTextView.setText(dateHourFormatter.print(flare.getDate()));
        return view;

    }


    private static class ViewHolder {
        public TextView mBrightnessTextView;
        public TextView mDateHourTextView;
        public TextView mDateDayTextView;
        public TextView mAltitudeTextView;
        public TextView mAzimuthTextView;

        private final View view;

        public ViewHolder(View view) {
            this.view = view;
            mBrightnessTextView = (TextView) view.findViewById(R.id.flare_row_brightness);
            mDateHourTextView = (TextView) view.findViewById(R.id.flare_row_date_hour);
            mDateDayTextView = (TextView) view.findViewById(R.id.flare_row_date_day);
            mAltitudeTextView = (TextView) view.findViewById(R.id.flare_row_altitude);
            mAzimuthTextView = (TextView) view.findViewById(R.id.flare_row_azimuth);
        }
    }
}
