package com.example.project_02_exercise_app.database.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.R;
import com.example.project_02_exercise_app.database.entities.Strength;

import java.time.format.DateTimeFormatter;

public class StrengthAdapter extends ListAdapter<Strength, StrengthAdapter.StrengthViewHolder> {

    public StrengthAdapter() {
        super(new StrengthDiff());
    }

    @NonNull
    @Override
    public StrengthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.strength_item, parent, false);
        return new StrengthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StrengthViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class StrengthViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseText, repsText, setsText, weightText, timeText;

        public StrengthViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseText = itemView.findViewById(R.id.exercise_text);
            repsText = itemView.findViewById(R.id.reps_text);
            setsText = itemView.findViewById(R.id.sets_text);
            weightText = itemView.findViewById(R.id.weight_text);
            timeText = itemView.findViewById(R.id.time_text);
        }

        public void bind(Strength s) {
            exerciseText.setText("Exercise: " + s.getStrengthExerciseName());
            repsText.setText("Reps: " + s.getStrengthReps());
            setsText.setText("Sets: " + s.getStrengthSets());
            weightText.setText("Weight: " + s.getStrengthWeight());

            long totalSeconds = s.getStrengthElapsedMs() / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            timeText.setText(String.format("Time: %02d:%02d", minutes, seconds));
        }
    }

    static class StrengthDiff extends DiffUtil.ItemCallback<Strength> {
        @Override
        public boolean areItemsTheSame(@NonNull Strength oldItem, @NonNull Strength newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Strength oldItem, @NonNull Strength newItem) {
            return oldItem.equals(newItem);
        }
    }
}