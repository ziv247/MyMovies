package com.example1.ziv24.mymovies1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example1.ziv24.mymovies1.db.DBHandler;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movies> movieList;
    MyMoviesAdapter adapter;
    String url_TopRated;
    String url_UpComing;
    String url_NowPlaying;
    DBHandler handler = new DBHandler(this);
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            Log.e("MainActivity_Exeption ", "MainActivity_Exeption " + e.getMessage());
        }
        url_TopRated = getString(R.string.tr_api);
        url_UpComing = getString(R.string.upco_api);
        url_NowPlaying = getString(R.string.np_api);

        final Animation buttonHide = AnimationUtils.loadAnimation(MainActivity.this, android.support.design.R.anim.abc_slide_out_bottom);
        final Animation buttonShow = AnimationUtils.loadAnimation(MainActivity.this, android.support.design.R.anim.abc_slide_in_bottom);
        final Animation buttonadd_show = AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_btn);
        final Animation buttonadd_hide = AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_btns);

        MyTask task = new MyTask(this, 0);
        MyTask task1 = new MyTask(this, 1);
        MyTask task2 = new MyTask(this, 2);
        task.execute(url_TopRated);
        task1.execute(url_UpComing);
        task2.execute(url_NowPlaying);


        movieList = handler.getAllNotes();
        movieList.add(new Movies());
        RecyclerView recyclerView = findViewById(R.id.rv_mymovies);
        adapter = new MyMoviesAdapter(this, movieList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new MyMoviesAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(int position) {
                final int pos = position;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View v = getLayoutInflater().inflate(R.layout.dialog, null);
                FloatingActionButton fab_remove = v.findViewById(R.id.fab_remove);
                fab_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int _id = movieList.get(pos).getId();
                        handler.deleteMovie(_id);
                        movieList.remove(pos);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();

                    }
                });
                FloatingActionButton fab_cancel = v.findViewById(R.id.fab_cancel);
                fab_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                mBuilder.setView(v);

                dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                dialog.getWindow().getAttributes().windowAnimations = R.style.UpDownAnim;
                dialog.show();

            }


        });


        final FloatingActionButton fab = findViewById(R.id.fab);
        final FloatingActionButton fab_src = findViewById(R.id.fab_src);
        final FloatingActionButton fab_kb = findViewById(R.id.fab_kb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab_src.getVisibility() == View.VISIBLE && fab_kb.getVisibility() == View.VISIBLE) {
                    fab.setAnimation(buttonadd_show);
                    fab_src.setVisibility(View.GONE);
                    fab_src.startAnimation(buttonHide);
                    fab_kb.setVisibility(View.GONE);
                    fab_kb.startAnimation(buttonHide);
                } else {
                    fab.setAnimation(buttonadd_hide);
                    fab_src.setVisibility(View.VISIBLE);
                    fab_src.startAnimation(buttonShow);
                    fab_kb.setVisibility(View.VISIBLE);
                    fab_kb.startAnimation(buttonShow);
                }

            }
        });
        fab_src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setAnimation(buttonadd_show);
                fab_src.setVisibility(View.GONE);
                fab_src.startAnimation(buttonHide);
                fab_kb.setVisibility(View.GONE);
                fab_kb.startAnimation(buttonHide);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        fab_kb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setAnimation(buttonadd_show);
                fab_src.setVisibility(View.GONE);
                fab_src.startAnimation(buttonHide);
                fab_kb.setVisibility(View.GONE);
                fab_kb.startAnimation(buttonHide);
                Intent intent = new Intent(MainActivity.this, Add_Activity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });

    }

    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_en) {
            setLocale("en");
            recreate();

        } else if (id == R.id.action_he) {
            setLocale("iw");
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en");
        setLocale(language);
    }


}
