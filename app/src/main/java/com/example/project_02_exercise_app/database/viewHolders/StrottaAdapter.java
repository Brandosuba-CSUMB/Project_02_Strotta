package com.example.project_02_exercise_app.database.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.project_02_exercise_app.database.entities.Strotta;


public class StrottaAdapter extends ListAdapter<Strotta, StrottaViewHolder> {
    public StrottaAdapter() {
        super(StrottaDiff);
    }

    @Override
    public StrottaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StrottaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull StrottaViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Strotta> StrottaDiff =
            new DiffUtil.ItemCallback<Strotta>() {
                @Override
                public boolean areItemsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem) {
                    return oldItem.getCardio() == newItem.getCardio();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

