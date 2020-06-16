package com.example.mynews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ProgressBar spinner1;
    private View abc;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private TextView textView;
    public static final String EXTRA_URL="imageUrl";
    public static final String EXTRA_Text1="text1";
    public static final String EXTRA_Text2="text2";
    public static final String EXTRA_Text3="text3";
    SwipeRefreshLayout refreshLayout;

    //    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    String url="";
    String query="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_search);

        spinner1 = (ProgressBar)findViewById(R.id.progressBar2);
        mRecyclerView = findViewById(R.id.recyclerViewSearch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent intent = getIntent();
//        String imageUrl=intent.getStringExtra(EXTRA_URL);
        query=intent.getStringExtra("query");
        ImageView backButton22 = findViewById(R.id.backButton22);
        backButton22.setImageResource(R.drawable.backbutton);
        backButton22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
//        mAdapter=new ExampleAdapter(exampleList);
//        spinner1.setVisibility(View.VISIBLE);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        mExampleList=new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(getApplicationContext());
//        refreshLayout =abc.findViewById(R.id.swiperefresh);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mRecyclerView = findViewById(R.id.recyclerView1);
//                mRecyclerView.setHasFixedSize(true);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                mRequestQueue= Volley.newRequestQueue(getApplicationContext());
//                parseJSON(url);
//
//                refreshLayout.setRefreshing(false);
//            }
//        });
        url ="https://sjsanghvhw8.appspot.com/guardianandroid/search?search="+query;



        parseJSON(url,query);

    }
    private void parseJSON(String url,String query) {
//        String url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=aa1c5b97-1c7f-4a3f-9f49-559a1bdcfdb2";
        Log.d("urlis",""+url);
        final String tt="Search Results for "+query;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            Log.d("hello","hi"+response);
                            //spinner1.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = response;
                            Log.d("hello3",""+jsonArray);
                            if(jsonArray.length()!=0) {
                                spinner1.setVisibility(View.GONE);
                                findViewById(R.id.fetchdata2).setVisibility(View.GONE);


                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String text1 = hit.getString("webTitle");
                                String text2 = hit.getString("date");

                                ZonedDateTime time_in_zone_format = ZonedDateTime.parse(text2);
//            Log.d("news_time",""+time_in_zone_format);
                                ZonedDateTime time_zone_LA = time_in_zone_format.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ));
//            Log.d("news_time",""+time_zone_LA);
                                LocalDateTime news_time_local =  time_zone_LA.toLocalDateTime();
//            Log.i("news_time",""+time_in_zone_format);
//            Log.d("news_time_local",""+news_time_local);

                                LocalDateTime current_time_local = LocalDateTime.now();            //Local date time
//            Log.i("current_time_local",""+current_time_local);

                                long time_difference;
                                String time;
                                time_difference = ChronoUnit.DAYS.between(news_time_local,current_time_local);
                                if(time_difference < 1)
                                {
                                    time_difference = ChronoUnit.HOURS.between(news_time_local,current_time_local);
                                    if(time_difference < 1)
                                    {
                                        time_difference = ChronoUnit.MINUTES.between(news_time_local, current_time_local);
                                        if(time_difference < 1)
                                        {
                                            time_difference = ChronoUnit.SECONDS.between(news_time_local,current_time_local);
                                            time = String.valueOf(time_difference)+"s ago";
                                        }
                                        else
                                        {
                                            time = String.valueOf(time_difference)+"m ago";
                                        }
                                    }
                                    else
                                    {
                                        time = String.valueOf(time_difference)+"h ago";
                                    }
                                }
                                else
                                {
                                    time = String.valueOf(time_difference)+"d ago";
                                }




                                String text3=hit.getString("sectionId");
                                String articleid=hit.getString("id");;
//                                Log.d(""+i,""+hit.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file"));
                                Log.d("hello2","try");
                                String imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                try {
                                    imageUrl =hit.getString("Image");
                                }
                                catch (Exception e){
                                    imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }

                                mExampleList.add(new ExampleItem(imageUrl, text1, text2, text3,articleid));
                                Log.d("hello2",String.valueOf(mExampleList));
                            }

                            Log.d("heybto","almost done trying hard");
//                            Log.d("hello1",String.valueOf(mExampleList));
                            mExampleAdapter = new ExampleAdapter(SearchActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
//                            mExampleAdapter.setOnItemClickListener(TabFragment.this);
                            TextView hhh=findViewById(R.id.searchtitle);
                            hhh.setText(tt);



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
        parseJSON(url,query);
    }
}
