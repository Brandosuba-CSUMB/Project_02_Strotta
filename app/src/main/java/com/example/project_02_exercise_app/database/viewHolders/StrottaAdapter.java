package com.example.project_02_exercise_app.database.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.entities.Calisthenics;
import com.example.project_02_exercise_app.database.entities.Strotta;

import java.util.function.Consumer;

public class StrottaAdapter extends ListAdapter<Strotta, StrottaViewHolder> {
    private java.util.function.Consumer<Strotta> longClickListener;

    public StrottaAdapter() {
        super(StrottaDiff);
    }

    @Override
    public StrottaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StrottaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull StrottaViewHolder holder, int position) {
        Strotta s = getItem(position);
        holder.bind(s);
        holder.itemView.setOnClickListener(view -> {
            if(longClickListener != null){
                longClickListener.accept(s);
            }
        });
    }
    public void setOnItemLongClick(Consumer<Strotta> l){
        longClickListener = l;
    }


    private static final DiffUtil.ItemCallback<Strotta> StrottaDiff =
            new DiffUtil.ItemCallback<Strotta>() {
                @Override
                public boolean areItemsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

