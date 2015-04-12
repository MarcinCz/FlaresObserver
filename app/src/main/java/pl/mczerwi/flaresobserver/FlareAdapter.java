package pl.mczerwi.flaresobserver;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-04-11.
 */
public class FlareAdapter extends RecyclerView.Adapter<FlareAdapter.ViewHolder> {

    private final DateTimeFormatter dateDayFormatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZone(DateTimeZone.getDefault());
    private final DateTimeFormatter dateHourFormatter = DateTimeFormat.forPattern("HH:mm:ss").withZone(DateTimeZone.getDefault());
    private final List<IridiumFlare> flares;

    public FlareAdapter(List<IridiumFlare> flares) {
        this.flares = flares;
    }

    @Override
    public FlareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flare_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        IridiumFlare flare = flares.get(position);
        viewHolder.mAzimuthTextView.setText(String.valueOf(flare.getAzimuth()));
        viewHolder.mAltitudeTextView.setText(String.valueOf(flare.getAltitude()));
        viewHolder.mBrightnessTextView.setText(String.valueOf(flare.getBrightness()));
        viewHolder.mDateDayTextView.setText(dateDayFormatter.print(flare.getDate()));
        viewHolder.mDateHourTextView.setText(dateHourFormatter.print(flare.getDate()));
    }

    @Override
    public int getItemCount() {
        return flares.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mBrightnessTextView;
        public TextView mDateHourTextView;
        public TextView mDateDayTextView;
        public TextView mAltitudeTextView;
        public TextView mAzimuthTextView;

        public ViewHolder(View view) {
            super(view);

            mBrightnessTextView = (TextView) view.findViewById(R.id.flare_row_brightness);
            mDateHourTextView = (TextView) view.findViewById(R.id.flare_row_date_hour);
            mDateDayTextView = (TextView) view.findViewById(R.id.flare_row_date_day);
            mAltitudeTextView = (TextView) view.findViewById(R.id.flare_row_altitude);
            mAzimuthTextView = (TextView) view.findViewById(R.id.flare_row_azimuth);
        }
    }
}
