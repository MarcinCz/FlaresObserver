package pl.mczerwi.flaresobserver.flares;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-04-11.
 */
class FlaresAdapter extends ArrayAdapter<IridiumFlare> {

    private final DateTimeFormatter mDateDayFormatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZone(DateTimeZone.getDefault());
    private final DateTimeFormatter mDateHourFormatter = DateTimeFormat.forPattern("HH:mm:ss").withZone(DateTimeZone.getDefault());
    private final List<IridiumFlare> mFlares;
    private final Context mContext;

    public FlaresAdapter(Context context, int textViewResourceId, List<IridiumFlare> objects) {
        super(context, textViewResourceId, objects);
        this.mFlares = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_flares_row, null);

            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        IridiumFlare flare = mFlares.get(position);
        viewHolder.mAzimuthTextView.setText(String.valueOf(flare.getAzimuth()) + "°");
        viewHolder.mAltitudeTextView.setText(String.valueOf(flare.getAltitude()) + "°");
        viewHolder.mBrightnessTextView.setText(String.valueOf(flare.getBrightness()));
        viewHolder.mDateDayTextView.setText(mDateDayFormatter.print(flare.getDate()));
        viewHolder.mDateHourTextView.setText(mDateHourFormatter.print(flare.getDate()));

        GradientDrawable bgShape = (GradientDrawable)viewHolder.mBrightnessTextView.getBackground();
        bgShape.setColor(getColorBasedOnBrightness(flare.getBrightness()));
        return view;

    }

    private int getColorBasedOnBrightness(double brightness) {
        int color;
        if(brightness > 0) {
            color = R.color.blue_grey_background_yellow;
        } else if (brightness > -2) {
            color = R.color.blue_grey_background_amber;
        } else if (brightness > -4) {
            color = R.color.blue_grey_background_orange;
        } else if (brightness > -6) {
            color = R.color.blue_grey_background_deep_orange;
        } else {
            color = R.color.blue_grey_background_red;
        }
        return mContext.getResources().getColor(color);
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
