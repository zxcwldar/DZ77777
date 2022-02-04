package com.example.todomanager06.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todomanager06.App;
import com.example.todomanager06.R;
import com.example.todomanager06.databinding.FragmentCreateTaskBinding;
import com.example.todomanager06.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateTaskFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {
    FragmentCreateTaskBinding binding;
    private int startYear;
    private int startMonth;
    private int startDay;

    private String date;
    private String repeat;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClickers();
    }

    private void initClickers() {
        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToDataBase();
                dismiss();
            }
        });
        binding.chooseDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        binding.chooseRepeatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRepeatDialog();
            }
        });
    }

    private void writeToDataBase() {
        String text = binding.taskEd.getText().toString();
        TaskModel taskModel = new TaskModel(text, date, repeat);
        App.getApp().getDb().taskDao().insert(taskModel);
        Map<String, String> task = new HashMap<>();
        task.put("task", taskModel.getTask());
        task.put("date", taskModel.getDate());
        task.put("repeat", taskModel.getRepeat());

        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    private void showRepeatDialog() {
        Dialog alertDialog = new Dialog(requireContext(), R.style.CustomBottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.repeat_dialog, requireView().findViewById(R.id.bottom_shit_con));

        alertDialog.setContentView(view);
        alertDialog.show();


        RadioButton never = alertDialog.findViewById(R.id.never_radioBtn);
        RadioButton everyDay = alertDialog.findViewById(R.id.Every_day_btn);
        RadioButton everyWeer = alertDialog.findViewById(R.id.Every_week_btn);
        RadioButton everyMonth = alertDialog.findViewById(R.id.Every_month_btn);
        RadioButton everyYear = alertDialog.findViewById(R.id.Every_year_btn);
        RadioButton custom = alertDialog.findViewById(R.id.Custom_btn);
        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String never = "Never";
                binding.chooseRepeatTv.setText(never);
                repeat = never;
                alertDialog.dismiss();
            }
        });
        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Every = "Every day";
                binding.chooseRepeatTv.setText(Every);
                repeat = Every;
                alertDialog.dismiss();
            }
        });
        everyWeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String week = "Every week";
                binding.chooseRepeatTv.setText(week);
                repeat = week;
                alertDialog.dismiss();
            }
        });
        everyMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month = "Every month";
                binding.chooseRepeatTv.setText(month);
                repeat = month;
                alertDialog.dismiss();
            }
        });
        everyYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String year = "Every year";
                binding.chooseRepeatTv.setText(year);
                repeat = year;
                alertDialog.dismiss();
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String custom = " Custom";
                binding.chooseRepeatTv.setText(custom);
                repeat = custom;
                alertDialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        date = "" + day + "." + month + 1 + "." + year;
        binding.chooseDateTv.setText("" + day + "." + month + 1 + "." + year);
    }
}