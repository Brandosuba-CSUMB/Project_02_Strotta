package com.example.project_02_exercise_app.database.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.R;
import com.example.project_02_exercise_app.database.entities.Strotta;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StrottaViewHolder extends RecyclerView.ViewHolder {
    private final TextView headline, sub;
    private StrottaViewHolder(View v){
        super(v);
        headline = v.findViewById(R.id.tvHeadline);
        sub = v.findViewById(R.id.tvSub);
    }
    public void bind(Strotta s){
        int totalSec = s.getSeconds();

        double pace = s.paceMinPerKm();
        int paceMin = (int) pace;
        int paceSec = (int) ((pace - paceMin) * 60);

        String summary = String.format(Locale.US,
                "%.2f km   |   %d sec   |   %02d:%02d km/min",
                s.getDistanceKm(),
                totalSec,
                paceMin,
                paceSec
        );

        headline.setText(summary);

        sub.setText(
                s.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a"))
        );
    }
    static StrottaViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.strotta_recycler_item, parent, false);
        return new StrottaViewHolder(view);
    }
}
