package com.example1.ziv24.mymovies1;


import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jbt on 18/03/2018.
 */

public class MyTask extends AsyncTask<String, Void, String> {

    private Activity activity;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    int flag;

    public MyTask(Activity activity, int flag) {
        this.activity = activity;
        this.flag = flag;
    }

    @Override
    protected String doInBackground(String... strings) {
        return sendHttpRequest(strings[0]);
    }


    @Override
    protected void onPostExecute(String s) {
        final ArrayList<Movies> list = new ArrayList<>();
        if (s != null) {
            try {
                JSONObject object = new JSONObject(s);
                JSONArray arr = object.getJSONArray("results");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject item = arr.getJSONObject(i);
                    String title = item.getString("title");
                    String overview = item.getString("overview");
                    float vote_average = (float) item.getDouble("vote_average");
                    String poster = "https://image.tmdb.org/t/p/original" + item.getString("poster_path");
                    int movie_id = item.getInt("id");
                    list.add(new Movies(title, overview, vote_average, poster, movie_id));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (flag) {
                case 0: {
                    recyclerView = (RecyclerView) activity.findViewById(R.id.rv_toprated);
                    break;
                }
                case 1: {
                    recyclerView = (RecyclerView) activity.findViewById(R.id.rv_upcoming);
                    break;
                }
                case 2: {
                    recyclerView = (RecyclerView) activity.findViewById(R.id.rv_nowplaying);
                    break;
                }
            }

            adapter = new RecyclerViewAdapter(activity, list, activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);

        }
    }

    private String sendHttpRequest(String urlString) {
        BufferedReader input = null;
        HttpURLConnection httpCon = null;
        InputStream input_stream = null;
        InputStreamReader input_stream_reader = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("TAG", "Cannot Connect to : " + urlString);
                return null;
            }

            input_stream = httpCon.getInputStream();
            input_stream_reader = new InputStreamReader(input_stream);
            input = new BufferedReader(input_stream_reader);
            String line;
            while ((line = input.readLine()) != null) {
                response.append(line + "\n");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input_stream_reader.close();
                    input_stream.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }
        }
        return response.toString();
    }
}
