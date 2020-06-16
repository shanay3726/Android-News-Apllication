
package com.example.mynews;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mynews.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ItemFourFragment extends Fragment {
    public static ItemFourFragment newInstance() {
        ItemFourFragment fragment = new ItemFourFragment();
        return fragment;
    }
    private RecyclerView mRecyclerView;
    private SecondAdapter mExampleAdapter;
    //    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ExampleItem cards;
    private View abc;
    Map<String,?> hm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bookmarks, container, false);
        abc=root;
//        spinner = (ProgressBar)root.findViewById(R.id.progressBar1);
//        spinner.setVisibility(View.VISIBLE);

        mRecyclerView = root.findViewById(R.id.recyclerView123);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));

//        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
//        mAdapter=new ExampleAdapter(exampleList);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

//        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());

        bookmarkJSON();
        return root;
    }
    public void bookmarkJSON(){
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        hm=sharedPreferences.getAll();
        if(hm.size()>0)
        {
            abc.findViewById(R.id.fetchdata2).setVisibility(View.GONE);

        }
        else
        {
            abc.findViewById(R.id.fetchdata2).setVisibility(View.VISIBLE);
        }
        mExampleList=new ArrayList<>();
        String title;
        String date;
        String image;
        String section;
        String id;
        String url;
        int c=0;

        Gson gson = new Gson();
        for(Map.Entry<String,?> entry : hm.entrySet())
        {
            String dataset = sharedPreferences.getString(entry.getKey(),"");
            cards=gson.fromJson(
                    dataset,ExampleItem.class
            );
            title=cards.getText1();
            date=cards.getText2();
            section=cards.getText3();
            image=cards.getImageResource();
            id=cards.getArticleid();
            mExampleList.add(new ExampleItem(image,title,date,section,id));



        }
        mExampleAdapter = new SecondAdapter(getActivity(), mExampleList,ItemFourFragment.this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mExampleAdapter);
    }
    private void parseJSON() {
//        String url = "http://192.168.0.168:9000/guardianandroid";
//        Log.d("heaf","nack");
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
////                            Log.d("hello","hi"+response);
//                            Log.d("apicall","ajf");
////                            spinner.setVisibility(View.VISIBLE);
//                            Log.d("apicall",""+response);
//                            JSONArray jsonArray = response;
//                            Log.d("hello",""+jsonArray);
//                            if(jsonArray.length()!=0) {
////                                spinner.setVisibility(View.GONE);
////                                abc.findViewById(R.id.fetchdata).setVisibility(View.GONE);
//
//                            }
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject hit = jsonArray.getJSONObject(i);
//
//                                String text1 = hit.getString("webTitle");
//                                String text2 = hit.getString("date");
//                                String text3=hit.getString("sectionId");
//                                String articleid=hit.getString("id");
//                                String imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
//                                try {
//                                    imageUrl = hit.getString("Image");
//                                }
//                                catch (Exception e){
//                                    imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
//                                }
//
//                                mExampleList.add(new ExampleItem(imageUrl, text1, text2, text3,articleid));
//                                Log.d("hello2",String.valueOf(mExampleList));
//                            }
//
//
//                            Log.d("hello1",String.valueOf(mExampleList));
//                            mExampleAdapter = new SecondAdapter(getActivity(), mExampleList);
//                            mRecyclerView.setAdapter(mExampleAdapter);
//
//                        } catch (JSONException e) {
//                            Log.d("hello3","Error");
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });

//        mRequestQueue.add(request);
    }
}