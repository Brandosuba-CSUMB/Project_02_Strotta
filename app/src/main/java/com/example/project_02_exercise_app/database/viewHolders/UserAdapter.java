package com.example.project_02_exercise_app.database.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.R;
import com.example.project_02_exercise_app.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private final Consumer<User> onClick;

    public UserAdapter(Consumer<User> onClick){
        this.onClick = onClick;
    }
    @NonNull @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.itemView.setOnClickListener(view -> onClick.accept(user));
    }
    @Override
    public int getItemCount(){
        return users.size();
    }
    public void submitList(List<User> userList){
        this.users = userList;
        notifyDataSetChanged();
    }
    static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        UserViewHolder(View view){
            super(view);
            username = view.findViewById(R.id.username_textview);
        }
    }
}
