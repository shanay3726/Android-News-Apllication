
package com.example.mynews;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mynews.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemThreeFragment extends Fragment {

    LineChart mpLineChart;
    private ProgressBar spinner;
    View abc;
    private RequestQueue mRequestQueue;


    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.trending, container, false);
        abc=root;
        spinner = (ProgressBar)root.findViewById(R.id.progressBar2);
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());


        parseJSON("Coronavirus");
        final EditText edit_txt = (EditText) root.findViewById(R.id.trendingtext);

        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    String wordtrend=edit_txt.getText().toString();
                    parseJSON(wordtrend);
                    return true;
                }
                return false;
            }
        });



        return root;
    }
    ArrayList<Entry> dataVals;

    private void parseJSON(final String term){
        Log.d("term",term);
        String url="https://sjsanghvhw8.appspot.com/guardianandroid/gettrending?gettrending="+term;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            Log.d("hello","hi"+response);
                            Log.d("apibigcall","ajf");
//                            spinner.setVisibility(View.VISIBLE);
                            Log.d("apibigcall",""+response);
                            JSONArray jsonArray = response;
                            Log.d("hello",""+jsonArray);
                            if(jsonArray.length()!=0) {
                                spinner.setVisibility(View.GONE);
                                abc.findViewById(R.id.fetchdata2).setVisibility(View.GONE);

                            }
                            Log.d("hello",""+jsonArray.get(5));
                            dataVals=new ArrayList<Entry>();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                dataVals.add(new Entry(i,jsonArray.getInt(i)));
                            }
                            Log.d("hello1",""+dataVals);
                            printdata(term);



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
    private void printdata(String term){
        mpLineChart=abc.findViewById(R.id.linechart1);
        Log.d("bhaijan",""+dataVals);
        LineDataSet linedataset1 = new LineDataSet(dataVals,"Trending Chart for "+term);
        linedataset1.setColor(getResources().getColor(R.color.colorPrimary));
        linedataset1.setCircleColor(getResources().getColor(R.color.colorPrimary));
        linedataset1.setCircleHoleColor(getResources().getColor(R.color.colorPrimary));
        linedataset1.setValueTextSize(8f);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(linedataset1);

        Legend l = mpLineChart.getLegend();
        l.setTextSize(16f);
        l.setFormSize(15f);
        mpLineChart.getXAxis().setDrawGridLines(false);
        mpLineChart.getAxisLeft().setDrawGridLines(false);
        mpLineChart.getAxisRight().setDrawGridLines(false);
        YAxis leftAxis = mpLineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);


        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }
}