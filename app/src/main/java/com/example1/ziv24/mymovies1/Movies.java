package com.example1.ziv24.mymovies1;

/**
 * Created by ziv24 on 21/03/2018.
 */

public class Movies {
    private String original_title;
    private String overview;
    private float vote_average;
    private String poster;
    private int movie_id;

    public Movies() {
        this.original_title = "Add movie";
        this.movie_id = -3;
    }

    public Movies(String original_title, String overview, float vote_average, String poster, int id) {
        this.original_title = original_title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.poster = poster;
        this.movie_id = id;
    }







    public int getId() {
        return movie_id;
    }

    public void setId(int id) {
        this.movie_id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return this.original_title;
    }
}
