package com.example.javamobil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList todoId;
    ArrayList todoText;
    ArrayList soundName;
    ArrayList dateTime;
    ArrayList alarmTime;
    DatabaseHelper databaseHelper;

    CustomAdapter(Context context, ArrayList todoId,
                  ArrayList todoText,
                  ArrayList soundName,
                  ArrayList dateTime,
                  ArrayList alarmTime) {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
        this.todoId = todoId;
        this.todoText = todoText;
        this.soundName = soundName;
        this.dateTime = dateTime;
        this.alarmTime = alarmTime;
        Log.d("size", "CustomAdapter: " + getItemCount());
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("CustomAdapter", "onCreateViewHolder called");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        view.refreshDrawableState();
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("CustomAdapter", "onBindViewHolder called for position: " + position);
        holder.todoTextText.setText(String.valueOf(todoText.get(position)));
        holder.dateTimeText.setText(String.valueOf(dateTime.get(position)));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: "+todoId.get(position));
                confirmDialog((String) todoId.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoTextText, dateTimeText;
        CardView cardView;

        ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            todoTextText = itemView.findViewById(R.id.explanation);
            dateTimeText = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.card);
        }
    }
    void confirmDialog(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete "+ " ?");
        builder.setMessage("Are you sure you want to delete " +" ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(context);
                myDB.deleteOneRow(id);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
