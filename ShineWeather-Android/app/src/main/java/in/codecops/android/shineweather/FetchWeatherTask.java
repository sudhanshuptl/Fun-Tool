package in.codecops.android.shineweather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by sudhanshu on 28/1/17.
 */

public class FetchWeatherTask extends AsyncTask<Void,Void,Void> {

    private final String apiKey = "21ea3e5e5a62420c47087912fa697558";
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... params) {
        //these two need to declare outside so they can be closed
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Mumbai&mode=xml&units=metric&cnt=7&appid=21ea3e5e5a62420c47087912fa697558");
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
            String forcastJasonStr = buffer.toString();
            Log.d(LOG_TAG, "fetch data success "+forcastJasonStr);

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
        return null;
    }

}