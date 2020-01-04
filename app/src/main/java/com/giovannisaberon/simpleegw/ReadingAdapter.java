package com.giovannisaberon.simpleegw;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    private ArrayList<String> mDataset;
    private Context context;
    private SharedPreferences pref;  // 0 - for private mode
    private SharedPreferences.Editor editor;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        View rowView;
        public MyViewHolder(View v) {
            super(v);
            rowView = v;
            textView =  v.findViewById(R.id.textView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReadingAdapter(ArrayList<String> myDataset, Context context) {

        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reading_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

                String item = mDataset.get(position);
//                String word = selectedparagraphs.get(position).get("word").toString();
                holder.textView.setText(item);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataset, i, i - 1);
            }
        }
        String listString = "";

        for (String s : mDataset)
        {
            listString += s + "@";
        }
        Log.i("reordered list", listString);
        pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putString("reorderedList", listString);
        editor.commit();
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}
