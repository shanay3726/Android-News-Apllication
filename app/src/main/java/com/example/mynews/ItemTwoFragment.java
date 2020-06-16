package com.example.mynews;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mynews.R;
import com.example.mynews.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ItemTwoFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        View root = inflater.inflate(R.layout.headlines, container, false);
////        toolbar = (Toolbar)root.findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        viewPager = (ViewPager)root.findViewById(R.id.viewpager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//        tabLayout = (TabLayout)root.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.headlines, container, false);
//        toolbar = (Toolbar)root.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)root.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout)root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
//        return inflater.inflate(R.layout.headlines, container, false);
    }

}