package com.example.mynews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TabFragment extends Fragment {
    int position;
    private TextView textView;
    public static final String EXTRA_URL="imageUrl";
    public static final String EXTRA_Text1="text1";
    public static final String EXTRA_Text2="text2";
    public static final String EXTRA_Text3="text3";
    SwipeRefreshLayout refreshLayout;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }
    private ProgressBar spinner1;
    private View abc;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;

    //    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    String url="";
//    private ProgressBar spinner1;
//    private View abc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        abc=inflater.inflate(R.layout.fragment_tab, container, false);

        spinner1 = (ProgressBar)abc.findViewById(R.id.progressBar2);
        mRecyclerView = abc.findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
//        mAdapter=new ExampleAdapter(exampleList);
//        spinner1.setVisibility(View.VISIBLE);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        mExampleList=new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());

        Log.d("position"," "+position);
        if(position==0)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=world";
        }
        else if(position==1)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=business";
        }
        else if(position==2)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=politics";
        }
        else if(position==3)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=sport";
        }
        else if(position==4)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=technology";
        }
        else if(position==5)
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=science";
        }
        else
        {
            url="https://sjsanghvhw8.appspot.com/guardianandroid/sectionName?sec=world";
        }


        refreshLayout =abc.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView = abc.findViewById(R.id.recyclerView1);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
                parseJSON(url);

                refreshLayout.setRefreshing(false);
            }
        });

        parseJSON(url);

        return abc;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        textView = (TextView) view.findViewById(R.id.textView);

//        textView.setText("Fragment " + (position + 1));
//        spinner1 = (ProgressBar)view.findViewById(R.id.progressBar2);
//        spinner1.setVisibility(View.VISIBLE);


    }
    private void parseJSON(String url) {
//        String url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=aa1c5b97-1c7f-4a3f-9f49-559a1bdcfdb2";
        Log.d("urlis",""+url);
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
                                abc.findViewById(R.id.fetchdata2).setVisibility(View.GONE);


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
                            mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
//                            mExampleAdapter.setOnItemClickListener(TabFragment.this);


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
        parseJSON(url);
    }
    //    @Override
//    public void onItemClick(int position) {
//        Intent detailIntent = new Intent(getActivity().getApplicationContext() ,DetailActivity.class);
//        ExampleItem clickedItem=mExampleList.get(position);
//
//        detailIntent.putExtra(EXTRA_URL,clickedItem.getImageResource());
//        detailIntent.putExtra(EXTRA_Text1,clickedItem.getText1());
//        detailIntent.putExtra(EXTRA_Text2,clickedItem.getText2());
//        detailIntent.putExtra(EXTRA_Text3,clickedItem.getText3());
//
//        startActivity(detailIntent);
//    }
}
