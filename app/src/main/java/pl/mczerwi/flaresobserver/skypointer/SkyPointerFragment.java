package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-11.
 */
public class SkyPointerFragment extends Fragment implements SensorEventListener {

    public final static String TAG = "SkyPointerFragment";

    private final static String FLARE = "flare";

    private TextView mTextView;
    private IridiumFlare mIridiumFlare;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private ImageView mArrowCircle;
    private int mCurrentCircleArrow;

    public static SkyPointerFragment newInstance(IridiumFlare flare) {
        SkyPointerFragment instance = new SkyPointerFragment();
        Bundle args = new Bundle();
        args.putParcelable(FLARE, new ParcelableIridiumFlare(flare));
        instance.setArguments(args);

        return instance;
    }

    public SkyPointerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skypointer, container, false);

        mIridiumFlare = ((ParcelableIridiumFlare) getArguments().getParcelable(FLARE)).getIridiumFlare();
        mTextView = (TextView) view.findViewById(R.id.skypointertext);
        mArrowCircle = (ImageView) view.findViewById(R.id.sky_pointer_arrow_circle);
        mCurrentCircleArrow = R.drawable.arrow_circle_green;
        return view;
    }

    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, null, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                float remapMatrix[] = new float[9];
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, remapMatrix);
                SensorManager.getOrientation(remapMatrix, orientation);
                onPhoneOrientationChanged(new PhoneOrientation(orientation));
            }
        }
    }

    private void onPhoneOrientationChanged(PhoneOrientation phoneOrientation) {
        double altitudeDifference = mIridiumFlare.getAltitude() - phoneOrientation.getAltitude();
        double azimuthDifference = SphereHelper.getAzimuthDifference(mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());
        double distanceToFlare = SphereHelper.getGreatCircleDistance(mIridiumFlare.getAltitude(), phoneOrientation.getAltitude(), mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());

        adjustArrowCircleGraphic(distanceToFlare);

        double angle = Math.toDegrees(Math.atan2(azimuthDifference, altitudeDifference));
        mTextView.setText(String.format("Orientation:\n [0] %s\n [1] %s\n [2] %s", phoneOrientation.getAltitude(), phoneOrientation.getAzimuth(), String.valueOf(distanceToFlare)));
        mArrowCircle.setRotation((float) angle);

    }

    private void adjustArrowCircleGraphic(double distanceToFlare) {
        if(distanceToFlare > SphereHelper.SPHERE_BIG_DISTANCE) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_red);
        } else if(distanceToFlare > SphereHelper.SPHERE_MEDIUM_DISTANCE ) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_orange);
        } else if(distanceToFlare > SphereHelper.SPHERE_SMALL_DISTANCE) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_yellow);
        } else {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_green);
        }
    }

    private void changeArrowCircleIfNecessary(int arrowCircle) {
        if(mCurrentCircleArrow != arrowCircle) {
            mArrowCircle.setImageResource(arrowCircle);
            mCurrentCircleArrow = arrowCircle;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    public void onSensorChanged(SensorEvent event) {
//        int sensor = event.type;
//        float[] values = event.values;
//        int i;
//        StringBuffer str=new StringBuffer();
//        // do something with the sensor data
//        TextView text = (TextView) findViewById(R.id.my_text);
//        float[] R = new float[9]; // rotation matrix
//        float[] magnetic = new float[3];
//        float[] orientation = new float[3];
//
//        magnetic[0]=0;
//        magnetic[1]=1;
//        magnetic[2]=0;
//
//        str.append("From Sensor :\n");
//        for(i=0;i<values.length; i++) {
//            str.append(values[i]);
//            str.append(", ");
//        }
//
//        SensorManager.getRotationMatrix(R, null, values, magnetic);
//        SensorManager.getOrientation(R, orientation);
//
//
//        str.append("\n\nGives :\n");
//        for(i=0;i<orientation.length; i++) {
//            str.append(orientation[i]);
//            str.append(", ");
//        }
//        text.setText(str);
//    }

}
