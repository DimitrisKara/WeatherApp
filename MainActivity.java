package com.example.farmweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.idListView);

        URL weatherUrl = NetworkUtils.buildUrlForWeather();
        new FetchWeatherDetails().execute(weatherUrl);
        Log.i(TAG, "onCreate: weatherUrl: " + weatherUrl);

    }

    private class FetchWeatherDetails extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherURL = urls[0];
            String weatherSearchResults = null;

            try {
                weatherSearchResults = NetworkUtils.getResponseFromHttpUrl(weatherURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "doInBackground: weatherSearchResults: " + weatherSearchResults);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
            if(weatherSearchResults != null && !weatherSearchResults.equals("")) {
                weatherArrayList = parseJSON(weatherSearchResults);
                Iterator itr = weatherArrayList.iterator();
                while (itr.hasNext()) {
                    Weather weatherInIterator = (Weather) itr.next();
                    Log.i(TAG, "onPostExecute:  Date " + weatherInIterator.getDate() +
                            "Min: " + weatherInIterator.getMinTemp() +
                            "Max: " + weatherInIterator.getMaxTemp() +
                            "Link: " + weatherInIterator.getLink());
                }
            }

            super.onPostExecute(weatherSearchResults);
        }
    }

    private ArrayList<Weather> parseJSON(String weatherSearchResults) {
        if(weatherArrayList != null) {
            weatherArrayList.clear();
        }

        if (weatherSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(weatherSearchResults);
                JSONArray results = rootObject.getJSONArray("DailyForecasts");

                for (int i = 0; i < results.length(); i++) {
                    Weather weather = new Weather();

                    JSONObject resultsObj = results.getJSONObject(i);

                    String date = resultsObj.getString("date");
                    weather.setDate(date);

                    Log.i(TAG, "parseJSON: date: " +date);

                    JSONObject temperatureObj =  resultsObj.getJSONObject("Temperature");
                    String minTemperature = temperatureObj.getJSONObject("Minimum").getString("Value");
                    weather.setMinTemp(minTemperature);

                    Log.i(TAG, "parseJSON: minTemperature: "+ minTemperature);

                    String maxTemperature = temperatureObj.getJSONObject("Maximum").getString("Value");
                    weather.setMaxTemp(maxTemperature);

                    Log.i(TAG, "parseJSON: maxTemperature: "+ maxTemperature);

                    String link = resultsObj.getString("Link");
                    weather.setLink(link);

                    Log.i(TAG, "parseJSON: Link: "+ link);

                    weatherArrayList.add(weather);


                }

                if (weatherArrayList != null) {
                    WeatherAdapter weatherAdapter = new WeatherAdapter(this, weatherArrayList);
                            listView.setAdapter(weatherAdapter);
                }

                return weatherArrayList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
}