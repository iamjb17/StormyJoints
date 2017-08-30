package com.example.jessie.stormyjoints;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jessie on 8/7/2017.
 * Creating a current weather object to display properly
 */

public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimezone;
    private int mIconId;
    private String mLocation;

    public void setIconId(int iconId) {
        mIconId = iconId;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
        // clear-day, clear-night, rain, snow, sleet, wind, fog,
        // cloudy, partly-cloudy-day, or partly-cloudy-night

        mIconId = R.drawable.clear_day;

        if (mIcon.equals("clear-day")) {
            mIconId = R.drawable.clear_day;
        } else if (mIcon.equals("clear-night")) {
            mIconId = R.drawable.clear_night;
        } else if (mIcon.equals("rain")) {
            mIconId = R.drawable.rain;
        } else if (mIcon.equals("snow")) {
            mIconId = R.drawable.snow;
        } else if (mIcon.equals("sleet")) {
            mIconId = R.drawable.sleet;
        } else if (mIcon.equals("wind")) {
            mIconId = R.drawable.wind;
        } else if (mIcon.equals("fog")) {
            mIconId = R.drawable.fog;
        } else if (mIcon.equals("cloudy")) {
            mIconId = R.drawable.cloudy;
        } else if (mIcon.equals("partly-cloudy-day")) {
            mIconId = R.drawable.partly_cloudy;
        } else if (mIcon.equals("partly-cloudy-night")) {
            mIconId = R.drawable.cloudy_night;
        }

        return mIconId;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(mTime * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getTemperature() {

        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 100;
        return (int) Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
