package com.giovannisaberon.simpleegw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent readingIntent = new Intent(this, ReadingActivity.class);

        Button reading_button = (Button) findViewById(R.id.reading_button);
        reading_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(readingIntent);
            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
//        TextView textview = (TextView) findViewById(R.id.textview);
        editor.putBoolean("bool", true); // Storing boolean - true/false
        editor.putString("string", "string value"); // Storing string
        editor.putInt("int", 1); // Storing integer
        editor.putFloat("float", 1); // Storing float
        editor.putLong("long", 1); // Storing long
        Set<String> books = new HashSet<String>();
        books.add("DA");
        books.add("CG");
        books.add("CD");
        books.add("Ed");
        books.add("MCP1");
        books.add("MCP2");
        books.add("PP");
        books.add("PK");
        books.add("AA");
        books.add("GC");
        books.add("MH");
        books.add("LDE");
        books.add("CL");
        editor.putStringSet("books", books);
        editor.commit(); // commit changes

        String string = pref.getString("string", null); // getting String
//        textview.setText(string);
        pref.getInt("int", -1); // getting Integer
        pref.getFloat("float", 0); // getting Float
        pref.getLong("long", 0); // getting Long
        pref.getBoolean("bool", false); // getting boolean
        Set<String> bookitems = pref.getStringSet("books", null);
//        textview.setText(bookitems.toString());
        editor.remove("name"); // will delete key name
        editor.remove("email"); // will delete key email

        editor.commit(); // commit changes

        editor.clear();
        editor.commit(); // commit changes

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Resources res = getResources();
        String[] dataset = res.getStringArray(R.array.book_arrays);

        mAdapter = new MyAdapter(dataset, this);
        recyclerView.setAdapter(mAdapter);
    }
}
