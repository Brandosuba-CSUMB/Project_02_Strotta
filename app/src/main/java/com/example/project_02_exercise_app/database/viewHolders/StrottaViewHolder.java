package com.example.project_02_exercise_app.database.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.R;

public class StrottaViewHolder extends RecyclerView.ViewHolder {
    private final TextView gymLogViewItem;
    private StrottaViewHolder(View strottaView){
        super(strottaView);
        gymLogViewItem = strottaView.findViewById(R.id.recyclerItemTextView);
    }
    public void bind(String text){
        gymLogViewItem.setText(text);
    }
    static StrottaViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.strotta_recycler_item, parent, false);
        return new StrottaViewHolder(view);
    }
}
