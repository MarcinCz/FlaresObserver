package pl.mczerwi.flaresobserver.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-10.
 */
public class ParcelableIridiumFlare implements Parcelable {

    private final IridiumFlare flare;

    public ParcelableIridiumFlare(IridiumFlare flare) {
        this.flare = flare;
    }

    public IridiumFlare getIridiumFlare() {
        return flare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(flare.getDate());
        dest.writeInt(flare.getAltitude());
        dest.writeInt(flare.getAzimuth());
        dest.writeDouble(flare.getBrightness());
    }

    public static final Parcelable.Creator<ParcelableIridiumFlare> CREATOR
            = new Parcelable.Creator<ParcelableIridiumFlare>() {
        public ParcelableIridiumFlare createFromParcel(Parcel in) {
            return new ParcelableIridiumFlare(in);
        }

        public ParcelableIridiumFlare[] newArray(int size) {
            return new ParcelableIridiumFlare[size];
        }
    };

    private ParcelableIridiumFlare(Parcel in) {
        IridiumFlare.IridiumFlareBuilder builder = new IridiumFlare.IridiumFlareBuilder();
        builder.date((DateTime) in.readSerializable());
        builder.altitude(in.readInt());
        builder.azimuth(in.readInt());
        builder.brightness(in.readDouble());
        this.flare = builder.build();
    }
}
