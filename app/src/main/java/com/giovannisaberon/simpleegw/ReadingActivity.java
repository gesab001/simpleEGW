package com.giovannisaberon.simpleegw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ReadingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReadingAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<String> selectedbooks = new ArrayList<String>(){};
    ArrayList<String> reorderedList = new ArrayList<String>(){};
    JSONObject jsonWritings = new JSONObject();

    ItemTouchHelper touchHelper;

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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        if(pref.contains("reorderedList")){
            String list = pref.getString("reorderedList", null);
            String[] array = list.split("@");
            for (int i=0; i<array.length; i++){
                reorderedList.add(array[i]);// 0 - for private mode
            }
            Log.i("reordered list", reorderedList.toString());

        }
        String json_data = null;
        EGWJson egwJson = new EGWJson(this);
        JSONObject jsonObject = new JSONObject();
        try {
            json_data = egwJson.loadJSONFromAsset("bookreferences.json");
            jsonObject = egwJson.getJsonObject(json_data);
//            json_data = egwJson.loadJSONFromAsset("egw.json");
            HashMap<String, ArrayList<HashMap>> egwmap = egwJson.convertToHashmap();
            ArrayList<HashMap> book = egwmap.get("DA");
            HashMap map = book.get(3);
            String word = map.get("word").toString();
            Log.i("word",word);
//            jsonWritings = egwJson.getJsonObject(json_data);
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


        if (reorderedList.isEmpty()){
            mAdapter = new ReadingAdapter(selectedbooks, this);

        }
        else{
            ArrayList<String> removeitems = new ArrayList<String>(){};
            for (String book : reorderedList) {
                if (selectedbooks.contains(book)) {
                    Log.i("already in the list", book);

                } else {
                    removeitems.add(book);
                    Log.i("remove book", book);

                }
            }

            reorderedList.removeAll(removeitems);
            ArrayList<String> additems = new ArrayList<String>(){};

            for (String book: selectedbooks){
                if (reorderedList.contains(book)){

                }else{
                    additems.add(book);
                }
            }
            reorderedList.addAll(additems);
//            ArrayList<String> words = new ArrayList<String>(){};
//            for (String code : reorderedList){
//                try {
//                    JSONArray jsonArray= jsonWritings.getJSONArray(code);
//                    String word = jsonArray.get(3).toString();
//                    words.add(word);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            mAdapter = new ReadingAdapter(reorderedList, this);

        }
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }
}
