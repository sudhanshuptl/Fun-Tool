package in.codecops.android.shineweather;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sudhanshu on 28/1/17.
 */

public class FetchWeatherTask extends AsyncTask<Object, Object, String[]> {
    private  final String API="appid";
    private final String apiKey = "21ea3e5e5a62420c47087912fa697558";
    private final  String FORECAST_BASE_URL ="http://api.openweathermap.org/data/2.5/forecast/daily?";
    private  final String QUERY_PARAM ="q";
    private final String FORMATE_PARAM = "mode";
    private  final String FORMATE = "json";
    private final String DAYS_PARAM="cnt";
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected String[] doInBackground(Object... params) {
        //these two need to declare outside so they can be closed
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJasonStr;

        String UNITS_PARAM ="units";
        String units="metric";
        int numOfDays=10;


        try {
            Uri builtUri =Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, (String) params[0])
                    .appendQueryParameter(FORMATE_PARAM,FORMATE)
                    .appendQueryParameter(UNITS_PARAM,units)
                    .appendQueryParameter(DAYS_PARAM,Integer.toString(numOfDays))
                    .appendQueryParameter(API,apiKey)
                    .build();
            URL url = new URL(builtUri.toString());
           // Log.v(LOG_TAG, "BUIlt URL "+ url);
           // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Mumbai&mode=xml&units=metric&cnt=7&appid=21ea3e5e5a62420c47087912fa697558");
            //create request to open weather app and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read input String

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");

            }
            if (buffer.length() == 0) {
                return null;
            }
            forecastJasonStr = buffer.toString();
           // Log.d(LOG_TAG, "fetch data success "+forecastJasonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while connecting to server ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "error in closing reader stream ", e);
                }
            }
        }
        try{
            return getWeatherDataFromJson(forecastJasonStr,numOfDays);
        }
        catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(),e );
            e.printStackTrace();
        }
        return  null;
    }

    //-----------------------


    @Override
    protected void onPostExecute(String[] result) {
        if(result !=null){
            MainActivity.PlaceholderFragment.mForcastAdaptor.clear();
            for(String dayForecastStr :result){
                MainActivity.PlaceholderFragment.mForcastAdaptor.add(dayForecastStr);
            }
        }
    }

    //-------------------
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DATETIME = "dt";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime = dayForecast.getLong(OWM_DATETIME);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        return resultStrs;
    }

}