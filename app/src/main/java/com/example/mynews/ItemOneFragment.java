package com.example.mynews;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.mynews.ExampleAdapter;
import com.example.mynews.ExampleItem;
import com.example.mynews.MainActivity;
import com.example.mynews.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemOneFragment extends Fragment{
    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }
    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    private RequestQueue wRequestQueue;
    LocationManager locationManager;
    final int REQUEST_LOCATION=1;
    private LocationListener locationListener;
    Double longi,lati;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private ProgressBar spinner;
    private View abc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home, container, false);
        abc=root;
        ActivityCompat.requestPermissions(getActivity(),
               new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        spinner = (ProgressBar)root.findViewById(R.id.progressBar1);
//        spinner.setVisibility(View.VISIBLE);
        refreshLayout =root.findViewById(R.id.swiperefresh);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
//        mAdapter=new ExampleAdapter(exampleList);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        mExampleList=new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView = abc.findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
//        mAdapter=new ExampleAdapter(exampleList);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
                mExampleList=new ArrayList<>();
                mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
                parseJSON();
                getlocation();
                refreshLayout.setRefreshing(false);
            }
        });


        parseJSON();
        getlocation();

        return root;
    }

    public void getlocation(){
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String text = "" + location.getLongitude() + ";" + location.getLatitude();
                Log.d("textingto",text);
//                displayTextView(text);
                wRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
                getweathercard(text);
                locationManager.removeUpdates(locationListener);
                locationManager = null;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        configure_button();
    }

    public void getweathercard(String text){

        longi = Double.parseDouble(text.split(";")[0]);
        lati = Double.parseDouble(text.split(";")[1]);
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
//         lon =-118.24;
//         lat=34.05;
        List<Address> addresses = null;
        Log.d("location","location");
        try {
            addresses = geocoder.getFromLocation((double)lati, (double)longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();
        TextView t3 = abc.findViewById(R.id.city);
        t3.setText(cityName);
        TextView t4 =abc.findViewById(R.id.state);
        t4.setText(stateName);
        Log.i("location",cityName);
        Log.i("location",stateName);
        final String weatherurl="https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&units=metric&appid=403b7c5537a8bc8f94ed2a285c75c1d7";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, weatherurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONObject master = response.getJSONObject("main");
                            JSONObject weather1 =response.getJSONArray("weather").getJSONObject(0);
                            Log.d("location",""+String.valueOf(response));
                            Log.d("location",""+String.valueOf(master));
                            Log.d("location",""+String.valueOf(weather1));

                            String weather_desc = weather1.getString("main");
                            Log.i("location",weather_desc);
//
                            Double temp = (double) master.getDouble("temp");
                            TextView t = abc.findViewById(R.id.temperature);
                            t.setText(""+Math.round(temp) +" "+ "\u2103");

                            TextView t2 = abc.findViewById(R.id.type);
                            t2.setText(weather_desc);

                            ImageView weatherImage = abc.findViewById(R.id.weatherImage);

                            switch (weather_desc){
                                case "Clouds":
                                    weatherImage.setImageResource(R.drawable.cloudy_weather);
                                    break;
                                case "Clear":
                                    weatherImage.setImageResource(R.drawable.clear_weather);
                                    break;
                                case "Snow":
                                    weatherImage.setImageResource(R.drawable.snowy_weather);
                                    break;
                                case "Rain":
                                case "Drizzle":
                                    weatherImage.setImageResource(R.drawable.rainy_weather);
                                    break;
                                case "Thunderstorm":
                                    weatherImage.setImageResource(R.drawable.thunder_weather);
                                    break;
                                default:
                                    weatherImage.setImageResource(R.drawable.sunny_weather);
                            }


//
//                            Log.d("location",""+master);
//
//                            //{"lon":-118.24,"lat":34.05}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("location","error");
                error.printStackTrace();
            }
        });

        wRequestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }

    private void parseJSON() {
        String url = "https://sjsanghvhw8.appspot.com/guardianandroid";
        Log.d("heaf","nack");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            Log.d("hello","hi"+response);
                            Log.d("apicall","ajf");
                            spinner.setVisibility(View.VISIBLE);
                            Log.d("apicall",""+response);
                            JSONArray jsonArray = response;
                            Log.d("hello",""+jsonArray);
                            if(jsonArray.length()!=0) {
                               spinner.setVisibility(View.GONE);
                               abc.findViewById(R.id.fetchdata).setVisibility(View.GONE);

                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String text1 = hit.getString("webTitle");
                                String text2 = hit.getString("date");
//                                ZonedDateTime time_in_zone_format = ZonedDateTime.parse(text2);
////            Log.d("news_time",""+time_in_zone_format);
//                                ZonedDateTime time_zone_LA = time_in_zone_format.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ));
////            Log.d("news_time",""+time_zone_LA);
//                                LocalDateTime news_time_local =  time_zone_LA.toLocalDateTime();
////            Log.i("news_time",""+time_in_zone_format);
////            Log.d("news_time_local",""+news_time_local);
//
//                                LocalDateTime current_time_local = LocalDateTime.now();            //Local date time
////            Log.i("current_time_local",""+current_time_local);
//
//                                long time_difference;
//                                String time;
//                                time_difference = ChronoUnit.DAYS.between(news_time_local,current_time_local);
//                                if(time_difference < 1)
//                                {
//                                    time_difference = ChronoUnit.HOURS.between(news_time_local,current_time_local);
//                                    if(time_difference < 1)
//                                    {
//                                        time_difference = ChronoUnit.MINUTES.between(news_time_local, current_time_local);
//                                        if(time_difference < 1)
//                                        {
//                                            time_difference = ChronoUnit.SECONDS.between(news_time_local,current_time_local);
//                                            time = String.valueOf(time_difference)+"s ago";
//                                        }
//                                        else
//                                        {
//                                            time = String.valueOf(time_difference)+"m ago";
//                                        }
//                                    }
//                                    else
//                                    {
//                                        time = String.valueOf(time_difference)+"h ago";
//                                    }
//                                }
//                                else
//                                {
//                                    time = String.valueOf(time_difference)+"d ago";
//                                }
                                String text3=hit.getString("sectionId");
                                String articleid=hit.getString("id");
                                String imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                try {
                                    imageUrl = hit.getString("Image");
                                }
                                catch (Exception e){
                                    imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                mExampleList.add(new ExampleItem(imageUrl, text1, text2, text3,articleid));
                                Log.d("hello2",String.valueOf(mExampleList));
                            }


                            Log.d("hello1",String.valueOf(mExampleList));
                            mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);

                        } catch (JSONException e) {
                            Log.d("hello3","Error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        parseJSON();
    }
}