package pl.mczerwi.flaresobserver.settings;

/**
 * Created by marcin on 2015-05-18.
 */
public interface NotificationSettings {
    boolean isShowNotifications();
    boolean isDoNotShowConstrainsEnabled();
    int getDoNotShowStartHour();
    int getDoNotShowEndHour();
    boolean isBrightnessLimitEnabled();
    double getBrightnessLimit();
}
