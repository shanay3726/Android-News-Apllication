package com.example.mynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    JSONArray suggestionsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.detailtool);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ItemOneFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemTwoFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemThreeFragment.newInstance();
                                break;
                            case R.id.action_item4:
                                selectedFragment = ItemFourFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemOneFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        Log.i("FragCreateList", "onCreateOptionsMenu called");
        //super.onCreateOptionsMenu(menu, inflater);
//        getMenuInflater().inflate(R.menu.search, menu);
//        MenuItem menuItem = menu.findItem(R.id.searchicon);
//
//        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
//        searchView.setQueryHint("");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.searchicon);
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchMenuItem.getActionView();
        final androidx.appcompat.widget.SearchView.SearchAutoComplete suggestions = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        //searchView.setBackgroundResource(R.drawable.ic_search_24px);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viz, int posi, long id) {
                //Intent send_intent = new Intent(viz.getContext(), SearchActivity.class);
                String search_keyw = (String) parent.getItemAtPosition(posi);
                searchView.setQuery(search_keyw, false);
                //send_intent.putExtra(Extra_query, search_keyw);
                //startActivity(send_intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("search", query);
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                String id= mDataset.get(holder.getAdapterPosition()).getmid();
                intent.putExtra("query",query);
//                spinner=(ProgressBar)v.findViewById(R.id.progressBar1);

                getApplicationContext().startActivity(intent);
////                return true;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(newText.length() < 3) {
                    ArrayAdapter<String> newsempty = null;
                    suggestions.setAdapter(newsempty);
                    return true;
                }
                getSuggestionList(newText, suggestions);

                return true;
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e("search", query);
////                return true;h
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                arrayAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }
    private void getSuggestionList(String query, final androidx.appcompat.widget.SearchView.SearchAutoComplete suggestions) {
        String url = "https://jmparekh.cognitiveservices.azure.com/bing/v7.0/suggestions?q="+query;
        RequestQueue bingReqQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest bingJsonReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    suggestionsData = response.getJSONArray("suggestionGroups").getJSONObject(0).getJSONArray("searchSuggestions");
                    String[] suggested = new String[5];
                    int limit = Math.min(suggestionsData.length(), 5);
                    for (int i = 0; i<limit; i++) {
                        try {
                            suggested[i] = suggestionsData.getJSONObject(i).getString("displayText");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, suggested);
                    suggestions.setAdapter(newsAdapter);
                    suggestions.showDropDown();
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map <String, String> bingKey = new HashMap<>();
                bingKey.put("Ocp-Apim-Subscription-Key", "667604a798274043a1a27047b75a5bc2");
                return bingKey;
            }
        };
        bingReqQueue.add(bingJsonReq);
    }
}
