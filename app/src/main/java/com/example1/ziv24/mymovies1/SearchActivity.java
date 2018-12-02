package com.example1.ziv24.mymovies1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    String fixed_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button search_btn = findViewById(R.id.search_btn);
        final android.widget.SearchView sv = (android.widget.SearchView) findViewById(R.id.svv);



        sv.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchView sv = (SearchView)findViewById(R.id.svv);
                fixed_url = sv.getQuery().toString();

                searchMovies(fixed_url);
            }
        });

    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
    public void searchMovies(String fixed_url){

        String mov = "";
        try {
            mov = URLEncoder.encode(fixed_url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.src_api);
        fixed_url = url + mov + "&page=1";

        Task_Get_MovieList task = new Task_Get_MovieList(SearchActivity.this);
        task.execute(fixed_url);
    }

}
