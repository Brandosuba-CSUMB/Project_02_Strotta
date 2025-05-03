package com.example.project_02_exercise_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.entities.Run;

import java.util.ArrayList;
import java.util.List;

public class RunAdapter extends RecyclerView.Adapter<RunAdapter.RunViewHolder> {
    private List<Run> runs = new ArrayList<>();

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_item, parent, false);        return new RunViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {
        Run run = runs.get(position);

        float km = run.getDistantMeters()/1000f;
        long ms = run.getTotalTime();
        long totalSec = ms/1000;
        long mm = (totalSec % 3600) / 60;
        long hh = totalSec / 3600;
        long ss = totalSec %60;
        String timeStr = hh >0 ?
                String.format("%d:%02d:%02d",hh,mm,ss) : String.format("%02d:%02d",mm,ss);
        float pace = (ms/1000f) / km;
        int pMin = (int) (pace / 60);
        int pSec = Math.round(pace) % 60;
        String paceStr = String.format("%d:%02d min/km",pMin,pSec);
        holder.runDate.setText("Time: "+timeStr);
        holder.runDistance.setText(String.format("Distance: %.2f km | Pace: %s",km,paceStr));
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
    public void setRuns(List<Run> runs){
        this.runs = (runs == null) ? new ArrayList<>() : runs;
        notifyDataSetChanged();
    }
    public static class RunViewHolder extends RecyclerView.ViewHolder{
        TextView runDate;
        TextView runDistance;
        public RunViewHolder(@NonNull View itemView){
            super(itemView);
            runDate = itemView.findViewById(R.id.run_date);
            runDistance = itemView.findViewById(R.id.run_distance);
        }
    }
}
