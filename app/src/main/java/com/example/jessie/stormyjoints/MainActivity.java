package com.example.jessie.stormyjoints;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public boolean isAvailable = false;
    private CurrentWeather mCurrentWeather;

    // Creating view variables using butterknife api
    @BindView(R.id.tempTextView) TextView mTempTextView;
    @BindView(R.id.humidNumTextView) TextView mHumidNumTextView;
    @BindView(R.id.timeTextView) TextView mTimeTextView;
    @BindView(R.id.locationTextView) TextView mLocationTextView;
    @BindView(R.id.rainPercTextView) TextView mRainPercTextView;
    @BindView(R.id.summaryTextView) TextView mSummaryTextView;
    @BindView(R.id.weatherImageView) ImageView mWeatherImageView;
    @BindView(R.id.refreshImageView) ImageView mRefreshImageView;
    @BindView(R.id.refreshProgressBar) ProgressBar mRefreshProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Start ButterKnife Api
        ButterKnife.bind(this);

        // Hide the progress bar
        mRefreshProgressBar.setVisibility(View.INVISIBLE);

        final double lat = 37.8267;
        final double lon = -122.4233;


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(lat, lon);
            }
        });

        getForecast(lat,lon);

    }

    // Method to get forecast and display it properly
    private void getForecast(double lat, double lon) {

        // For reference: https://api.darksky.net/forecast/5c00b1c78c611027dcff3b6f26a4786d/37.8267,-122.4233
        // 37.8267,-122.4233
        // Key: 5c00b1c78c611027dcff3b6f26a4786d
        String apiKey = "5c00b1c78c611027dcff3b6f26a4786d";

        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/"
                + lat + "," + lon;


        if (networkAvailable()) {

            toggleRefreshView();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Because we are in the background thread because of the Callback
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefreshView();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Because we are in the background thread because of the Callback
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefreshView();
                        }
                    });

                    try {
                        //Log.v(TAG, response.body().string());
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception was caught: ", e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.Network_Down, Toast.LENGTH_LONG)
                    .show();
        }
    }

    // Toggle refresh view
    private void toggleRefreshView() {
        if (mRefreshProgressBar.getVisibility() == View.INVISIBLE){
            mRefreshProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mRefreshProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }


    }

   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateDisplay() {
        mTempTextView.setText(mCurrentWeather.getTemperature()+"");
        mHumidNumTextView.setText(mCurrentWeather.getHumidity()+"");
        mTimeTextView.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        mRainPercTextView.setText(mCurrentWeather.getPrecipChance() +"%");
        mSummaryTextView.setText((mCurrentWeather.getSummary()));

        //Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId(),null);
       // mWeatherImageView.setImageDrawable(drawable);

        mWeatherImageView.setImageResource(mCurrentWeather.getIconId());

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject currently = jsonObject.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimezone(jsonObject.getString("timezone"));


        return currentWeather;
    }


    private boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        // notes: when to create a new object and when to call a method on it
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        // the tag can be whatever I want it to be
        dialogFragment.show(getFragmentManager(),"error_dialog");

    }
}
