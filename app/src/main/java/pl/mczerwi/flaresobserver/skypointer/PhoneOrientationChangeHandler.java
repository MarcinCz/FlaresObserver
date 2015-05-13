package pl.mczerwi.flaresobserver.skypointer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-13.
 */
class PhoneOrientationChangeHandler implements SensorEventListener {

    private final TextView mTextView;
    private final IridiumFlare mIridiumFlare;
    private final ImageView mArrowCircle;
    private int mCurrentCircleArrow;
    private PhoneOrientation mLastPhoneOrientation;
    private float[] mGravity;
    private float[] mGeomagnetic;

    public PhoneOrientationChangeHandler(View view, IridiumFlare flare) {
        mTextView = (TextView) view.findViewById(R.id.skypointertext);
        mArrowCircle = (ImageView) view.findViewById(R.id.sky_pointer_arrow_circle);
        mIridiumFlare = flare;
        mCurrentCircleArrow = R.drawable.arrow_circle_red;
    }

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

        if(!isChangeBigEnough(phoneOrientation)) {
            return;
        }

        double altitudeDifference = mIridiumFlare.getAltitude() - phoneOrientation.getAltitude();
        double azimuthDifference = SphereHelper.getAzimuthDifference(mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());
        double distanceToFlare = SphereHelper.getGreatCircleDistance(mIridiumFlare.getAltitude(), phoneOrientation.getAltitude(), mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());

        adjustArrowCircleGraphic(distanceToFlare);

        double angle = Math.toDegrees(Math.atan2(azimuthDifference, altitudeDifference));
        mTextView.setText(String.format("Orientation:\n [0] %s\n [1] %s\n [2] %s", phoneOrientation.getAltitude(), phoneOrientation.getAzimuth(), String.valueOf(distanceToFlare)));
        mArrowCircle.setRotation((float) angle);

    }

    private boolean isChangeBigEnough(PhoneOrientation phoneOrientation) {
        if(mLastPhoneOrientation == null
                || Math.abs(phoneOrientation.getAltitude() - mLastPhoneOrientation.getAltitude()) > 1
                || Math.abs(phoneOrientation.getAzimuth() - mLastPhoneOrientation.getAzimuth()) > 2) {
            mLastPhoneOrientation = phoneOrientation;
            return true;
        } else {
            return false;
        }
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
}
