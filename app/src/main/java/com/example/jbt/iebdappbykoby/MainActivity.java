package com.example.jbt.iebdappbykoby;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mainLV;
    ArrayList<Movie> allMovies;
    MySqlHelper sqlHelper;
    SimpleCursorAdapter adapter;
    Cursor myCursor;

    // position for  context menu
    int currentPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlHelper= new MySqlHelper(this);
        allMovies = new ArrayList<>();
        myCursor= sqlHelper.getReadableDatabase().rawQuery("SELECT * FROM "+DBconstants.TABLE_NAME, null);
        String[] myColumns= { DBconstants.NAME_COLUMN };
        int[] toIDS= { android.R.id.text1  };
        adapter = new SimpleCursorAdapter(this ,android.R.layout.simple_list_item_1 , myCursor , myColumns , toIDS);
        mainLV = (ListView) findViewById(R.id.mainLV);
        mainLV.setAdapter(adapter);

        //apply context menu on LV
        registerForContextMenu(mainLV);

        //clicking on item from listview will send you to add manual\edit activity
        mainLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                myCursor.moveToPosition(position);
                String movieName = myCursor.getString(myCursor.getColumnIndex(DBconstants.NAME_COLUMN));
                String movieBody = myCursor.getString(myCursor.getColumnIndex(DBconstants.BODY_COLUMN));
                String movieURL = myCursor.getString(myCursor.getColumnIndex(DBconstants.URL_COLUMN));
                int movieId = myCursor.getInt(myCursor.getColumnIndex("_id"));
                Intent intent = new Intent(MainActivity.this, addManual.class);
                intent.putExtra("Name", movieName);
                intent.putExtra("Body", movieBody);
                intent.putExtra("Url", movieURL);
                intent.putExtra("_id", movieId);
                startActivity(intent);

            }
        });

        //clicking on add manual will send you to add manual\edit activity
        findViewById(R.id.addManualBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addManual.class);
                startActivity(intent);

            }
        });

        //clicking on add from internet will send you to addFromInternet Activity
        findViewById(R.id.addFromInternetBtn).setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent intent = new Intent(MainActivity.this, addFromInternet.class);
                         startActivity(intent);
                     }
                 }
                );
    }

    //creating Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //assinging id's for Delete all and exit app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deleteAll_menu) {
            SQLiteDatabase db = null;
            db = sqlHelper.getWritableDatabase();
            db.delete(DBconstants.TABLE_NAME, null, null);
            MainActivity.this.adapter.notifyDataSetChanged();
            mainLV.invalidate();
            myCursor = sqlHelper.getReadableDatabase().query(DBconstants.TABLE_NAME, null, null, null, null, null, null);
            mainLV.setAdapter(adapter);
            adapter.swapCursor(myCursor);

        //closing app
        } else if (item.getItemId() == R.id.exit_menu) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }
        return true;
    }

    //creating context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(menuInfo != null)
        {
            currentPosition= ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        }
        getMenuInflater().inflate(R.menu.menu_onitem,menu );
    }

    //delete single item from LV
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.deleteItem) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            currentPosition = (int) info.id;
            sqlHelper.removeRecord(String.valueOf(currentPosition));
            MainActivity.this.adapter.notifyDataSetChanged();
            mainLV.invalidate();
            myCursor = sqlHelper.getReadableDatabase().query(DBconstants.TABLE_NAME, null, null, null, null, null, null);
            adapter.swapCursor(myCursor);

        }

        return true;
    }

    // refresh Cursor onResume to mainactivity
    @Override
    protected void onResume() {
        super.onResume();
        myCursor= sqlHelper.getReadableDatabase().rawQuery("SELECT * FROM "+DBconstants.TABLE_NAME, null);
        MainActivity.this.adapter.notifyDataSetChanged();
        adapter.swapCursor(myCursor);

    }


}


