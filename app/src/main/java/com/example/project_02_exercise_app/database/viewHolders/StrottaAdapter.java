package com.example.project_02_exercise_app.database.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.project_02_exercise_app.database.entities.Strotta;


public class StrottaAdapter extends ListAdapter<Strotta, StrottaViewHolder> {
    public StrottaAdapter(DiffUtil.ItemCallback<Strotta> diffCallback){
        super(diffCallback);
    }
    @NonNull
    @Override
    public StrottaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return StrottaViewHolder.create(parent);
    }
    @Override
    public void onBindViewHolder(@NonNull StrottaViewHolder holder, int position){
        Strotta current = getItem(position);
        holder.bind(current.toString());
    }
    public static class StrottaDiff extends DiffUtil.ItemCallback<Strotta>{
        @Override
        public boolean areItemsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem){
            return oldItem == newItem;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Strotta oldItem, @NonNull Strotta newItem){
            return oldItem.equals(newItem);
        }
    }
}
