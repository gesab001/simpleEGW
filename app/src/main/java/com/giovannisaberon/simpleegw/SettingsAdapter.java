package com.giovannisaberon.simpleegw;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView textView;
        public Switch bookswitch;
        public MyViewHolder(View v) {
            super(v);
//            textView =  v.findViewById(R.id.textView);
            bookswitch = v.findViewById(R.id.switch1);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SettingsAdapter(String[] myDataset, Context context) {

        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String item = mDataset[position];

//        holder.textView.setText(String.valueOf(position+1)+". " +item);
        final SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        holder.bookswitch.setText(item);
        Boolean bookIsOn = pref.getBoolean(holder.bookswitch.getText().toString(), false);
        holder.bookswitch.setChecked(bookIsOn);
        holder.bookswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                //                // true if the switch is in the On position


//        TextView textview = (TextView) findViewById(R.id.textview);
//                    if(isChecked){
//                        editor.putBoolean(mDataset[position], true); // Storing boolean - true/false
//                        Boolean bool = pref.getBoolean(mDataset[position], true);
//                        Log.i(mDataset[position], isChecked);
//                    }else{
//                        editor.putBoolean(mDataset[position], false); // Storing boolean - true/false
//                        Boolean bool = pref.getBoolean(mDataset[position], false);
//                    }
                    Boolean checked = isChecked;
                    Log.i(checked.toString(), holder.bookswitch.getText().toString());

                        editor.putBoolean(holder.bookswitch.getText().toString(), checked); // Storing boolean - true/false
                        editor.commit();
                        Boolean choice = pref.getBoolean(holder.bookswitch.getText().toString(), false);
                        Log.i(holder.bookswitch.getText().toString(), choice.toString());

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}