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

    private final TextView titleView;
    private StrottaViewHolder(View v){
        super(v);
        titleView = v.findViewById(R.id.logTitleTextView);

        headline = v.findViewById(R.id.tvHeadline);
        sub = v.findViewById(R.id.tvSub);
    }
    public void bind(Strotta s){
        int totalSec = s.getSeconds();

        titleView.setText(s.getTitle());

        String summary;
        if (!s.isCalisthenics()) {
            double pace = s.paceMinPerKm();
            int paceMin = (int) pace;
            int paceSec = (int) ((pace - paceMin) * 60);

            summary = String.format(Locale.US,
                    "%.2f km   |   %d sec   |   %02d:%02d km/min",
                    s.getDistanceKm(),
                    totalSec,
                    paceMin,
                    paceSec
            );
        } else {
            double bodyWeight = s.getBodyWeight();
            String exercise = s.getCalisthenicsExercise();

            summary = String.format(Locale.US,
                    "%s   |   %.1f lbs   |   %02d:%02d:%02d",
                    exercise, bodyWeight, getHours(totalSec), getMinutes(totalSec), getSeconds(totalSec));
        }

        headline.setText(summary);

        sub.setText(
                s.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a"))
        );

    }

    int getHours(int totalSec) {
        return totalSec/3600;
    }

    int getMinutes(int totalSec) {
        return (totalSec%3600)/60;
    }

    int getSeconds(int totalSec) {
        return (totalSec%60)%60;
    }
    static StrottaViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.strotta_recycler_item, parent, false);
        return new StrottaViewHolder(view);
    }
}
