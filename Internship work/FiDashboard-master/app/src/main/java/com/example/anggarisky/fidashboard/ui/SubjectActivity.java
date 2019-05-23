package com.example.anggarisky.fidashboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.anggarisky.fidashboard.R;
import com.example.anggarisky.fidashboard.adapters.ViewPagerAdapter;
import com.example.anggarisky.fidashboard.fragments.EnglishFragment;
import com.example.anggarisky.fidashboard.fragments.HindiFragment;
import com.squareup.picasso.Picasso;

public class CollapsingToolbarTabs extends AppCompatActivity {
    String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toobar_tabs);
        Intent i = getIntent();
        className = i.getStringExtra("className");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(EnglishFragment.newInstance(className), "English Books");
        mViewPagerAdapter.addFragment(HindiFragment.newInstance(className), "Hindi Books");
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ImageView imageViewMusic = findViewById(R.id.imaViewMusic);
        Picasso.get().load("https://www.gool.com").placeholder(R.drawable.mascot2).fit().into(imageViewMusic);
    }

}
