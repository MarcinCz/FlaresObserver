package pl.mczerwi.flarespredict.heavensabove;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlares;
import pl.mczerwi.flarespredict.IridiumFlaresPredictor;
import pl.mczerwi.flarespredict.altitude.AltitudeProvider;

public class HeavensAbovePredictor implements IridiumFlaresPredictor {

    private final static SimpleDateFormat sdf = new SimpleDateFormat(HeavensAboveConstants.DATE_FORMAT, Locale.ENGLISH);

    private final HeavensAboveScraper scraper;

    private final AltitudeProvider altitudeProvider;

    public HeavensAbovePredictor(HeavensAboveScraper scraper, AltitudeProvider altitudeProvider) {
        this.scraper = scraper;
        this.altitudeProvider = altitudeProvider;
    }

    @Override
    public IridiumFlares predict(double latitude, double longitude) {
        return predict(latitude, longitude, altitudeProvider.getAltitude(latitude, longitude));
    }

    @Override
    public IridiumFlares predict(final double latitude, final double longitude, final double altitude) {

        Document doc = scraper.getPage(getQueryParams(latitude, longitude));
        final List<IridiumFlare> iridiumFlares = parseRows(doc);

        return new IridiumFlares() {
            @Override
            public List<IridiumFlare> getFlares() {
                return iridiumFlares;
            }

            @Override
            public double getLatitude() {
                return latitude;
            }

            @Override
            public double getLongitude() {
                return longitude;
            }

            @Override
            public double getAltitude() {
                return altitude;
            }
        };
    }

    private Map<String, String> getQueryParams(double latitude, double longitude) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HeavensAboveConstants.LATITUDE_PARAM, String.valueOf(latitude));
        params.put(HeavensAboveConstants.LONGITUDE_PARAM, String.valueOf(longitude));
        params.put(HeavensAboveConstants.ALTITUDE_PARAM, "0");
        params.put(HeavensAboveConstants.TIMEZONE_PARAM, "UCT");
        return params;
    }

    private List<IridiumFlare> parseRows(Document doc) {
        final List<IridiumFlare> iridiumFlares = new ArrayList<IridiumFlare>();
        Elements rows = doc.select(".standardTable tr.clickableRow");
        for (Element row : rows) {
            iridiumFlares.add(parseRow(row));
        }

        return iridiumFlares;
    }

    private IridiumFlare parseRow(Element row) {
        try {
            final Date date = sdf.parse(row.child(0).text());
            final double brightness = Double.parseDouble(row.child(1).text());
            final int altitude = Integer.parseInt(row.child(2).text().replaceAll("[^0-9]", ""));
            final int azimuth = Integer.parseInt(row.child(3).text().replaceAll("[^0-9]", ""));
            return new IridiumFlare() {
                @Override
                public double getBrightness() {
                    return brightness;
                }

                @Override
                public int getAltitude() {
                    return altitude;
                }

                @Override
                public int getAzimuth() {
                    return azimuth;
                }

                @Override
                public Date getDate() {
                    return date;
                }
            };
        } catch (Throwable e) {
            throw new IllegalStateException("Error while parsing html row with flare information", e);
        }
    }
}
