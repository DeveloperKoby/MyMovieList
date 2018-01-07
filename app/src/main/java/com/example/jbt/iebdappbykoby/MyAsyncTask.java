package com.example.jbt.iebdappbykoby;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by jbt on 17/11/2017.
 */

public class MyAsyncTask extends AsyncTask <String , Integer ,String> {

    ListView internetLV;
    Context activityContext;
    ArrayList<Movie> allMovies;
    ArrayAdapter<Movie> adapter;


    public MyAsyncTask(  ListView internetLV ,  Context activityContext ){
        this.internetLV = internetLV;
        this.activityContext = activityContext;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder response= null;



        try {
            String myUrl= strings[0];
            URL website = new URL(myUrl);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
        }
        catch (IOException ee)
        {

        }

        return response.toString();
    }

    @Override
    protected void onPostExecute(String JSONText) {
        String title = null;
        String overview = null;
        String poster_path = null;

        /* break the JSON to array */
        final ArrayList<Movie> allMovies= new ArrayList();

        try {
            JSONObject mainObject= new JSONObject(JSONText);
            allMovies.clear();
            /* diging inside the JSONObject (looking for the array) */

            JSONArray movieArray= mainObject.getJSONArray("results");
            //for loop that would get the information from the Json
            for (int i = 0 ; i < movieArray.length(); i++){
                JSONObject currentMovieObj = movieArray.getJSONObject(i);
                 title = currentMovieObj.getString("title");
                 overview = currentMovieObj.getString("overview");
                 poster_path = currentMovieObj.getString("poster_path");

                Movie newMovie = new Movie(title, overview ,poster_path);
                allMovies.add(newMovie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<Movie>(activityContext, android.R.layout.simple_list_item_1, allMovies);
        internetLV.setAdapter(adapter);
        internetLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie currentMovie= allMovies.get(position);
                Intent intent = new Intent(activityContext, addManual.class);
                intent.putExtra("Name",currentMovie.title);
                intent.putExtra("Body", currentMovie.body);
                intent.putExtra("Url", currentMovie.poster_path);

                activityContext.startActivity(intent);



            }
        });
        adapter.notifyDataSetChanged();


    }
}


