package com.example.javamobil;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.javamobil.databinding.FragmentSecondBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    SharedPreferences sp;
    TextView ringText;
    TextView dateText;
    TextView timeText;
    EditText getNote;
    DatabaseHelper dh;
    Spinner spin;
    ArrayList<String> todoId,todoText, soundName, dateTime, alarmTime;;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    String[] array = { "10 dakika önce hatırlat", "20 dakika önce hatırlat", "30 dakika önce hatırlat", "40 dakika önce hatırlat"};

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        dh = new DatabaseHelper(getContext());
        todoId = new ArrayList<>();
        todoText = new ArrayList<>();
        soundName = new ArrayList<>();
        dateTime = new ArrayList<>();
        alarmTime = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);
        dateText = view.findViewById(R.id.dateText);
        timeText = view.findViewById(R.id.timeText);
        ringText = view.findViewById(R.id.ringText);
        getNote= view.findViewById(R.id.icerictext);
        sp = view.getContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        binding.editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Tarih");
                long todayInMillis = MaterialDatePicker.todayInUtcMilliseconds();
                builder.setSelection(todayInMillis);
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Tarih")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        // Seçilen tarih ile ilgili işlemleri burada yapabilirsiniz
                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection));
                        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
                        Date date1 = null;
                        try {
                            date1 = formatter.parse(date);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Date date2 = new Date();
                        date2.setTime(date2.getDay()-1);
                        if (date1.after(date2)) {
                            dateText.setText(date);
                        } else {
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getContext(), "Hatalı Tarih Seçtiniz", duration);
                            toast.show();
                        }
                    }
                });
                datePicker.show(getChildFragmentManager(), "tag");
            }
        });
        binding.editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Alarm saati")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeText.setText(MessageFormat.format("{0}:{1}", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute())));
                    }
                });
                timePicker.show(getChildFragmentManager(), "tag");
            }
        });
        Field[] fields = R.raw.class.getFields();
        List<String> fileNamesList = new ArrayList<>();

        for (Field field : fields) {
            String fileName = field.getName();
            fileNamesList.add(fileName);
        }
        String[] fileNamesArray = new String[fileNamesList.size()];
        fileNamesArray = fileNamesList.toArray(fileNamesArray);
        String[] finalFileNamesArray = fileNamesArray;
        binding.editRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Ses Seçiniz");
                int checkedItem = 1;
                alertDialog.setSingleChoiceItems(finalFileNamesArray, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Seçilen öğeyi almak için bu kısmı kullanabilirsiniz.
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition != -1) {
                            ringText.setText(finalFileNamesArray[selectedPosition]);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

        spin = (Spinner) view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
                String currenttimeMinute = dateFormat.format(new Date());
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh");
                String currenttimeHour = dateFormat2.format(new Date());
                int hrdiff = Integer.parseInt(timeText.getText().subSequence(0,2).toString()) - Integer.parseInt(currenttimeHour);
                int hrinms = hrdiff * 3600000;
                int mindiff = Integer.parseInt(timeText.getText().subSequence(3,5).toString()) - Integer.parseInt(currenttimeMinute);
                int mininms = mindiff * 60000;
                int totalms = hrinms + mininms;
                editor.putInt("totalms", totalms);
                editor.apply();
                dh.addToDo(getNote.getText().toString().trim(),ringText.getText().toString().trim(),dateText.getText().toString().trim(),timeText.getText().toString().trim(),spin.getSelectedItem().toString().trim());
                startAlarm();
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    private void startAlarm() {
        storeDataInArrays();
        int year = Integer.parseInt(dateText.getText().toString().substring(6,10));
        int month =Integer.parseInt(dateText.getText().toString().substring(3,5))-1;
        int day = Integer.parseInt(dateText.getText().toString().substring(0,2));
        int hour = Integer.parseInt(timeText.getText().toString().substring(0,2));
        int minute = Integer.parseInt(timeText.getText().toString().substring(3,5));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra("sound",ringText.getText().toString());

        intent.putExtra("id",todoId.get(todoId.size()-1));
        intent.putExtra("text",getNote.getText().toString());
        Log.d("TAG", "startAlarm: "+calendar.getTimeInMillis()+"  "+System.currentTimeMillis()+"   "+ todoId.get(todoId.size()-1));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.parseInt(todoId.get(todoId.size()-1)), intent,FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-6000000*Integer.parseInt(spin.getSelectedItem().toString().substring(0,1)), pendingIntent);
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
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getContext(), "Nofication and Music Alarm cancelled", Toast.LENGTH_SHORT).show();
    }
}