package in.codecops.android.shineweather;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container ,new PlaceholderFragment()).commit();

        }
    }



public static class PlaceholderFragment extends Fragment{
    private ArrayAdapter<String> mForcastAdaptor;
    public  PlaceholderFragment(){
        // constructor
    }

    //------------------------
    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        //add thid line in order for this fragment to handle menu
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //handle action bar item clicked
        int id =item.getItemId();
        if(id==R.id.action_refresh){
            FetchWeatherTask weatherTask =new FetchWeatherTask();
            weatherTask.execute();
            return true;
        }
        return  onOptionsItemSelected(item);
    }

   // ---------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forcastArray={
                "Sat -34 -324",
                "Sun -34-234 ",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sun -34-234 ",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324",
                "Sat -34 -324"
        };
        List<String> weekForcast = new ArrayList<String>(Arrays.asList(forcastArray));
        mForcastAdaptor = new ArrayAdapter<String>(
                //current context
                getActivity(),
                // ID of list item layout
                R.layout.list_item_forecost,
                //ID of textview to populate
                R.id.list_item_forcost_textView,
                // forecast data
                weekForcast );
        //get reference to the list view and attach to the adaptor
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forcast);
        listView.setAdapter(mForcastAdaptor);

        return rootView;
    }
}

}
