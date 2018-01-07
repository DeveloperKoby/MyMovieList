package com.example.jbt.iebdappbykoby;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class addManual extends AppCompatActivity {

    EditText subjectET;
    EditText bodyET;
    EditText urlET;
    ImageView myImageIV;
    MySqlHelper sqlHelper;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual);
        sqlHelper= new MySqlHelper(this);
        subjectET =(EditText)findViewById(R.id.subjectET);
        bodyET =(EditText)findViewById(R.id.bodyET);
        urlET =(EditText)findViewById(R.id.urlET);
        myImageIV = (ImageView)findViewById(R.id.myImageIV);

        // creating get intent to get information from myAsyckTask \ Json class
        Intent receivedIntent = getIntent();
        String name = receivedIntent.getStringExtra("Name");
        String body = receivedIntent.getStringExtra("Body");
        String url = receivedIntent.getStringExtra("Url");

        //id for update or new movie
        id = receivedIntent.getIntExtra("_id", -1);
        subjectET.setText(name);
        bodyET.setText(body);
        final String imageViewUrl = "https://image.tmdb.org/t/p/w500"+url;
        urlET.setText(url);

        //clicking on show button will show the Image on ImageView
        findViewById(R.id.showImageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(getApplicationContext()).load(imageViewUrl).into(myImageIV);
                Toast.makeText(addManual.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        });

        //clicking on save will save the information into the database
        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             String title = subjectET.getText().toString();
             String body = bodyET.getText().toString();
             String url = urlET.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBconstants.NAME_COLUMN , title);
                contentValues.put(DBconstants.BODY_COLUMN , body);
                contentValues.put(DBconstants.URL_COLUMN , url);

                //if the id isn't -1 , its not a new movie
                if(id != -1){
                    sqlHelper.getWritableDatabase().update(DBconstants.TABLE_NAME ,contentValues , "_id = ?",new String[]{""+id} );
                    Toast.makeText(addManual.this, "Database updated", Toast.LENGTH_SHORT).show();

                    //if the id is -1 is a new movie
                }else {
                    sqlHelper.getWritableDatabase().insert(DBconstants.TABLE_NAME, null, contentValues);
                    Toast.makeText(addManual.this, "Saved on database", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(addManual.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //clicking on manualCancelBtn will finish the activity and take you back to main activity
        findViewById(R.id.manualCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(addManual.this, "Cancel", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
