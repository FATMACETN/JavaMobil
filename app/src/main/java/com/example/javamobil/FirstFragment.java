package com.example.javamobil;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.javamobil.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.Objects;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    DatabaseHelper dh;
    RecyclerView recyclerView;


    ArrayList<String> todoId, todoText, soundName, dateTime, alarmTime;
    CustomAdapter customAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_item);
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }

        });
        dh = new DatabaseHelper(getContext());
        todoId = new ArrayList<>();
        todoText = new ArrayList<>();
        soundName = new ArrayList<>();
        dateTime = new ArrayList<>();
        alarmTime = new ArrayList<>();
        storeDataInArrays();
        customAdapter = new CustomAdapter(getActivity(), todoId, todoText, soundName, dateTime, alarmTime);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void storeDataInArrays() {
        Cursor cursor = dh.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Veri yok", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                todoId.add(cursor.getString(0));
                todoText.add(cursor.getString(1));
                soundName.add(cursor.getString(2));
                dateTime.add(cursor.getString(3));
                alarmTime.add(cursor.getString(4));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}