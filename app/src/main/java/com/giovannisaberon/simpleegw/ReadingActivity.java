package com.giovannisaberon.simpleegw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        final Intent readingIntent = new Intent(this, SettingsActivity.class);
        Button settings_button = (Button) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(readingIntent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Resources res = getResources();
        String[] dataset = res.getStringArray(R.array.book_arrays);
        ArrayList<String> selectedbooks = new ArrayList<String>(){};
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String json_data = null;
        EGWJson egwJson = new EGWJson(this);
        JSONObject jsonObject = new JSONObject();
        try {
            json_data = egwJson.loadJSONFromAsset("bookreferences.json");
            jsonObject = egwJson.getJsonObject(json_data);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0; i<dataset.length; i++){
            String book = dataset[i];
            Boolean choice = pref.getBoolean(book, false);
//
            if(choice){
                try {
                    String title = jsonObject.getString(book);
                    Log.i("adding", title);
                    selectedbooks.add(title);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        mAdapter = new ReadingAdapter(selectedbooks, this);
        recyclerView.setAdapter(mAdapter);
    }
}
