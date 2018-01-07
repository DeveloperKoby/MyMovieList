package com.example.jbt.iebdappbykoby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class addFromInternet extends AppCompatActivity {

    MySqlHelper sqlHelper;
    EditText searchMovieET;
    ListView internetLV;
    ArrayAdapter internetListViewAdapter;
    ArrayList<Movie> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_internet);

        sqlHelper= new MySqlHelper(this);
        internetLV = (ListView)findViewById(R.id.internetLV);
        allMovies= new ArrayList();
        internetListViewAdapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, allMovies );

        //assigning context menu to internet search listview
        registerForContextMenu(internetLV);
        internetLV.setAdapter(internetListViewAdapter);

        //clicking on search will download info form json in DoInBackground
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMovieET = (EditText)findViewById(R.id.searchMovieET);
                String searchWord = searchMovieET.getText().toString();
                searchWord = searchWord.replace(" ", "");

                //assigning listview to async task / json information
                MyAsyncTask myMovieSearchAction= new MyAsyncTask(internetLV , addFromInternet.this);

                //checking internet connection if true = internet search \ false = no internet connection toast
                if (CheckInternetConnection.isNetworkAvailable(addFromInternet.this))
                {
                 myMovieSearchAction.execute("https://api.themoviedb.org/3/search/movie?query="
                         +searchWord+"&api_key=07f1757b270822a15bbf5e0bab9d9fdc");
                    Toast.makeText(addFromInternet.this, "Searching...", Toast.LENGTH_SHORT).show();

                    internetListViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(addFromInternet.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //clicking on internetCancelBtn will finish the activity and take you back to searching action
        findViewById(R.id.internetCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(addFromInternet.this, "Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
