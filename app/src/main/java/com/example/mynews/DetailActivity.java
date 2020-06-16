package com.example.mynews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private ProgressBar spinner;
    View abc;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    Map<String,?> hm;
    ExampleItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        setContentView(R.layout.activity_detail);
        sharedPreferences = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = getIntent();
//        String imageUrl=intent.getStringExtra(EXTRA_URL);
        String articleid=intent.getStringExtra("title");
        mRequestQueue= Volley.newRequestQueue(this.getApplicationContext());
        parseJSON(articleid);
        spinner = findViewById(R.id.progressBar2);
        abc=findViewById(R.id.fetchdata2);

//        String text2=intent.getStringExtra(EXTRA_Text2);
//        String text3=intent.getStringExtra(EXTRA_Text3);

//        ImageView imageView=findViewById(R.id.image_view_detail);

//        TextView textView1=findViewById(R.id.text_view_detail2);
//        TextView textView2=findViewById(R.id.text_view_detail3);

//        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);

//        textView1.setText(text2);
//        textView2.setText(text3);
    }
    private void parseJSON(String articleid){
        String url="https://sjsanghvhw8.appspot.com/guardianandroid/id?id1="+articleid;
        Log.d("article",url);
//        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
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
                                JSONObject hit = jsonArray.getJSONObject(0);

                                final String text1 = hit.getString("webTitle");
                                String text2 = hit.getString("date");

                            ZonedDateTime time_in_zone_format = ZonedDateTime.parse(text2);
                            ZonedDateTime time_zone_LA = time_in_zone_format.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ));
                            LocalDateTime news_time_local =  time_zone_LA.toLocalDateTime();
                            LocalDate localDate = news_time_local.toLocalDate();

                            int day = localDate.getDayOfMonth();
                            String month = localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
                            int year = localDate.getYear();
                            String time_to_display="";
                            time_to_display = ""+day+" "+month+" "+year;

//            Log.d("news_time",""+time_in_zone_format);



                                String text3=hit.getString("sectionId");
                                String text4=hit.getString("description");
                                Spanned desc = Html.fromHtml(text4.toString(),Html.FROM_HTML_MODE_LEGACY);
                                final String articleid=hit.getString("id");
                                final String weburl=hit.getString("weburl");
                                String webtext="<u>View Full Article</u>";


                                String imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                try {
                                    imageUrl = hit.getString("Image");
                                }
                                catch (Exception e){
                                    imageUrl="https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                                }
                                currentItem=new ExampleItem(imageUrl, text1, text2, text3,articleid);
                                Log.d("image",imageUrl);
                                TextView textView=findViewById(R.id.text_view_detail1);
                                textView.setText(text3);
                                TextView textView1=findViewById(R.id.text_view_detail3);
                                textView1.setText(text1);





                                TextView textView2=findViewById(R.id.text_view_detail2);
                                textView2.setText(time_to_display);





                                TextView textView3=findViewById(R.id.text_view_detail4);
                                textView3.setText(desc);

                                TextView titletool = findViewById(R.id.detailtitle);
                                titletool.setText(text1);

                                final ImageView booktool=findViewById(R.id.toolbookmark);
                            hm=sharedPreferences.getAll();
                            for(Map.Entry<String,?> entry : hm.entrySet())
                            {
                                Log.d("datas",""+entry.getKey());
                                if(articleid.equals(entry.getKey()))
                                {
                                    booktool.setImageResource(R.drawable.solid_bookmark);
                                }
                            }

                            booktool.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hm=sharedPreferences.getAll();
                                    if(!hm.containsKey((articleid)))
                                    {
                                        booktool.setImageResource(R.drawable.solid_bookmark);
                                        Gson gson = new Gson();
                                        Log.d("share",""+sharedPreferences.getString(articleid,""));
                                        editor.putString(articleid, gson.toJson(currentItem));
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(),"\""+text1+"\" was added to bookmarks",Toast.LENGTH_SHORT).show();

//                editor.clear();
//                editor.commit();
                                    }
                                    else
                                    {
                                        booktool.setImageResource(R.drawable.hollow_bookmark);
                                        editor.remove(articleid);
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(),"\""+text1+"\" was removed from bookmarks",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });



                                ImageView imageview = findViewById(R.id.image_view_detail);
//                                Picasso.with(getApplicationContext()).load(imageUrl).into(imageview);
                                Picasso.get().load(imageUrl).into(imageview);

                                TextView urldetail =  findViewById(R.id.urldetail);
                                urldetail.setText(Html.fromHtml(webtext));
                                urldetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent gg = new Intent(Intent.ACTION_VIEW);
                                        gg.setData(Uri.parse(weburl));
                                        startActivity(gg);
                                    }
                                });

                            ImageView twitter=findViewById(R.id.twitter);
                            twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent hh=new Intent(Intent.ACTION_VIEW);
                                    String tt="Check out this Link: https://theguardian.com/"+articleid;
                                    hh.setData(Uri.parse("https://twitter.com/intent/tweet?text="+tt+"&hashtags=CSCI571NewsSearch"));
                                   getApplicationContext().startActivity(hh);
                                }
                            });

                            ImageView backbutton = findViewById(R.id.backbutton);
                            backbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

//                                mExampleList.add(new ExampleItem(imageUrl, text1, text2, text3,articleid));
//                                Log.d("hello2",String.valueOf(mExampleList));



//                            Log.d("hello1",String.valueOf(mExampleList));
//                            mExampleAdapter = new ExampleAdapter(getActivity().getApplicationContext(), mExampleList);
//                            mRecyclerView.setAdapter(mExampleAdapter);

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
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.fadein,R.anim.slideout);
    }
}
