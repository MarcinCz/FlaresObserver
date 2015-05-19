package pl.mczerwi.flaresobserver.settings;

public interface NotificationSettings {
    boolean isShowNotifications();
    boolean isDoNotShowConstrainsEnabled();
    int getDoNotShowStartHour();
    int getDoNotShowEndHour();
    boolean isBrightnessLimitEnabled();
    int getMinutesAhead();
}
