package com.example.jbt.iebdappbykoby;

/**
 * Created by jbt on 14/11/2017.
 */

//Movie object class
public class Movie {

    String title;
    String body;
    String poster_path;



    //Movie Object constructor
    public Movie(String title, String body ,String poster_path ) {
        this.title = title;
        this.body = body;
        this.poster_path = poster_path;



    }

    @Override
    public String toString() {
        return title;
    }
}
